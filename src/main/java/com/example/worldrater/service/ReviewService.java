package com.example.worldrater.service;

import com.example.worldrater.dto.ReviewDto;
import com.example.worldrater.model.Attraction;
import com.example.worldrater.model.Review;
import com.example.worldrater.repository.AttractionRepository;
import com.example.worldrater.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AttractionRepository attractionRepository;

    public ReviewDto addReview(Long attractionId, ReviewDto dto) {
        Attraction attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> new com.example.worldrater.exception.ResourceNotFoundException(
                        "Attraction not found with id: " + attractionId));

        Review review = new Review();
        review.setAttraction(attraction);
        review.setAuthorName(dto.getAuthorName());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review saved = reviewRepository.save(review);
        return mapToDto(saved);
    }

    public List<ReviewDto> getReviewsForAttraction(Long attractionId) {
        return reviewRepository.findByAttractionId(attractionId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setAttractionId(review.getAttraction().getId());
        dto.setAuthorName(review.getAuthorName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
