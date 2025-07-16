package com.example.demo.controller;

import com.example.demo.entity.ShortUrl;
import com.example.demo.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorturl")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, Object> body) {
        try {
            String url = (String) body.get("url");
            Integer validity = body.get("validity") != null ? (Integer) body.get("validity") : null;
            String shortcode = (String) body.get("shortcode");

            ShortUrl su = urlService.createShortUrl(url, validity, shortcode);

            Map<String, String> response = new HashMap<>();
            response.put("shortLink", "http://localhost:8080/" + su.getShortcode());
            response.put("expiry", su.getExpiry().toString());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortcode, HttpServletRequest req) {
        try {
            String referrer = req.getHeader("referer");
            ShortUrl su = urlService.getAndLogRedirection(shortcode, referrer);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(su.getOriginalUrl()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 redirect
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()),
                    e.getMessage().equals("Link expired") ? HttpStatus.GONE : HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/shorturls/{shortcode}")
    public ResponseEntity<?> getStats(@PathVariable String shortcode) {
        try {
            return ResponseEntity.ok(urlService.getStats(shortcode));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
