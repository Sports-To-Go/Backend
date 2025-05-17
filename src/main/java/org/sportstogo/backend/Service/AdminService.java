package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.*;
import org.sportstogo.backend.Models.Revenue.PeriodType;
import org.sportstogo.backend.Repository.AdminRepository;
import org.sportstogo.backend.Repository.BanRepository;
import org.sportstogo.backend.Repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;


    public List<User> getUsersRegisteredLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return adminRepository.findUsersRegisteredAfterNative(oneWeekAgo);
    }

    public long getLocationCount() {
        return adminRepository.countAllLocations();
    }

    public long getReservationCount() {
        return adminRepository.countReservations();
    }

    public long getNumberOfUsersRegisteredInLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        return adminRepository.countUsersRegisteredAfterNative(oneWeekAgo);
    }



    public List<Revenue> getMonthlyRevenue(LocalDate from, LocalDate to) {
        return adminRepository.findRevenueByPeriodTypeAndPeriodStartBetween(
                PeriodType.MONTHLY, from, to
        );
    }


    public List<Revenue> getAnnualRevenue(LocalDate from, LocalDate to) {
        return adminRepository.findRevenueByPeriodTypeAndPeriodStartBetween(
                PeriodType.ANNUAL, from, to
        );
    }


    //bans
    private BanRepository banRepository;

    public List<Ban> getBans() {
        return banRepository.findAll();
    }

    public void addBan(Ban ban) {
        banRepository.save(ban);
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


    //reports
    private ReportRepository reportRepository;

    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    public void addReport(Report report) {
        reportRepository.save(report);
    }

    @Transactional
    public void updateReport(Long id, String reviewedBy, LocalDate reviewedAt, ReportStatus status) {

        Optional<Report> optionalReport = reportRepository.findById(id);

        if (optionalReport.isEmpty()) {
            throw new IllegalArgumentException("Report with id " + id + " does not exist");
        }

        Report report = optionalReport.get();

        if (reviewedBy != null) {
            report.setReviewedBy(reviewedBy);
        }

        if (reviewedAt != null) {
            report.setReviewedAt(reviewedAt);
        }

        if (status != null) {
            report.setStatus(status);
        }
    }

    public void deleteReport(Long id) {
        if  (!reportRepository.existsById(id)) {
            throw new IllegalArgumentException("Report with id " + id + " does not exist");
        }
        reportRepository.deleteById(id);
    }
}
