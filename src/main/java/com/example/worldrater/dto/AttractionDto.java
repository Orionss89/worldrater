package com.example.worldrater.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttractionDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    private String category;
}
