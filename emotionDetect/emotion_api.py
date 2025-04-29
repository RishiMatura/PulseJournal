from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Usage multi-label emotion model
classifier = pipeline(
    "text-classification",
    model="j-hartmann/emotion-english-distilroberta-base",
    return_all_scores=True
)

class JournalEntry(BaseModel):
    text: str

@app.post("/analyze-emotion")
def analyze_emotion(entry: JournalEntry):
    results = classifier(entry.text)[0]
    sorted_results = sorted(results, key=lambda x: x['score'], reverse=True)

    return {
        "top_emotion": sorted_results[0],
        "all_emotions": sorted_results
    }
