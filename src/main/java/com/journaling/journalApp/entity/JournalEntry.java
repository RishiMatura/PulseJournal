package com.journaling.journalApp.entity;

import com.journaling.journalApp.enums.Sentiment;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    //Add userId or userName to associate journal entries with a specific user
    @NonNull
    private String owner;  // or private ObjectId userId; if you prefer ObjectId
    private Sentiment sentiment;

}
