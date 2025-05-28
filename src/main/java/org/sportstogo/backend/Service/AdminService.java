package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Models.Revenue;
import org.sportstogo.backend.Models.Revenue.PeriodType;
import org.sportstogo.backend.Repository.AdminRepository;
import org.sportstogo.backend.Repository.RevenueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final RevenueRepository revenueRepository;


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
    public long getUsersCount(){return adminRepository.countUsers();}

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

    public boolean isAdmin(String uid) {
        return adminRepository.findById(uid).isPresent();
    }

    public Revenue saveRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

}
