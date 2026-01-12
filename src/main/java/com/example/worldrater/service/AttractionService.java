package com.example.worldrater.service;

import com.example.worldrater.dto.AttractionDto;
import com.example.worldrater.model.Attraction;
import com.example.worldrater.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;

    public List<AttractionDto> getAllAttractions(String search) {
        List<Attraction> attractions;
        if (search != null && !search.trim().isEmpty()) {
            attractions = attractionRepository
                    .findByNameContainingIgnoreCaseOrCityContainingIgnoreCaseOrCategoryContainingIgnoreCase(search,
                            search, search);
        } else {
            attractions = attractionRepository.findAll();
        }

        return attractions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AttractionDto createAttraction(AttractionDto dto) {
        Attraction attraction = mapToEntity(dto);
        Attraction saved = attractionRepository.save(attraction);
        return mapToDto(saved);
    }

    public AttractionDto getAttractionById(Long id) {
        return attractionRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new com.example.worldrater.exception.ResourceNotFoundException(
                        "Attraction not found with id: " + id));
    }

    public void deleteAttraction(Long id) {
        if (!attractionRepository.existsById(id)) {
            throw new com.example.worldrater.exception.ResourceNotFoundException("Attraction not found with id: " + id);
        }
        attractionRepository.deleteById(id);
    }

    // Simple mapper methods (could use MapStruct)
    private AttractionDto mapToDto(Attraction attr) {
        AttractionDto dto = new AttractionDto();
        dto.setId(attr.getId());
        dto.setName(attr.getName());
        dto.setDescription(attr.getDescription());
        dto.setCity(attr.getCity());
        dto.setCountry(attr.getCountry());
        dto.setCategory(attr.getCategory());
        return dto;
    }

    private Attraction mapToEntity(AttractionDto dto) {
        Attraction attr = new Attraction();
        attr.setName(dto.getName());
        attr.setDescription(dto.getDescription());
        attr.setCity(dto.getCity());
        attr.setCountry(dto.getCountry());
        attr.setCategory(dto.getCategory());
        return attr;
    }
}
