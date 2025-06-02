package com.content_service.controller;

import com.content_service.dto.ContentResponse;
import com.content_service.model.Content;
import com.content_service.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentRepository contentRepository;

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContent() {
        List<ContentResponse> contents = contentRepository.findAll().stream()
                .map(content -> new ContentResponse(content.getId(), content.getTitle(), content.getDescription(), content.getUserId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        return contentRepository.findById(id)
                .map(content -> ResponseEntity.ok(new ContentResponse(content.getId(), content.getTitle(), content.getDescription(), content.getUserId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContentResponse>> getContentByUserId(@PathVariable Long userId) {
        List<ContentResponse> contents = contentRepository.findByUserId(userId).stream()
                .map(content -> new ContentResponse(content.getId(), content.getTitle(), content.getDescription(), content.getUserId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(contents);
    }

    @PostMapping
    public ResponseEntity<ContentResponse> createContent(@RequestBody Content content) {
        Content savedContent = contentRepository.save(content);
        return ResponseEntity.ok(new ContentResponse(savedContent.getId(), savedContent.getTitle(), savedContent.getDescription(), savedContent.getUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id, @RequestBody Content content) {
        return contentRepository.findById(id)
                .map(existingContent -> {
                    existingContent.setTitle(content.getTitle());
                    existingContent.setDescription(content.getDescription());
                    existingContent.setUserId(content.getUserId());
                    Content updatedContent = contentRepository.save(existingContent);
                    return ResponseEntity.ok(new ContentResponse(updatedContent.getId(), updatedContent.getTitle(), updatedContent.getDescription(), updatedContent.getUserId()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        if (contentRepository.existsById(id)) {
            contentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}