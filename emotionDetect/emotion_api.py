from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline

# Initialize FastAPI
app = FastAPI()

# Load sentiment analysis pipeline
classifier = pipeline("sentiment-analysis", model="distilbert/distilbert-base-uncased-finetuned-sst-2-english")

# Define request body model
class JournalEntry(BaseModel):
    text: str

@app.post("/analyze-emotion")
def analyze_emotion(entry: JournalEntry):
    result = classifier(entry.text)
    return {"label": result[0]["label"], "score": result[0]["score"]}

# Run the API using: uvicorn emotion_api:app --reload
