package com.journaling.journalApp.scheduler;

import com.journaling.journalApp.cache.AppCache;
import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.entity.User;
import com.journaling.journalApp.enums.Sentiment;
import com.journaling.journalApp.repository.UserRepositoryImpl;
import com.journaling.journalApp.services.EmailService;
import com.journaling.journalApp.services.MailSlurpService;
import com.journaling.journalApp.services.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired      private EmailService emailService;
    @Autowired      private UserRepositoryImpl userRepository;
    @Autowired      private SentimentAnalysisService sentimentAnalysisService;
    @Autowired      private AppCache appCache;
    @Autowired      private MailSlurpService mailSlurpService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSAMail(){
        List<User> users = userRepository.getUserForSA();

        for(User user : users){
            List<JournalEntry> journalEntries= user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                    emailService.sendEmail(user.getEmail(), "Sentiment for previous week", mostFrequentSentiment.toString());
                }
            }
        }
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
