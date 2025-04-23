package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void registerNewUser(User user) {
        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email-ul exista deja");
        } else if  (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username-ul exista deja");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if  (!matcher.matches()) {
            throw new IllegalStateException("Email-ul nu este valid");
        }
        user.setDateCreated(LocalDate.now());
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    public void updateUser(Long id, String email, String username) {
        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = userOp.get();
        if (email != null) {
            if (this.userRepository.findByEmail(email).isPresent()) {
                throw new IllegalStateException("Email-ul exista deja");
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(user.getEmail());
            if  (!matcher.matches()) {
                throw new IllegalStateException("Email-ul nu este valid");
            }
            user.setEmail(email);
        }
        if (username != null) {
            if  (this.userRepository.findByUsername(username).isPresent()) {
                throw new IllegalStateException("Username-ul exista deja");
            }
            user.setUsername(username);
        }
    }

}
