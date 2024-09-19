package com.journaling.journalApp.services;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.models.SendEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
@Service
public class MailSlurpService {

    private final InboxControllerApi inboxControllerApi;

    @Value("${mailslurp-api-key}")     private String API_KEY;
    @Value("${mailslurp-inboxID}")     private String INBOX_ID;

    public MailSlurpService() {
        ApiClient mailslurpClient = new ApiClient();
        mailslurpClient.setApiKey(API_KEY);
        this.inboxControllerApi = new InboxControllerApi(mailslurpClient);
    }

    // Function to send an email using MailSlurp
    public void sendTestEmail(String toEmail, String subject, String body) {
        try {
            // Convert inboxId from String to UUID
            UUID inboxId = UUID.fromString(INBOX_ID);

            // Create email options and wrap the toEmail in a List
            SendEmailOptions emailOptions = new SendEmailOptions()
                    .to(Collections.singletonList(toEmail))  // Use Collections.singletonList to convert to a list
                    .subject(subject)  // Use the passed subject
                    .body(body);  // Use the passed body

            inboxControllerApi.sendEmail(inboxId, emailOptions);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (com.mailslurp.clients.ApiException e) {
            System.err.println("ApiException occurred. Status code: " + e.getCode());
            System.err.println("Response body: " + e.getResponseBody());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
