package org.sportstogo.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sportstogo.backend.Models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
