package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    @Query("SELECT b FROM Bug b WHERE b.id = :id")
    Bug findBugById(@Param("id") Long id);


    @Query("SELECT b FROM Bug b WHERE b.status = false")
    List<Bug> findUnresolvedBugs();

}
