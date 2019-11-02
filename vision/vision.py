#!/usr/local/bin/python3
from PIL import Image
from pdf2image import convert_from_path
import pytesseract

def polarizeImage(image):
    gray_doc_image = image.convert('L')
    polarize = lambda pixel: 0 if pixel < 200 else 255
    bw_doc_image = gray_doc_image.point(polarize, '1') 
    return bw_doc_image

def main():
    print('----enter main----')
    image = Image.open('testImages/testImg01.JPG')
    text = pytesseract.image_to_string(polarizeImage(image))
    print(text)
    print('----main complete----')

if __name__ == "__main__":
    main()
