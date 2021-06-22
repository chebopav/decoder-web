package com.decoder.service;

import com.decoder.model.template.Template;
import com.decoder.model.template.TemplateBlock;
import com.decoder.model.template.TemplateLine;
import com.decoder.services.PatternCreatorServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

@RunWith(MockitoJUnitRunner.class)
public class PatternCreatorServiceTest {

    @InjectMocks
    private PatternCreatorServiceImpl patternCreatorService;

    @Test
    public void createLineTest() {
        String inputPattern = "NAME AAAA(A)_NNN(N)-UR";
        TemplateLine line = patternCreatorService.createLine(inputPattern);
        Pattern pattern = Pattern.compile(line.getRegEx());

        Assert.assertTrue(line.isUnique());
        Assert.assertTrue(line.isRequired());
        Assert.assertTrue(pattern.matcher("QWER 234").matches());
        Assert.assertTrue(pattern.matcher("QWERT 234").matches());
        Assert.assertTrue(pattern.matcher("QWERT 2345").matches());
    }
    @Test
    public void createBlockTest(){
        LinkedList<String> input = new LinkedList<>(Arrays.asList( "B-BLOCKNAME-UR", "LINENAME1 AAAA(A)_NNN(N)-UR", "B-BLOCKNAME2-U", "LINENAME2 AAAA(A)_NNN(N)-R", "//", "LINENAME3 AAAA(A)_NNN(N)-U",  "//"));
        TemplateBlock res = patternCreatorService.createBlock(input);
        TemplateBlock templateBlock = getExpectedTemplateBlock();

        Assert.assertEquals(res, templateBlock);
    }

    private TemplateBlock getExpectedTemplateBlock(){
        TemplateBlock mainBlock = new TemplateBlock("BLOCKNAME", true, true, null);
        TemplateBlock inlineBlock = new TemplateBlock("BLOCKNAME2", false, true, null);
        LinkedList<Template> mainStricture = new LinkedList<>();
        LinkedList<Template> inlineStructure = new LinkedList<>();
        TemplateLine first = new TemplateLine("LINENAME1", "[A-Z][A-Z][A-Z][A-Z][A-Z]?[\\s][0-9][0-9][0-9][0-9]?", true, true);
        TemplateLine second = new TemplateLine("LINENAME2", "[A-Z][A-Z][A-Z][A-Z][A-Z]?[\\s][0-9][0-9][0-9][0-9]?", true, false);
        TemplateLine third = new TemplateLine("LINENAME3", "[A-Z][A-Z][A-Z][A-Z][A-Z]?[\\s][0-9][0-9][0-9][0-9]?", false, true);
        inlineStructure.add(second);
        inlineBlock.setStructure(inlineStructure);
        mainStricture.add(first);
        mainStricture.add(inlineBlock);
        mainStricture.add(third);
        mainBlock.setStructure(mainStricture);
        return mainBlock;
    }
}
