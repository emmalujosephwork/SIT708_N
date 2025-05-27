import os
import requests
from flask import Flask, request, jsonify

app = Flask(__name__)

# ─── Configuration ────────────────────────────────────────────────────────────

HF_CHAT_API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions"
HF_API_TOKEN    = os.getenv("HF_API_TOKEN", "")
HEADERS = {
    "Authorization": f"Bearer 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",
    "Content-Type": "application/json"
}
MODEL = "deepseek/deepseek-v3-0324"  # or your preferred chat model

# ─── Helper ───────────────────────────────────────────────────────────────────

def fetch_chat_reply(user_message: str) -> str:
    payload = {
        "model": MODEL,
        "messages": [
            {"role": "system",  "content": "You are a helpful teaching assistant."},
            {"role": "user",    "content": user_message}
        ],
        "temperature": 0.7,
        "max_tokens": 500,
        "top_p": 0.9
    }
    resp = requests.post(HF_CHAT_API_URL, headers=HEADERS, json=payload)
    if resp.status_code != 200:
        raise RuntimeError(f"HF API error {resp.status_code}: {resp.text}")
    return resp.json()["choices"][0]["message"]["content"].strip()

# ─── Routes ───────────────────────────────────────────────────────────────────

@app.route("/", methods=["GET"])
def home():
    return "🤖 Chatbot backend is up!", 200

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json(force=True)
    user = data.get("username")
    msg  = data.get("message")
    if not user or not msg:
        return jsonify({"error": "username and message fields are required"}), 400

    try:
        reply = fetch_chat_reply(msg)
        return jsonify({"reply": reply}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# ─── Main ─────────────────────────────────────────────────────────────────────

if __name__ == "__main__":
    port = int(os.getenv("PORT", 5000))
    print(f"Chatbot backend listening on port {port}")
    app.run(host="0.0.0.0", port=port)
