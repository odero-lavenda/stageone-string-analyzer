package com.stageone.service;

import com.stageone.dto.NaturalLanguageQueryDto;
import com.stageone.dto.StringFilterParamsDto;
import com.stageone.entity.StringEntity;
import com.stageone.exception.ResourceAlreadyExistsException;
import com.stageone.exception.ResourceNotFoundException;
import com.stageone.repository.StringRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringAnalyzerService {

    private final StringRepository repository;

    public StringAnalyzerService(StringRepository repo) {
        this.repository = repo;
    }
    public StringEntity analyzeAndStore(String value) {
        if (repository.existsByValue(value)) {
            throw new ResourceAlreadyExistsException("String already exists in the system");
        }

        StringEntity entity = new StringEntity();
        entity.setValue(value);

        entity.setLength(value.length());
        entity.setIsPalindrome(isPalindrome(value));
        entity.setUniqueCharacters(countUniqueCharacters(value));
        entity.setWordCount(countWords(value));

        String hash = computeSHA256(value);
        entity.setSha256Hash(hash);
        entity.setId(hash);

        entity.setCharacterFrequencyMap(computeCharacterFrequency(value));

        return repository.save(entity);
    }

    public StringEntity getByValue(String value) {
        return repository.findByValue(value)
                .orElseThrow(() -> new ResourceNotFoundException("String does not exist in the system"));
    }

    public List<StringEntity> getAllWithFilters(StringFilterParamsDto params) {
        Specification<StringEntity> spec = buildSpecification(params);
        return repository.findAll(spec);
    }

    public void deleteByValue(String value) {
        StringEntity entity = getByValue(value);
        repository.delete(entity);
    }

    public List<StringEntity> filterByNaturalLanguage(String query) {
        NaturalLanguageQueryDto parsedQuery = parseNaturalLanguageQuery(query);
        Specification<StringEntity> spec = buildSpecification(parsedQuery.getFilters());
        return repository.findAll(spec);
    }

    public NaturalLanguageQueryDto parseNaturalLanguageQuery(String query) {
        StringFilterParamsDto filters = new StringFilterParamsDto();
        String lowerQuery = query.toLowerCase();

        // Check for palindrome
        if (lowerQuery.contains("palindrome") || lowerQuery.contains("palindromic")) {
            filters.setIsPalindrome(true);
        }

        // Check for single word
        if (lowerQuery.contains("single word")) {
            filters.setWordCount(1);
        }

        // Extract length constraints
        Pattern longerThanPattern = Pattern.compile("longer than (\\d+)");
        Matcher longerThanMatcher = longerThanPattern.matcher(lowerQuery);
        if (longerThanMatcher.find()) {
            filters.setMinLength(Integer.parseInt(longerThanMatcher.group(1)) + 1);
        }

        Pattern shorterThanPattern = Pattern.compile("shorter than (\\d+)");
        Matcher shorterThanMatcher = shorterThanPattern.matcher(lowerQuery);
        if (shorterThanMatcher.find()) {
            filters.setMaxLength(Integer.parseInt(shorterThanMatcher.group(1)) - 1);
        }

        // Extract contains character
        Pattern containsLetterPattern = Pattern.compile("contain(?:s|ing)? (?:the )?letter ([a-z])");
        Matcher containsLetterMatcher = containsLetterPattern.matcher(lowerQuery);
        if (containsLetterMatcher.find()) {
            filters.setContainsCharacter(containsLetterMatcher.group(1));
        }

        // Check for first vowel (a)
        if (lowerQuery.contains("first vowel")) {
            filters.setContainsCharacter("a");
        }

        NaturalLanguageQueryDto result = new NaturalLanguageQueryDto();
        result.setOriginalQuery(query);
        result.setFilters(filters);

        return result;
    }

    private Specification<StringEntity> buildSpecification(StringFilterParamsDto params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getIsPalindrome() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPalindrome"), params.getIsPalindrome()));
            }

            if (params.getMinLength() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("length"), params.getMinLength()));
            }

            if (params.getMaxLength() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("length"), params.getMaxLength()));
            }

            if (params.getWordCount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("wordCount"), params.getWordCount()));
            }

            if (params.getContainsCharacter() != null && !params.getContainsCharacter().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("value")),
                        "%" + params.getContainsCharacter().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean isPalindrome(String str) {
        String cleaned = str.toLowerCase().replaceAll("[^a-z0-9]", "");
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    private int countUniqueCharacters(String str) {
        return (int) str.chars().distinct().count();
    }

    private int countWords(String str) {
        if (str.trim().isEmpty()) {
            return 0;
        }
        return str.trim().split("\\s+").length;
    }

    private String computeSHA256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private Map<String, Integer> computeCharacterFrequency(String str) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (char c : str.toCharArray()) {
            String key = String.valueOf(c);
            frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
        }
        return frequencyMap;
    }
}
