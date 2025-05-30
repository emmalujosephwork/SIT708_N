import os
import requests
import re
from flask import Flask, request, jsonify
from pymongo import MongoClient
from dotenv import load_dotenv

load_dotenv()  # Load environment variables from .env file

app = Flask(__name__)

# MongoDB connection using pymongo.MongoClient (not flask_pymongo)
MONGO_URI = os.getenv("MONGO_URI")

if not MONGO_URI:
    raise Exception("MONGO_URI environment variable not set!")

client = MongoClient(MONGO_URI)
db = client.get_default_database()  # or specify database name here: client['your_db_name']

# Test MongoDB connection on startup
try:
    client.admin.command("ping")
    print("MongoDB connection successful!")
except Exception as e:
    print("MongoDB connection failed:", e)

# HuggingFace API setup
API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions"
HF_API_TOKEN = os.getenv('HF_API_TOKEN')
HEADERS = {"Authorization": f"Bearer {HF_API_TOKEN}"}
MODEL = "deepseek/deepseek-v3-0324"

def fetchQuizFromLlama(student_topic):
    payload = {
        "messages": [
            {
                "role": "user",
                "content": (
                    f"Generate a quiz with 3 questions to test students on the provided topic. "
                    f"For each question, generate 4 options where only one of the options is correct. "
                    f"Format your response as follows:\n"
                    f"**QUESTION 1:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"**QUESTION 2:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"**QUESTION 3:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"Here is the student topic:\n{student_topic}"
                )
            }
        ],
        "model": MODEL,
        "max_tokens": 500,
        "temperature": 0.7,
        "top_p": 0.9
    }
    response = requests.post(API_URL, headers=HEADERS, json=payload)
    if response.status_code == 200:
        return response.json()["choices"][0]["message"]["content"]
    else:
        raise Exception(f"API request failed: {response.status_code} - {response.text}")

def process_quiz(quiz_text):
    questions = []
    pattern = re.compile(
        r'\*\*QUESTION \d+:\*\* (.+?)\n'
        r'\*\*OPTION A:\*\* (.+?)\n'
        r'\*\*OPTION B:\*\* (.+?)\n'
        r'\*\*OPTION C:\*\* (.+?)\n'
        r'\*\*OPTION D:\*\* (.+?)\n'
        r'\*\*ANS:\*\* (.+?)(?=\n|$)',
        re.DOTALL
    )
    matches = pattern.findall(quiz_text)
    for match in matches:
        questions.append({
            "questionText": match[0].strip(),
            "options": [match[1].strip(), match[2].strip(), match[3].strip(), match[4].strip()],
            "correctAnswer": match[5].strip()
        })
    return questions

@app.route('/getQuiz', methods=['GET'])
def get_quiz():
    student_topic = request.args.get('topic')
    if not student_topic:
        return jsonify({'error': 'Missing topic parameter'}), 400
    try:
        quiz_text = fetchQuizFromLlama(student_topic)
        quiz_data = process_quiz(quiz_text)
        return jsonify({'quiz': quiz_data})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/mongoQuizzes', methods=['GET'])
def get_mongo_quizzes():
    quizzes = list(db.quizzes.find())
    for quiz in quizzes:
        quiz["_id"] = str(quiz["_id"])
    return jsonify(quizzes)

@app.route('/mongoQuizzes', methods=['POST'])
def add_mongo_quiz():
    data = request.json
    app.logger.info(f"Received POST /mongoQuizzes with data: {data}")

    username = data.get("username")
    quiz = data.get("quiz")
    user_answers = data.get("user_answers")  # List of selected answers
    score = data.get("score")  # User's score

    if not username or not quiz or user_answers is None or score is None:
        return jsonify({"error": "Missing required fields"}), 400

    quiz_doc = {
        "username": username,
        "quiz": quiz,
        "user_answers": user_answers,
        "score": score
    }

    try:
        result = db.quizzes.insert_one(quiz_doc)
        quiz_doc["_id"] = str(result.inserted_id)
        return jsonify(quiz_doc), 201
    except Exception as e:
        app.logger.error(f"Failed to insert quiz into MongoDB: {e}")
        return jsonify({"error": "Failed to save quiz results"}), 500

# NEW: Get quiz history for specific user
@app.route('/getUserQuizHistory', methods=['GET'])
def get_user_quiz_history():
    username = request.args.get('username')
    if not username:
        return jsonify({'error': 'Missing username parameter'}), 400
    try:
        user_quizzes = list(db.quizzes.find({"username": username}))
        for quiz in user_quizzes:
            quiz["_id"] = str(quiz["_id"])
        return jsonify(user_quizzes), 200
    except Exception as e:
        app.logger.error(f"Failed to fetch quiz history: {e}")
        return jsonify({'error': 'Failed to fetch quiz history'}), 500

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
