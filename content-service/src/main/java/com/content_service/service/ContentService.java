package com.content_service.service;

import com.content_service.model.Content;

import java.util.List;
import java.util.Optional;

public interface ContentService {
    Content createContent(Content content);
    List<Content> getAllContent();
    Optional<Content> getContentById(Long id);
    List<Content> getContentByUserId(Long userId);
    Content updateContent(Long id, Content content);
    void deleteContent(Long id);
}
