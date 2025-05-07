package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Service.GeocodingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geocode")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/reverse")
    public ResponseEntity<Map<String, String>> reverseGeocode(@RequestParam double lat, @RequestParam double lng) {
        String address = geocodingService.reverseGeocode(lat, lng);
        if (address != null) {
            return ResponseEntity.ok(Map.of("address", address));
        } else {
            return ResponseEntity.ok(Map.of()); // sau Map.of("address", "")
        }
    }

}

