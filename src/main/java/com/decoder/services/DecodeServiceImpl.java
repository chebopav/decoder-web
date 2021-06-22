package com.decoder.services;

import com.decoder.exception.DecoderException;
import com.decoder.model.Input;
import com.decoder.model.template.Template;
import com.decoder.model.template.TemplateBlock;
import com.decoder.model.template.TemplateLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Objects.isNull;

@Service
public class DecodeServiceImpl implements DecodeService{
    private final PatternCreatorService patternCreatorService;
    private final TextService textService;

    @Autowired
    public DecodeServiceImpl(PatternCreatorService patternCreatorService, TextService textService) {
        this.patternCreatorService = patternCreatorService;
        this.textService = textService;
    }

    @Override
    public Map<String, Object> decode(Input input) {
        LinkedList<Template> structure = patternCreatorService.getTemplate(input.getPattern());
        LinkedList<String> lines = textService.getLines(input.getInputText());
        return decodeByStructure(lines, structure);
    }

    public final LinkedHashMap<String, Object> decodeByStructure(LinkedList<String> lines, LinkedList<Template> str) {
        LinkedList<Template> structure = (LinkedList<Template>) str.clone();
        LinkedHashMap<String, Object> res = new LinkedHashMap<>();
        boolean isPreviousUnique = true;

        while (true) {
            if (lines.isEmpty()) {
                return res;
            }
            Template template = structure.getFirst();
            String line = lines.getFirst();
            if (line.equals("//")) {
                lines.removeFirst();
                return res;
            }
            if (template instanceof TemplateBlock) {
                if (line.equals(template.getName())) {
                    lines.removeFirst();
                    if (res.containsKey(line)) {
                        line = incrementName(line);
                    }
                    res.put(line, decodeByStructure(lines, ((TemplateBlock) template).getStructure()));
                } else {
                    structure.removeFirst();
                    continue;
                }
            }

            if (template instanceof TemplateLine) {
                Map.Entry<String, Object> resultList = decodeLine((TemplateLine) template, line);
                if (isNull(resultList)) {
                    if (template.isRequired() && isPreviousUnique) {
                        throw new DecoderException("Required line " + template.getName() + " is empty or incorrect " + line + " for template " + template);
                    } else {
                        structure.removeFirst();
                    }
                } else {
                    mergeLines(res, resultList);
                    lines.removeFirst();
                    if (template.isUnique()) {
                        isPreviousUnique = true;
                        structure.removeFirst();
                    } else {
                        isPreviousUnique = false;
                    }
                }
            }
        }
    }

    private Map.Entry<String, Object> decodeLine(TemplateLine lineTemplate, String line){
        Map<String, Object> resultMap = new HashMap<>(1);
        if (!(line.matches(lineTemplate.getRegEx()))){
            return null;
        } else {
            resultMap.put(lineTemplate.getName(), line);
        }
        return resultMap.entrySet().stream().findFirst().orElseThrow(() -> new DecoderException("Unable to read line"));
    }

    private void mergeLines(LinkedHashMap<String, Object> mainMap, Map.Entry<String, Object> entry) {
        if (mainMap.containsKey(entry.getKey())) {
            mainMap.put(incrementName(entry.getKey()), entry.getValue());
        } else {
            mainMap.put(entry.getKey(), entry.getValue());
        }
    }

    private String incrementName(String str){
        if (str.matches("[a-zA-Z]*")) {
            return str + "2";
        }
        String keyWord = str.replaceAll("[0-9]", "");
        int number = Integer.parseInt(str.replaceAll("[a-zA-Z]", ""));
        return keyWord + ++number;
    }
}
