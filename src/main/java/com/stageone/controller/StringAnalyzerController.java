package com.stageone.controller;


import com.stageone.dto.*;
import com.stageone.entity.StringEntity;
import com.stageone.service.StringAnalyzerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strings")
public class StringAnalyzerController {

    private final StringAnalyzerService service;

    public StringAnalyzerController(StringAnalyzerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StringResponseDto> createString(@Valid @RequestBody StringRequestDto request) {
        StringEntity entity = service.analyzeAndStore(request.getValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(StringResponseDto.fromEntity(entity));
    }

    @GetMapping("/{value}")
    public ResponseEntity<StringResponseDto> getString(@PathVariable String value) {
        StringEntity entity = service.getByValue(value);
        return ResponseEntity.ok(StringResponseDto.fromEntity(entity));
    }

    @GetMapping
    public ResponseEntity<StringListResponseDto> getAllStrings(
            @RequestParam(required = false) Boolean is_palindrome,
            @RequestParam(required = false) Integer min_length,
            @RequestParam(required = false) Integer max_length,
            @RequestParam(required = false) Integer word_count,
            @RequestParam(required = false) String contains_character) {

        StringFilterParamsDto params = new StringFilterParamsDto();
        params.setIsPalindrome(is_palindrome);
        params.setMinLength(min_length);
        params.setMaxLength(max_length);
        params.setWordCount(word_count);
        params.setContainsCharacter(contains_character);

        List<StringEntity> entities = service.getAllWithFilters(params);

        StringListResponseDto response = new StringListResponseDto();
        response.setData(entities.stream().map(StringResponseDto::fromEntity).toList());
        response.setCount(entities.size());
        response.setFiltersApplied(params);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter-by-natural-language")
    public ResponseEntity<NaturalLanguageResponseDto> filterByNaturalLanguage(@RequestParam String query) {
        NaturalLanguageQueryDto parsedQuery = service.parseNaturalLanguageQuery(query);
        List<StringEntity> entities = service.filterByNaturalLanguage(query);

        NaturalLanguageResponseDto response = new NaturalLanguageResponseDto();
        response.setData(entities.stream().map(StringResponseDto::fromEntity).toList());
        response.setCount(entities.size());
        response.setInterpretedQuery(parsedQuery);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{value}")
    public ResponseEntity<Void> deleteString(@PathVariable String value) {
        service.deleteByValue(value);
        return ResponseEntity.noContent().build();
    }
}