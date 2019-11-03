#!/usr/local/bin/python3

# OCR stuff
from PIL import Image
from PIL import ImageFilter
import pytesseract
import pandas
import re

# Firebase stuff
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Google bucket stuff
from google.cloud import storage

# Flask stuff
from flask import Flask, redirect, url_for, request
app = Flask(__name__)

# Stuff stuff
import json
import os

# More firebase stuff, yay
cred = credentials.Certificate(os.environ.get('DEV_G_CERT'))
firebase_admin.initialize_app(cred, {
    'projectId': 'vresume-c96a3',
})

# More Google bucket stuff, more yay
storage_client = storage.Client('vresume-c96a3')
bucket = storage_client.get_bucket('vresume-c96a3.appspot.com')

db = firestore.client()

# Home route for testing
@app.route('/')
def index():
    users = db.collection(u'users').get()
    #docs = users_ref.stream()

    results = []
    for user in users:
        results.append({user.id: user.to_dict()})
        
    return json.dumps(results)

# OCR helper functions
polarization_threshold = 200

def polarizeImage(image):
    gray_doc_image = image.convert('L')
    polarize = lambda pixel: 0 if pixel < polarization_threshold else 255
    bw_doc_image = gray_doc_image.point(polarize, '1') 
    return bw_doc_image

def reversePolarizeImage(image):
    gray_doc_image = image.convert('L')
    polarize = lambda pixel: 255 if pixel < polarization_threshold else 0
    bw_doc_image = gray_doc_image.point(polarize, '1') 
    return bw_doc_image

def getConfidentWords(image):
    confident_words = pytesseract.image_to_data(image, output_type='data.frame').query('conf >= 80')
    return confident_words.iloc[1:, 11].to_numpy()

def combineLists(list_a, list_b):
    return list(set().union(list_a, list_b))

def getAlteredImages(image):
    width, height = image.size

    polarized_image = polarizeImage(image)
    enlarged_image = image.resize((2*width, 2*height))
    #sharpened_image = image.filter(ImageFilter.SHARPEN).filter(ImageFilter.SHARPEN)
    mask = polarized_image
    base = image
    double_image = base.copy()
    double_image.paste(mask, None, reversePolarizeImage(mask))
    return [polarized_image, enlarged_image, double_image]

def getKeywords(image, expedite):
    polarized_image, enlarged_image, double_image = getAlteredImages(image)
    
    if expedite:
        return getConfidentWords(enlarged_image)

    keywords = getConfidentWords(image)
    keywords = combineLists(keywords, getConfidentWords(polarized_image))
    keywords = combineLists(keywords, getConfidentWords(enlarged_image))
    #keywords = combineLists(keywords, getConfidentWords(sharpened_image))
    keywords = combineLists(keywords, getConfidentWords(double_image))
    return keywords

# Process route for processing an image
@app.route('/process', methods = ['POST'])
def process():
    incomingInfo = request.get_json()
    try:
        employerID = incomingInfo['employerID']
        appName    = incomingInfo['appName']
        tags       = incomingInfo['tags']
        expedite   = incomingInfo['expedite']
        
        if expedite is None:
            expedite = False

        files = bucket.list_blobs(prefix='resumes/'+incomingInfo['employerID'])
        fileList = [file.name for file in files if '.' in file.name]

        for file in fileList:
            if incomingInfo['appName'] in file:
                blob = bucket.blob(file)
                blob.download_to_filename("./testResume.JPG")
                break

        image = Image.open('testResume.JPG')
        text = [word.upper() for word in getKeywords(image, expedite)]
        text_alphanumeric = [re.sub(r'\W+', '', word) for word in text]

        found_tags = []
        for tag in tags:
            if tag.upper() in text:
                found_tags.append(tag)
                
        found_tags_alphanumeric = []
        for tag in tags:
            if tag.upper() in text_alphanumeric:
                found_tags_alphanumeric.append(tag)
                
        return json.dumps(combineLists(found_tags, found_tags_alphanumeric))

    except Exception as e:
        return "Failure in process: " + e

# Main function, no touchie
if __name__ == "__main__":
    app.run(port=80, host="0.0.0.0")
