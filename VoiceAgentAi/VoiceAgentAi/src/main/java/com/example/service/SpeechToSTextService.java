package com.example.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SpeechToSTextService {

    private final TransactionModel transactionModel;

    public SpeechToSTextService(TransactionModel transactionModel){
        this.transactionModel=transactionModel;
    }

    public String transcribe(Resource audio){
        return transactionModel.transcribe(audio);
    }
}