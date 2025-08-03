from flask import Flask, request, jsonify
import tempfile
import os
from paddleocr import PaddleOCR
from PIL import Image, ImageOps

app = Flask(__name__)
ocr = PaddleOCR(use_angle_cls=True, lang='ch')

@app.route('/ocr', methods=['POST'])
def ocr_image():
    uploaded_file = request.files.get('file')
    
    if uploaded_file:
        print("Nombre del archivo recibido:", uploaded_file.filename)

        # Redimensionamos la imagen y la procesamos para que el modelo OCR tenga menos problemas al analizarla
        img = Image.open(uploaded_file.stream)
        img.thumbnail((512, 512))
        padding = 50
        img = ImageOps.expand(img, border=padding, fill='white')


        # Crear archivo temporal que se eliminará automáticamente al salir del bloque
        with tempfile.NamedTemporaryFile(delete=True, suffix=os.path.splitext(uploaded_file.filename)[1]) as tmp:
            img.save(tmp.name)  # Guardamos el archivo recibido en el temporal
            print("Archivo temporal guardado en:", tmp.name)
            
            # Aquí podrías hacer procesamiento OCR, por ejemplo:
            result = ocr.predict(tmp.name)
            print(result)
            if result and isinstance(result, list) and 'rec_texts' in result[0]:
                recognized_texts = result[0]['rec_texts']
                text_string = '\n'.join(recognized_texts)
                return text_string
            else:
                return "No se encontró texto reconocido"

if __name__ == '__main__':
    app.run(port=5000, debug=True)
