package com.example.worldrater.controller;

import com.example.worldrater.dto.AttractionDto;
import com.example.worldrater.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
@Tag(name = "Attractions", description = "Endpoints for managing tourist attractions")
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping
    @Operation(summary = "Get all attractions", description = "Retrieve a list of all registered tourist attractions")
    public List<AttractionDto> getAllAttractions(@RequestParam(required = false) String search) {
        return attractionService.getAllAttractions(search);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attraction by ID", description = "Retrieve a specific attraction by its unique identifier")
    public AttractionDto getAttractionById(@PathVariable Long id) {
        return attractionService.getAttractionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new attraction", description = "Add a new tourist attraction to the database")
    public AttractionDto createAttraction(@Valid @RequestBody AttractionDto attractionDto) {
        return attractionService.createAttraction(attractionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an attraction", description = "Remove an attraction and its associated reviews")
    public void deleteAttraction(@PathVariable Long id) {
        attractionService.deleteAttraction(id);
    }
}
