package com.decoder.model.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TemplateBlock implements Template {
    private String name;
    private boolean required;
    private boolean unique;
    private LinkedList<Template> structure;

}
