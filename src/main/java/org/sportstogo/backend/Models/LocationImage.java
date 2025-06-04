package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(optional = false)
    @JoinColumn(name = "image_id")
    private Image image;
}
