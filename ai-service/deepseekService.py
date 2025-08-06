from flask import Flask, request, jsonify
from openai import OpenAI
import os
import requests
import ast
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY")
DEEPSEEK_API_URL = "https://openrouter.ai/api/v1"  # Modifica si cambia

PROMPT_ANALYZE = """
Toma el siguiente texto en chino y devuélvelo corregido en el caso de que encuentres algún fallo en los caracteres. En ningún caso reescribas nada, solo si encuentras fallos críticos u obvios modifica los caracteres. Si no encuentras nada excesivamente raro, entonces devuelve el texto tal cual estaba. No devuelvas nada más, solo el texto (corregido o sin corregir).

Texto: "{input_text}"
"""

PROMPT_GETTITLES = """
Toma el siguiente texto en chino y devuelve un titulo en inglés y otro en español para el mismo. No devuelvas nada más (ni explicaciones, ni mensajes, ...) solamente los títulos con este formato:

["Título ingles", "Título espanol"]

Texto: "{input_text}"
"""

PROMPT_GETTRANSLATIONS = """
Toma el siguiente texto en chino y tradúcelo completamente al inglés y al español.

Es imprescindible que las traducciones no contengan puntos ".", pero se permiten otros signos de puntuación como comas, puntos y coma, etc.

Devuelve solo un array con dos elementos: la primera traducción en inglés y la segunda en español, exactamente con este formato (usando comillas simples, en formato Python):

['Traducción completa al inglés', 'Traducción completa al español']

No incluyas explicaciones, encabezados ni ningún otro contenido. Solo el array.

Texto: "{input_text}"
"""

PROMPT_GETDESCRIPTIONS = """
Toma el siguiente texto en chino y genera una breve descripción en inglés y otra en español que resulten atractivas para el lector.

Devuelve solo un array con dos elementos: la primera descripción en inglés y la segunda en español, exactamente con este formato:

["English description", "Descripción en español"]

No incluyas explicaciones, encabezados ni texto adicional. Solo el array.

Texto: "{input_text}"
"""

PROMPT_GETWORDSENGLISH = """
Toma el siguiente array de palabras en chino, ya separadas como unidades léxicas individuales. Tu única tarea es devolver un array en formato Python (usa comillas simples), con la traducción literal y palabra por palabra al inglés, manteniendo exactamente el mismo número de elementos y el mismo orden.

No traduzcas frases. No combines palabras. No interpretes. Solo haz la correspondencia directa y literal de una palabra en chino a una palabra (o grupo de palabras si es necesario) en inglés, sin alterar la estructura del array.

Ejemplo de salida:
['hello', 'teacher', 'how', 'to be', 'you']

No añadas nada más. No expliques. Solo devuelve el array.

Array: {input_text}
"""

PROMPT_GETWORDSSPANISH = """
Toma el siguiente array de palabras en chino, ya separadas como unidades léxicas individuales. Tu única tarea es devolver un array en formato Python (usa comillas simples), con la traducción literal y palabra por palabra al español, manteniendo exactamente el mismo número de elementos y el mismo orden.

No traduzcas frases. No combines palabras. No interpretes. Solo haz la correspondencia directa y literal de una palabra en chino a una palabra (o grupo de palabras si es necesario) en español, sin alterar la estructura del array.

Ejemplo de salida:
['hola', 'profesor', 'cómo', 'estar', 'tú']

No añadas nada más. No expliques. Solo devuelve el array.

Array: {input_text}
"""

PROMPT_GETWORDSPINYIN = """
Toma el siguiente array de palabras en chino. Necesito que me devuelvas exclusivamente un array de la misma longitud, con el pinyin correspondiente de cada palabra en el mismo orden.

Devuelve únicamente un array en formato Python (con comillas simples), como este ejemplo:
['pinyin1', 'pinyin2', 'pinyin3']

No añadas explicaciones, ni etiquetas, ni ningún texto adicional. Solo el array.

Array: {input_text}
"""

