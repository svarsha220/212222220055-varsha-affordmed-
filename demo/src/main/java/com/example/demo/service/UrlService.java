package com.example.demo.service;

import com.example.demo.entity.ShortUrl;
import com.example.demo.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlService {

    private final ShortUrlRepository repository;

    public UrlService(ShortUrlRepository repository) {
        this.repository = repository;
    }

    public ShortUrl create(String originalUrl, String shortcode) {
        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(originalUrl);
        url.setShortcode(shortcode);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiryAt(LocalDateTime.now().plusMinutes(30));
        return repository.save(url);
    }

    public Optional<ShortUrl> getByShortcode(String code) {
        return repository.findByShortcode(code);
    }
}
