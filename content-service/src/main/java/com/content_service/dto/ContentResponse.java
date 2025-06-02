package com.content_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}