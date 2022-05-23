package com.example.arifaservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateTemplateMsgInterRequest {

    @JsonProperty
    private String language;

    @JsonProperty
    private List<String> type;

    @JsonProperty
    private Map<String, String> parameterMap;
}