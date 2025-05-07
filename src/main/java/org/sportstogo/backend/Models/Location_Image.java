package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Location_Images")
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class Location_Image {
    /**
     * Unique identifier for the image. Auto-generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * url of the image. cannot be null
     */
    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
}
