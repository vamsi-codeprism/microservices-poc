package com.content_service.service;

import com.content_service.config.UserClient;
import com.content_service.model.Content;
import com.content_service.repository.ContentRepository;
import com.content_service.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserClient userClient;

    @Override
    public Content createContent(Content content) {
        // Validate if the provided userId exists via UserService
        boolean userExists = userClient.isUserExists(content.getUserId());
        if (!userExists) {
            throw new RuntimeException("User with ID " + content.getUserId() + " does not exist");
        }
        return contentRepository.save(content);
    }

    @Override
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }

    @Override
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    @Override
    public List<Content> getContentByUserId(Long userId) {
        return contentRepository.findByUserId(userId);
    }

    @Override
    public Content updateContent(Long id, Content content) {
        // Check if the content with the given ID exists
        Content existing = contentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Content not found with ID: " + id));
        // Validate if the provided userId exists via UserService
        boolean userExists = userClient.isUserExists(content.getUserId());
        if (!userExists) {
            throw new RuntimeException("User with ID " + content.getUserId() + " does not exist");
        }
        // Update the content fields
        existing.setTitle(content.getTitle());
        existing.setDescription(content.getDescription());
        existing.setUserId(content.getUserId());

        return contentRepository.save(existing);
    }


    @Override
    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }
}
