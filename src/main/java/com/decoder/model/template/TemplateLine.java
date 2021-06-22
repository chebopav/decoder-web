package com.decoder.model.template;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TemplateLine implements Template {
    private String name;
    private String regEx;
    private boolean required;
    private boolean unique;
}
