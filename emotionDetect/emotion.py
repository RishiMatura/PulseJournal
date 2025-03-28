from transformers import pipeline

classifier = pipeline("sentiment-analysis", model="distilbert/distilbert-base-uncased-finetuned-sst-2-english")

result = classifier("I am sad today!")
print(result)
import torch
print(torch.cuda.is_available())  # Should print True if GPU is available