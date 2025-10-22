package com.stageone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stageone.dto.StringRequestDto;
import com.stageone.repository.StringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StringAnalyzerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StringRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testCreateString_Success() throws Exception {
        StringRequestDto req = new StringRequestDto("hello world");

        mockMvc.perform(post("/strings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value", is("hello world")))
                .andExpect(jsonPath("$.properties.length", is(11)))
                .andExpect(jsonPath("$.properties.word_count", is(2)));
    }

    @Test
    void testGetString_Success() throws Exception {
        mockMvc.perform(post("/strings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\":\"madam\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/strings/madam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.properties.is_palindrome", is(true)));
    }

    @Test
    void testGetAllStrings_FilterByPalindrome() throws Exception {
        mockMvc.perform(post("/strings").contentType(MediaType.APPLICATION_JSON).content("{\"value\":\"madam\"}"));
        mockMvc.perform(post("/strings").contentType(MediaType.APPLICATION_JSON).content("{\"value\":\"hello\"}"));

        mockMvc.perform(get("/strings?is_palindrome=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].value", is("madam")));
    }

    @Test
    void testFilterByNaturalLanguage() throws Exception {
        mockMvc.perform(post("/strings").contentType(MediaType.APPLICATION_JSON).content("{\"value\":\"madam\"}"));

        mockMvc.perform(get("/strings/filter-by-natural-language")
                        .param("query", "all single word palindromic strings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.interpreted_query.original", is("all single word palindromic strings")));
    }

    @Test
    void testDeleteString_Success() throws Exception {
        mockMvc.perform(post("/strings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\":\"delete me\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/strings/delete me"))
                .andExpect(status().isNoContent());
    }
}
