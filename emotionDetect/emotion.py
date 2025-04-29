# from transformers import pipeline
#
# classifier = pipeline("sentiment-analysis", model="distilbert/distilbert-base-uncased-finetuned-sst-2-english")
#
# result = classifier("I am sad today!")
# print(result)
# import torch
# print(torch.cuda.is_available())  # Should print True if GPU is available


# from transformers import pipeline
#
# classifier = pipeline(
#     "text-classification",
#     model="bhadresh-savani/distilbert-base-uncased-emotion",
#     return_all_scores=True
# )
#
# # Check the model is loaded by printing the classifier's output
# print(classifier("I feel so happy today!"))


from transformers import pipeline

classifier = pipeline(
    "text-classification",
    model="j-hartmann/emotion-english-distilroberta-base",
    return_all_scores=True
)

text = "I’m feeling very anxious about tomorrow."

results = classifier(text)[0]
sorted_results = sorted(results, key=lambda x: x['score'], reverse=True)

print("Top emotion:", sorted_results[0])
print("All emotions:", sorted_results)
