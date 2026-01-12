package com.example.worldrater.controller;

import com.example.worldrater.dto.ReviewDto;
import com.example.worldrater.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions/{attractionId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for managing reviews of attractions")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get reviews for an attraction", description = "Retrieve all reviews associated with a specific attraction")
    public List<ReviewDto> getReviews(@PathVariable Long attractionId) {
        return reviewService.getReviewsForAttraction(attractionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a review", description = "Submit a new rating and comment for an attraction")
    public ReviewDto addReview(@PathVariable Long attractionId, @Valid @RequestBody ReviewDto reviewDto) {
        return reviewService.addReview(attractionId, reviewDto);
    }
}
