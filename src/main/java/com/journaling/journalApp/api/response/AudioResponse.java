package com.journaling.journalApp.api.response;

import lombok.Data;

@Data
public class AudioResponse {
    private String audioUrl; // or whatever field you get in the response
    private String status;   // additional metadata if available
}
