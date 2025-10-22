package com.stageone;

import com.stageone.entity.StringEntity;
import com.stageone.exception.ResourceAlreadyExistsException;
import com.stageone.exception.ResourceNotFoundException;
import com.stageone.repository.StringRepository;
import com.stageone.service.StringAnalyzerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StringAnalyzerServiceTests {

    @Mock
    private StringRepository repository;

    @InjectMocks
    private StringAnalyzerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Default stub for repository.save() to prevent null returns
        when(repository.save(any(StringEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testAnalyzeAndStore_Success() {
        String value = "hello world";
        when(repository.existsByValue(value)).thenReturn(false);

        StringEntity entity = service.analyzeAndStore(value);

        assertNotNull(entity);
        assertEquals(value, entity.getValue());
        assertEquals(11, entity.getLength());
        assertEquals(2, entity.getWordCount());
        assertFalse(entity.getIsPalindrome());
        assertTrue(entity.getCharacterFrequencyMap().containsKey("h"));
        verify(repository).save(any(StringEntity.class));
    }

    @Test
    void testAnalyzeAndStore_AlreadyExists() {
        String value = "duplicate";
        when(repository.existsByValue(value)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.analyzeAndStore(value));
        verify(repository, never()).save(any());
    }

    @Test
    void testGetByValue_Found() {
        String value = "test";
        StringEntity entity = new StringEntity();
        entity.setValue(value);
        when(repository.findByValue(value)).thenReturn(Optional.of(entity));

        StringEntity result = service.getByValue(value);
        assertEquals(value, result.getValue());
    }

    @Test
    void testGetByValue_NotFound() {
        when(repository.findByValue(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getByValue("not-found"));
    }

    @Test
    void testDeleteByValue_Success() {
        StringEntity entity = new StringEntity();
        entity.setValue("to-delete");
        when(repository.findByValue("to-delete")).thenReturn(Optional.of(entity));

        service.deleteByValue("to-delete");
        verify(repository).delete(entity);
    }

    @Test
    void testDeleteByValue_NotFound() {
        when(repository.findByValue(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteByValue("missing"));
    }

    @Test
    void testCharacterFrequencyMap() {
        when(repository.existsByValue(anyString())).thenReturn(false);

        StringEntity entity = service.analyzeAndStore("aabb");
        assertNotNull(entity);
        assertEquals(2, entity.getCharacterFrequencyMap().get("a"));
        assertEquals(2, entity.getCharacterFrequencyMap().get("b"));
    }

    @Test
    void testSHA256HashConsistency() {
        when(repository.existsByValue(anyString())).thenReturn(false);

        StringEntity e1 = service.analyzeAndStore("abc");
        StringEntity e2 = service.analyzeAndStore("abc");

        assertNotNull(e1);
        assertNotNull(e2);
        assertEquals(e1.getSha256Hash(), e2.getSha256Hash());
    }
}
