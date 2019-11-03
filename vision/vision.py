#!/usr/local/bin/python3
from PIL import Image
from pdf2image import convert_from_path
import pytesseract
import os
from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
    return "Hello World!"


def polarizeImage(image):
    gray_doc_image = image.convert('L')
    polarize = lambda pixel: 0 if pixel < 200 else 255
    bw_doc_image = gray_doc_image.point(polarize, '1') 
    return bw_doc_image

def main():
    image = Image.open('testImages/test.JPG')
    #image.show()
    text = pytesseract.image_to_string(polarizeImage(image))
    print(text)
    print('\nmain complete\n')

if __name__ == "__main__":
    app.run(port=80, host="0.0.0.0")
