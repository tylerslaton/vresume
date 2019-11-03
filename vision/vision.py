#!/usr/local/bin/python3

# OCR stuff
from PIL import Image
from pdf2image import convert_from_path
import pytesseract

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

# Process route for processing an image
@app.route('/process', methods = ['POST'])
def process():
    incomingInfo = request.get_json()
    try:
        employerID = incomingInfo['employerID']
        appName    = incomingInfo['appName']
        tags       = incomingInfo['tags']

        files = bucket.list_blobs(prefix='resumes/'+incomingInfo['employerID'])
        fileList = [file.name for file in files if '.' in file.name]

        for file in fileList:
            if incomingInfo['appName'] in file:
                blob = bucket.blob(file)
                blob.download_to_filename("./testResume.JPG")

        image = Image.open('testResume.JPG')
        text = pytesseract.image_to_string(polarizeImage(image))

        foundTags = []
        searchingText = text.upper()
        for tag in tags:
            if tag.upper() in searchingText:
                foundTags.append(tag)
        return json.dumps(foundTags)

    except Exception as e:
        return "Failure in process: " + e

# Helper function to improve accuracy
def polarizeImage(image):
    gray_doc_image = image.convert('L')
    polarize = lambda pixel: 0 if pixel < 200 else 255
    bw_doc_image = gray_doc_image.point(polarize, '1') 
    return bw_doc_image

# Main function, no touchie
if __name__ == "__main__":
    app.run(port=80, host="0.0.0.0")
