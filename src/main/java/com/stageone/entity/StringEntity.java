package com.stageone.entity;

import com.stageone.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "analyzed_strings")
@Data
@AllArgsConstructor
public class StringEntity {
    @Id
    private String id; // SHA-256 hash

    @Column(name = "string_value", nullable = false, unique = true, columnDefinition = "TEXT")
    private String value;

    @Column(nullable = false)
    private Integer length;

    @Column(nullable = false)
    private Boolean isPalindrome;

    @Column(nullable = false)
    private Integer uniqueCharacters;

    @Column(nullable = false)
    private Integer wordCount;

    @Column(nullable = false)
    private String sha256Hash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "character_frequencies", joinColumns = @JoinColumn(name = "string_id"))
    @MapKeyColumn(name = "character")
    @Column(name = "frequency")
    private Map<String, Integer> characterFrequencyMap = new HashMap<>();

    @Column(nullable = false)
    private Instant createdAt;

    public StringEntity() {
        this.createdAt = Instant.now();
    }
}
