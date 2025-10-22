package com.stageone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stageone.entity.StringEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringResponseDto {
    private String id;
    private String value;
    private StringProperties properties;


    @JsonProperty("created_at")
    private Instant createdAt;

    public static StringResponseDto fromEntity(StringEntity entity) {
        StringResponseDto response = new StringResponseDto();
        response.setId(entity.getId());
        response.setValue(entity.getValue());
        response.setCreatedAt(entity.getCreatedAt());

        StringProperties props = new StringProperties();
        props.setLength(entity.getLength());
        props.setIsPalindrome(entity.getIsPalindrome());
        props.setUniqueCharacters(entity.getUniqueCharacters());
        props.setWordCount(entity.getWordCount());
        props.setSha256Hash(entity.getSha256Hash());
        props.setCharacterFrequencyMap(entity.getCharacterFrequencyMap());

        response.setProperties(props);
        return response;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringProperties getProperties() {
        return properties;
    }

    public void setProperties(StringProperties properties) {
        this.properties = properties;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public static class StringProperties {
        private Integer length;

        @JsonProperty("is_palindrome")
        private Boolean isPalindrome;

        @JsonProperty("unique_characters")
        private Integer uniqueCharacters;

        @JsonProperty("word_count")
        private Integer wordCount;

        @JsonProperty("sha256_hash")
        private String sha256Hash;

        @JsonProperty("character_frequency_map")
        private Map<String, Integer> characterFrequencyMap;

        // Getters and Setters
        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Boolean getIsPalindrome() {
            return isPalindrome;
        }

        public void setIsPalindrome(Boolean isPalindrome) {
            this.isPalindrome = isPalindrome;
        }

        public Integer getUniqueCharacters() {
            return uniqueCharacters;
        }

        public void setUniqueCharacters(Integer uniqueCharacters) {
            this.uniqueCharacters = uniqueCharacters;
        }

        public Integer getWordCount() {
            return wordCount;
        }

        public void setWordCount(Integer wordCount) {
            this.wordCount = wordCount;
        }

        public String getSha256Hash() {
            return sha256Hash;
        }

        public void setSha256Hash(String sha256Hash) {
            this.sha256Hash = sha256Hash;
        }

        public Map<String, Integer> getCharacterFrequencyMap() {
            return characterFrequencyMap;
        }

        public void setCharacterFrequencyMap(Map<String, Integer> characterFrequencyMap) {
            this.characterFrequencyMap = characterFrequencyMap;
        }
    }

}
