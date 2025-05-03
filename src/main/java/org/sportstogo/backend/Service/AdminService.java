package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.AdminRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public List<User> getUsersRegisteredLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        return adminRepository.findUsersRegisteredAfter(oneWeekAgo);
    }

    public long getLocationCount() {
        return adminRepository.countAllLocations();
    }

    public long getReservationCount() {
        return adminRepository.countReservations();
    }

    public long getNumberOfUsersRegisteredInLastWeek() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        return adminRepository.countUsersRegisteredAfter(oneWeekAgo);
    }

}
