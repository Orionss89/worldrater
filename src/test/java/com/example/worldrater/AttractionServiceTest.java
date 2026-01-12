package com.example.worldrater;

import com.example.worldrater.dto.AttractionDto;
import com.example.worldrater.model.Attraction;
import com.example.worldrater.repository.AttractionRepository;
import com.example.worldrater.service.AttractionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private AttractionService attractionService;

    @Test
    void shouldCreateAttraction() {
        // Given
        AttractionDto dto = new AttractionDto();
        dto.setName("Eiffel Tower");
        dto.setCity("Paris");
        dto.setCountry("France");

        Attraction savedAttraction = new Attraction();
        savedAttraction.setId(1L);
        savedAttraction.setName("Eiffel Tower");
        savedAttraction.setCity("Paris");
        savedAttraction.setCountry("France");

        when(attractionRepository.save(any(Attraction.class))).thenReturn(savedAttraction);

        // When
        AttractionDto result = attractionService.createAttraction(dto);

        // Then
        assertNotNull(result.getId());
        assertEquals("Eiffel Tower", result.getName());
        verify(attractionRepository).save(any(Attraction.class));
    }

    @Test
    void shouldGetAttraction() {
        // Given
        Long id = 1L;
        Attraction attraction = new Attraction();
        attraction.setId(id);
        attraction.setName("Grand Canyon");

        when(attractionRepository.findById(id)).thenReturn(Optional.of(attraction));

        // When
        AttractionDto result = attractionService.getAttractionById(id);

        // Then
        assertEquals("Grand Canyon", result.getName());
    }
}
