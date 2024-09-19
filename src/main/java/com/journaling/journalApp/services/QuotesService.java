package com.journaling.journalApp.services;

import com.journaling.journalApp.api.response.QuotesResponse;
import com.journaling.journalApp.api.response.WeatherResponse;
import com.journaling.journalApp.cache.AppCache;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuotesService {

    @Value("${quotes-API-Key}") public String apiKey ;

    @Autowired      private RestTemplate restTemplate;
    @Autowired      private AppCache appCache;


    public QuotesResponse getQuotes(String category){
        String finalAPI = appCache.APP_CACHE.get("quotes_api_url").replace("<category>", category);

        // Set headers to include the API key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey); // Sending API key in headers

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<QuotesResponse[]> response = restTemplate.exchange(finalAPI, HttpMethod.GET, entity, QuotesResponse[].class);
        QuotesResponse[] quotes = response.getBody();
        return quotes != null && quotes.length > 0 ? quotes[0] : null;
    }

}
