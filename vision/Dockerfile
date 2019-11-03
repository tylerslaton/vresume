FROM python:3.8

WORKDIR /home

COPY . .

RUN pip3 install             \
        pytesseract          \
        pillow               \
        pdf2image            \
        flask                \
        firebase_admin       \
        google-cloud-storage \
    && apt-get -y update     \ 
    && apt-get -y install    \
        tesseract-ocr        \
    && chmod 700 vision.py 

#CMD ./vision.py