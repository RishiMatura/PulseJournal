package com.journaling.journalApp.entity;

import com.journaling.journalApp.enums.Sentiment;
import com.journaling.journalApp.models.EmotionAnalysisResponse;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
@Component
public class JournalEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;

    private String audioUrl;
    private String emotion; // 🆕 New field for emotion

    private EmotionAnalysisResponse.Emotion topEmotion;  // Top emotion from emotion analysis
    private List<EmotionAnalysisResponse.Emotion> allEmotions;  // All emotions detected from emotion analysis
    private Sentiment sentiment;  // Sentiment analysis result

    //Add userId or userName to associate journal entries with a specific user
    @NonNull
    private String owner;  // or private ObjectId userId; if you prefer ObjectId
//    private Sentiment sentiment;

}
