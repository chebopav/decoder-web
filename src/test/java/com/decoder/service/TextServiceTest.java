package com.decoder.service;

import com.decoder.services.TextServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;

@RunWith(MockitoJUnitRunner.class)
public class TextServiceTest {
    @InjectMocks
    private TextServiceImpl textService;

    @Test
    public void getLinesTest(){
        String inputString = "SSM\nUTC\n20APR26444E001\nRPL\nY7919\n01JUN21 28SEP21 2\nJ 738 .C6Y174\nNSK0845 UFA1210\nUFA1340 AAQ1650\nNSKUFA 98/1\nUFAAAQ 99/1\n//\nRPL\nY7920\n01JUN21 28SEP21 2\nJ 738 .C6Y174\nAAQ1930 UFA2215\nUFA2345 NSK0310/1\nAAQUFA 98/1\nUFANSK 99/1\n//\nSI AUTOFEED 326444";
        LinkedList<String> lines = textService.getLines(inputString);
        Assert.assertEquals(22, lines.size());
    }
}
