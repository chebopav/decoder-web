package com.decoder.services;

import com.decoder.model.template.Template;
import com.decoder.model.template.TemplateBlock;
import com.decoder.model.template.TemplateLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class PatternCreatorServiceImpl implements PatternCreatorService{

    private static final String UNIQUE = "U";
    private static final String REQUIRED = "R";
    private static final String BLOCK_START = "B-";
    private static final String END = "//";
    private static final String SYMBOL_REGEX = "[A-Z]";
    private static final String NUMERIC_REGEX = "[0-9]";
    private static final String SPACE_REGEX = "[\\s]";

    private final TextService textService;

    @Autowired
    public PatternCreatorServiceImpl(TextService textService) {
        this.textService = textService;
    }

    @Override
    public LinkedList<Template> getTemplate(String text) {
        LinkedList<Template> resultList = new LinkedList<>();
        LinkedList<String> lines = textService.getLines(text);
        resultList.add(createBlock(lines));
        return resultList;
    }

    public TemplateBlock createBlock(LinkedList<String> lines){
        TemplateBlock resultBlock = new TemplateBlock();
        LinkedList<Template> structure = new LinkedList<>();
        String blockName = "";
        String l = lines.getFirst();
        if (l.lastIndexOf("-") != -1){
            String config = l.substring(l.lastIndexOf("-") + 1);
            if (config.contains(REQUIRED)){
                resultBlock.setRequired(true);
            }
            if (config.contains(UNIQUE)){
                resultBlock.setUnique(true);
            }
            l = l.substring(2, l.lastIndexOf("-")).trim();
        }
        blockName = l;
        resultBlock.setName(blockName);
        lines.removeFirst();
        while (lines.size() > 0) {
            String line = lines.getFirst();
            if (line.startsWith(BLOCK_START)){
                structure.add(createBlock(lines));
                continue;
            }
            if (line.startsWith("//")){
                resultBlock.setStructure(structure);
                lines.removeFirst();
                return resultBlock;
            }
            structure.add(createLine(line));
            lines.remove(line);
        }
        resultBlock.setStructure(structure);
        return resultBlock;
    }

    public TemplateLine createLine(String line){
        TemplateLine resultLine = new TemplateLine();
        String lineName = line.substring(0, line.indexOf(" "));
        resultLine.setName(lineName);
        line = line.substring(line.indexOf(" ") + 1);
        if (line.lastIndexOf("-") != -1){
            String config = line.substring(line.lastIndexOf("-") + 1);
            if (config.contains(REQUIRED)){
                resultLine.setRequired(true);
            }
            if (config.contains(UNIQUE)){
                resultLine.setUnique(true);
            }
            line = line.substring(0, line.lastIndexOf("-")).trim();
        }
        line = line.trim();
        char[] chars = line.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean requiredCharBlock = true;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '('){
                requiredCharBlock = false;
                continue;
            }
            if (chars[i] == ')'){
                requiredCharBlock = true;
                continue;
            }
            sb.append(getSymbolOrNumberRegex(chars[i]));
            if (i != (chars.length - 1) && chars[i+1] == '*'){
                sb.append('*');
                i++;
                continue;
            }
            if (!requiredCharBlock){
                sb.append('?');
            }
        }
        resultLine.setRegEx(sb.toString());
        return resultLine;
    }

    private String getSymbolOrNumberRegex(char ch){
        switch (ch){
            case 'N' :
                return NUMERIC_REGEX;
            case 'A' :
                return SYMBOL_REGEX;
            case '_' :
                return SPACE_REGEX;
            default:
                return "[" + ch + "]";
        }
    }

}
