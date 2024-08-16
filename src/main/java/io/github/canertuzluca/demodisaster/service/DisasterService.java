package io.github.canertuzluca.demodisaster.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.canertuzluca.demodisaster.ext.call.NewsApiClientImpl;
import io.github.canertuzluca.demodisaster.models.Disaster;
import io.github.canertuzluca.demodisaster.models.Location;
import io.github.canertuzluca.demodisaster.models.News;
import io.github.canertuzluca.demodisaster.models.NewsResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisasterService {

    private final NewsApiClientImpl newsApiService;
    private final OpenAiChatModel openAiChatModel;

    @Autowired
    public DisasterService(NewsApiClientImpl newsApiService, OpenAiChatModel openAiChatModel) {
        this.newsApiService = newsApiService;
        this.openAiChatModel = openAiChatModel;
    }



    public List<Object> disasterAnalyze() {
        NewsResponse newsResponse = newsApiService.getAllNews();
        List<News> newsList = newsResponse.getNews();
        long totalCount = newsResponse.getTotalCount();

        Disaster disaster = new Disaster();

        List<Object> disasterList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();


        for (News newsItem : newsList) {
            String content = newsItem.getContent();

            Prompt prompt = new Prompt(String.format("""
                    %s Bu metni analiz et ve bu metinde bir afet var mı varsa türü nedir? Nerede olduysa o bölgenin kordinatlarını bana getir.
                    Getireceğin format sadece bu şekilde olmalı {
                                                             "disasterType": "string",
                                                             "locations": [
                                                             {
                                                             "cityName": "string",
                                                             "longitude": "double",
                                                             "latitude": "double"
                                                             }
                                                             ]
                                                            }""", content));
            ChatResponse chatResponse = openAiChatModel.call(prompt);
            try {
                disaster = objectMapper.readValue(chatResponse.getResult().getOutput().getContent(), Disaster.class);
                for (Location location : disaster.getDisasterLocation()) {
                    location.setTitle(newsItem.getTitle());
                }
                disasterList.add(disaster);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Long> countMap = new HashMap<>();
        countMap.put("totalCount", totalCount);
        disasterList.add(countMap);
        return disasterList;
    }



    public NewsResponse getAllNews(){
        return newsApiService.getAllNews();
    }
}