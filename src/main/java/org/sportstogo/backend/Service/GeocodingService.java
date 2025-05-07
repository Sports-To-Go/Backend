package org.sportstogo.backend.Service;

import com.google.api.client.util.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {
    @Value("${google.api.key}")
    private String apiKey;

    public String reverseGeocode(double lat, double lng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List results = (List) response.getBody().get("results");
            if (!results.isEmpty()) {
                Map firstResult = (Map) results.get(0);
                return (String) firstResult.get("formatted_address");
            }
        }

        throw new RuntimeException("Could not reverse geocode");
    }
}