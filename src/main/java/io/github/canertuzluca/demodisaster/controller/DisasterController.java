package io.github.canertuzluca.demodisaster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.canertuzluca.demodisaster.models.Disaster;
import io.github.canertuzluca.demodisaster.models.NewsResponse;
import io.github.canertuzluca.demodisaster.service.DisasterMonitoringService;
import io.github.canertuzluca.demodisaster.service.DisasterService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class DisasterController {
    private final DisasterService disasterService;

    private final OpenAiChatModel openAiChatModel;

    private final DisasterMonitoringService disasterMonitoringService;



    @Autowired
    public DisasterController(DisasterService disasterService, OpenAiChatModel openAiChatModel, DisasterMonitoringService disasterMonitoringService) {

        this.disasterService = disasterService;
        this.openAiChatModel = openAiChatModel;
        this.disasterMonitoringService = disasterMonitoringService;
    }

    @GetMapping("/openai")
    public Disaster chat(){
        ObjectMapper objectMapper = new ObjectMapper();
        Disaster disaster = new Disaster();

        Prompt prompt = new Prompt("""
                {Ege Denizinde 4.3 Deprem} Bu metni analiz et ve bu metinde bir afet var mı varsa türü nedir? Nerede olduysa o bölgenin kordinatlarını bana getir. 
                Getireceğin format sadece bu şekilde olmalı {
                                                             "disasterType": "string",
                                                             "locations": [
                                                             {
                                                             "cityName": "string",
                                                             "longitude": "double",
                                                             "latitude": "double"
                                                             }
                                                             ]
                                                            }""");

        ChatResponse chatResponse = openAiChatModel.call(prompt);

        try {
            disaster = objectMapper.readValue(chatResponse.getResult().getOutput().getContent(),Disaster.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  disaster;
    }


    @GetMapping
    public ResponseEntity<NewsResponse> getAllNews(){
        NewsResponse disasterEntity = disasterService.getAllNews();
        return ResponseEntity.ok(disasterEntity);
    }


    @GetMapping("/test")
    public ResponseEntity<List<Object>> getAllNews2(){
        return ResponseEntity.ok(disasterService.disasterAnalyze());
    }

    @GetMapping("/timer")
    public ResponseEntity<List<Object>> getAllNews3() {
        List<Object> disasters = disasterMonitoringService.getLatestDisasters();
        return ResponseEntity.ok(disasters);
    }

}