@app.route("/analyze", methods=["POST"])
def analyze_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]
    print(input_text)

    prompt = PROMPT_ANALYZE.replace("{input_text}", input_text)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[
            {"role": "user", "content": prompt},
        ],
        stream=False
    )

    print(response.choices[0].message.content)
    return response.choices[0].message.content

@app.route("/getTitles", methods=["POST"])
def getTitles_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]

    prompt = PROMPT_GETTITLES.replace("{input_text}", input_text)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[
            {"role": "user", "content": prompt},
        ],
        stream=False
    )

    print(response.choices[0].message.content)
    return response.choices[0].message.content

@app.route("/getTranslations", methods=["POST"])
def getTranslations_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]

    prompt = PROMPT_GETTRANSLATIONS.replace("{input_text}", input_text)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[
            {"role": "user", "content": prompt},
        ],
        stream=False
    )

    raw_output = response.choices[0].message.content
    print(raw_output)

    # Intenta convertir string a lista con ast.literal_eval o json.loads si el formato es JSON
    try:
        import ast
        parsed_list = ast.literal_eval(raw_output)
    except Exception:
        parsed_list = [raw_output]  # fallback a lista con string único

    return jsonify(parsed_list)

@app.route("/getDescriptions", methods=["POST"])
def getDescriptions_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]

    prompt = PROMPT_GETDESCRIPTIONS.replace("{input_text}", input_text)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[
            {"role": "user", "content": prompt},
        ],
        stream=False
    )

    print(response.choices[0].message.content)
    return response.choices[0].message.content

@app.route("/getWordsEnglish", methods=["POST"])
def getWordsEnglish_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]
    input_text_str = str(input_text)

    prompt = PROMPT_GETWORDSENGLISH.replace("{input_text}", input_text_str)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[{"role": "user", "content": prompt}],
        stream=False
    )

    raw_output = response.choices[0].message.content
    print("Modelo (EN):", raw_output)

    try:
        parsed_list = ast.literal_eval(raw_output)
        if not isinstance(parsed_list, list):
            raise ValueError("El modelo no devolvió una lista")
    except Exception as e:
        return jsonify({"error": f"Formato inválido del modelo (EN): {str(e)}"}), 500

    return jsonify(parsed_list)

@app.route("/getWordsSpanish", methods=["POST"])
def getWordsSpanish_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]
    input_text_str = str(input_text)

    prompt = PROMPT_GETWORDSSPANISH.replace("{input_text}", input_text_str)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[{"role": "user", "content": prompt}],
        stream=False
    )

    raw_output = response.choices[0].message.content
    print("Modelo (ES):", raw_output)

    try:
        parsed_list = ast.literal_eval(raw_output)
        if not isinstance(parsed_list, list):
            raise ValueError("El modelo no devolvió una lista")
    except Exception as e:
        return jsonify({"error": f"Formato inválido del modelo (ES): {str(e)}"}), 500

    return jsonify(parsed_list)

@app.route("/getWordsPinyin", methods=["POST"])
def getWordsPinyin_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]
    input_text_str = str(input_text)

    prompt = PROMPT_GETWORDSPINYIN.replace("{input_text}", input_text_str)

    client = OpenAI(api_key=DEEPSEEK_API_KEY, base_url=DEEPSEEK_API_URL)

    response = client.chat.completions.create(
        model="deepseek/deepseek-r1:free",
        messages=[
            {"role": "user", "content": prompt},
        ],
        stream=False
    )

    raw_output = response.choices[0].message.content
    print("Modelo devolvió:", raw_output)

    try:
        parsed_list = ast.literal_eval(raw_output)  # ✅ convierte string tipo lista en lista real
        if not isinstance(parsed_list, list):
            raise ValueError("No es una lista")
    except Exception as e:
        return jsonify({"error": f"Formato inválido del modelo: {str(e)}"}), 500

    return jsonify(parsed_list)  # ✅ ahora devuelve un JSON array válido

if __name__ == "__main__":
    app.run(port=5001, debug=True)
