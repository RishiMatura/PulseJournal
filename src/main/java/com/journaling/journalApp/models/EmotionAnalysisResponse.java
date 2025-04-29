package com.journaling.journalApp.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmotionAnalysisResponse {
    private Emotion top_emotion;
    private List<Emotion> all_emotions;

    // Inner class for Emotion
    @Getter
    @Setter
    public static class Emotion {
        private String label;
        private double score;
    }
}