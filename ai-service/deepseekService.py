from flask import Flask, request, jsonify
from openai import OpenAI
import os
import requests
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY")
DEEPSEEK_API_URL = "https://openrouter.ai/api/v1"  # Modifica si cambia

PROMPT_TEMPLATE = """
Toma el siguiente texto en chino y devuélvelo desglosado en frases y palabras, con su traducción al español y el pinyin correspondiente. Usa un formato con este esquema:

{
  "original": "...",
  "segments": [
    {
      "phrase": "...",
      "pinyin": "...",
      "translation": "..."
    }
  ],
  "words": [
    {
      "word": "...",
      "pinyin": "...",
      "translation": "..."
    }
  ],
  "full_translation": "..."
}

Texto: "{input_text}"
"""

@app.route("/analyze", methods=["POST"])
def analyze_text():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "No se envió texto"}), 400

    input_text = data["text"]
    print(input_text)

    prompt = PROMPT_TEMPLATE.replace("{input_text}", input_text)

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

if __name__ == "__main__":
    app.run(port=5001, debug=True)
