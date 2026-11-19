package com.example.service;

import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.dto.VoiceResponse;

@Service
public class VoiceAgentService {
private final SpeechToSTextService speechToTextService;
private final OrderAgentService orderAgentService;
private TextToSpeechService textTechSpeechService = null;


public VoiceAgentService(SpeechToSTextService speechToTextService ,
                         OrderAgentService orderAgentService,
                         TextToSpeechService textT){

                  this.speechToTextService =speechToTextService;
                  this.orderAgentService=orderAgentService;
                  this.textTechSpeechService=textTechSpeechService;          
                         }


            public VoiceResponse process(Resource audio){
                String transcript=speechToTextService.transcribe(audio);
                String response =orderAgentService.ask(transcript);
                byte[] audioResponse =textTechSpeechService.generateSpeech(response);
                String base64audio =Base64.getEncoder() .encodeToString(audioResponse);
                return new VoiceResponse (transcript, response,base64audio);
            }
}
