package com.decoder.services;

import com.decoder.model.template.Template;

import java.util.LinkedList;

public interface PatternCreatorService {
    LinkedList<Template> getTemplate(String text);
}
