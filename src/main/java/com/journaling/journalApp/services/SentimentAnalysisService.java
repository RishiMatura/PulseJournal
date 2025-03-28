package com.journaling.journalApp.services;

import com.journaling.journalApp.enums.Sentiment;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisService {

    public Sentiment getSentiment(String text) {
        // Here you can analyze the text and return the appropriate sentiment
        // For now, we're just returning a hardcoded value as an example
        return Sentiment.OKIDOKI; // or any other sentiment based on the text
    }
}



