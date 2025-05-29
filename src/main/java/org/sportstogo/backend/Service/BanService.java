package org.sportstogo.backend.Service;


import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Ban;
import org.sportstogo.backend.Repository.BanRepository;
import org.sportstogo.backend.Repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BanService {

    private BanRepository banRepository;
    private final ReportRepository reportRepository;
    public List<Ban> getBans() {
        return banRepository.findAll();
    }

    @Transactional
    public void addBan(Ban ban) {
        banRepository.save(ban);
        reportRepository.deleteByTargetId(ban.getIdUser());
    }

    @Transactional
    public void updateBan(Long id, Integer duration, String reason) {

        Optional<Ban> optionalBan = banRepository.findById(id);

        if (optionalBan.isEmpty()) {
            throw new IllegalArgumentException("Ban with id " + id + " does not exist");
        }

        Ban ban = optionalBan.get();

        if (duration != null) {
            ban.setDuration(ban.getDuration() + duration);
        }

        if (reason != null) {
            ban.setReason(reason);
        }
    }

    public void deleteBan(Long id) {
        if  (!banRepository.existsById(id)) {
            throw new IllegalArgumentException("Ban with id " + id + " does not exist");
        }
        banRepository.deleteById(id);
    }
}
