package com.journaling.journalApp.services;

import com.journaling.journalApp.entity.JournalEntry;

public interface AudioService {
    String convertTextToSpeech(String text, String fileName);

    String replaceAudioFile(JournalEntry entry, String newText);

    String generateFileName(JournalEntry entry);

    void deleteAudioFile(String audioUrl);


    }
