package com.decoder.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;

@Service
public class TextServiceImpl implements TextService{
    @Override
    public LinkedList<String> getLines(String text) {
        String modifiedText = text.trim().toUpperCase();
        return new LinkedList<>(Arrays.asList(modifiedText.split("\n")));
    }
}
