package com.decoder.controllers;

import com.decoder.model.Input;
import com.decoder.services.DecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/")
public class InputController {
    private final DecodeService decodeService;

    @Autowired
    public InputController(DecodeService decodeService) {
        this.decodeService = decodeService;
    }

    @PostMapping("/input")
    public Map<String, Object> decode(@RequestBody Input input){
        return decodeService.decode(input);
    }
}
