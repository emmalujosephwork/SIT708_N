import os
import uuid
from flask import Flask, request, jsonify
from pymongo import MongoClient
from werkzeug.security import generate_password_hash, check_password_hash
from dotenv import load_dotenv
import requests

load_dotenv()

app = Flask(__name__)

# MongoDB connection
MONGO_URI = os.getenv("MONGO_URI")
client = MongoClient(MONGO_URI)
db = client.get_default_database()

# Ollama API or your AI endpoint (update if using other model)
OLLAMA_API_URL = "http://localhost:11434/api/generate"
MODEL = "llama3.2:latest"

# --- AUTH ROUTES ---

@app.route('/signup', methods=['POST'])
def signup():
    data = request.get_json()
    full_name = data.get("full_name")
    email = data.get("email")
    password = data.get("password")
    confirm_password = data.get("confirm_password")

    if not all([full_name, email, password, confirm_password]):
        return jsonify({"error": "Missing fields"}), 400

    if password != confirm_password:
        return jsonify({"error": "Passwords do not match"}), 400

    if db.users.find_one({"email": email}):
        return jsonify({"error": "Email already registered"}), 400

    hashed_password = generate_password_hash(password)

    user_doc = {
        "full_name": full_name,
        "email": email,
        "password": hashed_password
    }

    db.users.insert_one(user_doc)

    return jsonify({"message": "User registered successfully"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    email = data.get("email")
    password = data.get("password")

    if not email or not password:
        return jsonify({"error": "Missing email or password"}), 400

    user = db.users.find_one({"email": email})
    if user and check_password_hash(user['password'], password):
        return jsonify({"message": "Login successful", "full_name": user["full_name"], "email": user["email"]}), 200
    else:
        return jsonify({"error": "Invalid email or password"}), 401


# --- CHAT SESSION & HISTORY ---

@app.route('/chat/start', methods=['POST'])
def start_chat():
    data = request.get_json()
    user_email = data.get("user_email")
    if not user_email:
        return jsonify({"error": "Missing user_email"}), 400

    chat_id = str(uuid.uuid4())
    chat_doc = {
        "chat_id": chat_id,
        "user_email": user_email,
        "messages": [],  # empty chat initially
    }
    db.chats.insert_one(chat_doc)
    return jsonify({"chat_id": chat_id}), 201

@app.route('/chat/list/<user_email>', methods=['GET'])
def list_user_chats(user_email):
    chats = list(db.chats.find({"user_email": user_email}, {"chat_id": 1, "messages": 1}))
    chat_summaries = []
    for chat in chats:
        last_message = chat["messages"][-1]["text"] if chat.get("messages") else "(empty)"
        chat_summaries.append({
            "chat_id": chat["chat_id"],
            "last_message": last_message
        })
    return jsonify(chat_summaries)

@app.route('/chat/<chat_id>', methods=['GET'])
def get_chat_messages(chat_id):
    chat = db.chats.find_one({"chat_id": chat_id})
    if not chat:
        return jsonify({"error": "Chat not found"}), 404
    return jsonify({"messages": chat.get("messages", [])})

@app.route('/chat/<chat_id>/send', methods=['POST'])
def send_chat_message(chat_id):
    data = request.get_json()
    message_text = data.get("message")
    sender = data.get("sender")  # "user" or "bot"
    if not message_text or not sender:
        return jsonify({"error": "Missing message or sender"}), 400

    chat = db.chats.find_one({"chat_id": chat_id})
    if not chat:
        return jsonify({"error": "Chat not found"}), 404

    new_message = {
        "text": message_text,
        "sender": sender,
    }
    db.chats.update_one({"chat_id": chat_id}, {"$push": {"messages": new_message}})

    return jsonify({"message": "Message saved"}), 200


# --- AI CHAT (existing) ---

@app.route('/chat', methods=['POST'])
def chat():
    data = request.get_json()
    user_message = data.get('message')
    if not user_message:
        return jsonify({"error": "Missing message"}), 400

    prompt = f"You are a helpful AI travel planner assistant. Answer the following:\n{user_message}"

    payload = {
        "model": MODEL,
        "prompt": prompt,
        "stream": False,
        "options": {
            "temperature": 0.7,
            "top_p": 0.9,
            "num_predict": 250
        }
    }

    try:
        response = requests.post(OLLAMA_API_URL, json=payload)
        response.raise_for_status()
        result = response.json()
        ai_reply = result.get("response", "").strip()
    except Exception as e:
        print(f"Error calling Ollama API: {e}")
        return jsonify({"error": "Failed to get AI response"}), 500

    if not ai_reply:
        ai_reply = "Sorry, I couldn't generate a travel plan. Please try again."

    return jsonify({"reply": ai_reply})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
