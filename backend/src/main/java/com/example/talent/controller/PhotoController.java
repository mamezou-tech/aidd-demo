package com.example.talent.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class PhotoController {

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhoto(@PathVariable String id) {
        Resource photo = new ClassPathResource("static/photos/" + id + ".jpg");
        if (!photo.exists()) {
            photo = new ClassPathResource("static/photos/default.png");
        }
        return ResponseEntity.ok()
                .contentType(photo.getFilename().endsWith(".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(photo);
    }
}
