package com.mapping.task.controller;

import com.mapping.task.service.MappingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
class ControllerTest {

    private static final String URL = "/map";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MappingService mappingService;

    @Test
    void mapValuesShouldPass() throws Exception {
        //given
        String expectedResults = "{\"1\":[\"Lion\"],\"2\":[\"Lion\",\"Elephant\"],\"4\":[\"Lion\",\"Elephant\",\"Giraffe\"]}";
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenReturn(prepareSampleResult());
        mockMvc.perform(
                        get(URL)
                                .param("category", "Animals")
                                .param("numberLists", "1,2,4")
                ).andExpect(status().isOk())
                .andExpect(content().string(expectedResults));
    }

    @Test
    void mapValuesWithIncorrectCategoryShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "notExist")
                        .param("numberLists", "1,2,4")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void mapValuesWithBlankCategoryShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "")
                        .param("numberLists", "1,2,4")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void mapValuesWithBlankNumbersShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "Animals")
                        .param("numberLists", "")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void mapValuesWithStringParsingAsNumbersShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(NumberFormatException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "Animals")
                        .param("numberLists", "1,2,String")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void mapValuesWithNumbersGreaterThan20ShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "Animals")
                        .param("numberLists", "21")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void mapValuesWithNumbersLowerThan1ShouldReturn400() throws Exception {
        //given
        //when
        when(mappingService.mapValues(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(
                get(URL)
                        .param("category", "Animals")
                        .param("numberLists", "-5")
        ).andExpect(status().isBadRequest());
    }

    private Map<Integer, List<String>> prepareSampleResult() {
        Map<Integer, List<String>> sampleResult = new LinkedHashMap<>();
        sampleResult.put(1, List.of("Lion"));
        sampleResult.put(2, List.of("Lion", "Elephant"));
        sampleResult.put(4, List.of("Lion", "Elephant", "Giraffe"));
        return sampleResult;
    }
}