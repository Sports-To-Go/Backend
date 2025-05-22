package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Bug;
import org.sportstogo.backend.Repository.BugRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BugService {
    private BugRepository bugRepository;


    public Bug addBug(Bug bug) {
        bug.setReportedTime(LocalDateTime.now());
        bug.setStatus(false);
        return bugRepository.save(bug);
    }

    public Bug resolveBug(Long id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found with id: " + id));
        bug.setStatus(true);
        return bugRepository.save(bug);
    }

    public List<Bug> getUnresolvedBugs() {
        return bugRepository.findUnresolvedBugs();
    }
}
