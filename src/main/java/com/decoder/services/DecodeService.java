package com.decoder.services;

import com.decoder.model.Input;

import java.util.Map;

public interface DecodeService {
    Map<String, Object> decode(Input input);
}
