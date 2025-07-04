package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.Theme;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "uid", nullable = false)
    private User createdBy;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Theme theme = Theme.DEFAULT;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
