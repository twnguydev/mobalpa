package com.mobalpa.api.service;

import com.mobalpa.api.dto.RegisterRequestDTO;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.UserCoupon;
import com.mobalpa.api.repository.RoleRepository;
import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.repository.UserCouponRepository;
import com.mobalpa.api.repository.CouponCodeRepository;
import com.mobalpa.api.util.JwtUtil;
import com.mobalpa.api.model.CouponCode;
import com.mobalpa.api.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.format.DateTimeFormatter;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CouponCodeRepository couponCodeRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUuid(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User registerUser(RegisterRequestDTO registerUser) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(registerUser.getEmail()));
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + registerUser.getEmail() + " already exists");
        }

        User user = new User();
        user.setFirstname(registerUser.getFirstname());
        user.setLastname(registerUser.getLastname());
        user.setEmail(registerUser.getEmail());
        user.setPhoneNumber(registerUser.getPhoneNumber());
        user.setBirthdate(registerUser.getBirthdate());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);

        Role userRole = roleService.getRoleByName("ROLE_USER");
        user.getRoles().add(userRole);

        userRepository.save(user);
        sendConfirmationEmail(user);
        return user;
    }

    public void sendConfirmationEmail(User user) {
        String confirmationUrl = appBaseUrl + "api/users/confirm?token=" + user.getToken();
        try {
            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Confirmation de l'email",
                    "confirmationEmailTemplate.html",
                    null,
                    null,
                    "${user.firstName}", user.getFirstname(),
                    "${confirmationUrl}", confirmationUrl,
                    "${appName}", "Mobalpa");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public User confirmUser(String token) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setActive(true);
            user.setToken(null);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public String generateToken(User user) {
        Optional<User> existingUser = Optional.of(userRepository.findByEmail(user.getEmail()));
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " not found");
        }

        String token = tokenService.getTokenByEmail(user.getEmail());
        if (token == null) {
            token = jwtUtil.generateToken(user);
            tokenService.saveToken(user.getEmail(), token);
        }
        return token;
    }

    public void sendPasswordResetEmail(User user) {
        String resetToken = UUID.randomUUID().toString();
        user.setToken(resetToken);
        userRepository.save(user);

        String resetUrlWithToken = "http://localhost:4200/auth/reinitialiser-mot-de-passe?token=" + resetToken;
        try {
            emailService.sendHtmlEmail(user.getEmail(),
                    "Réinitialisation du mot de passe",
                    "passwordResetTemplate.html",
                    null,
                    null,
                    "${resetUrlWithToken}", resetUrlWithToken,
                    "${appName}", "Mobalpa");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public User resetPassword(String token, String newPassword) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setToken(null);
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(UUID uuid, User user) {
        return userRepository.findById(uuid).map(existingUser -> {
            if (user.getFirstname() != null)
                existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null)
                existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null)
                existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null)
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getPhoneNumber() != null)
                existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getBirthdate() != null)
                existingUser.setBirthdate(user.getBirthdate());
            if (user.getZipcode() != null)
                existingUser.setZipcode(user.getZipcode());
            if (user.getCity() != null)
                existingUser.setCity(user.getCity());
            if (user.getAddress() != null)
                existingUser.setAddress(user.getAddress());

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                Set<Role> roles = user.getRoles().stream()
                        .map(role -> roleRepository.findByName(role.getName())
                                .orElseThrow(
                                        () -> new IllegalArgumentException("Role " + role.getName() + " not found")))
                        .collect(Collectors.toSet());
                existingUser.setRoles(roles);
            }

            existingUser.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User with id " + uuid + " not found"));
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createBirthdayPromos() {
        System.out.println("Cron job started at " + LocalDateTime.now());
        MonthDay today = MonthDay.now();
        List<User> users = userRepository.findAll();
    
        for (User user : users) {
            System.out.println("Checking user: " + user.getUuid());
            if (isBirthdayToday(user, today)) {
                createPromoForUser(user);
            } else {
                System.out.println("No birthday promo for user " + user.getUuid() + " at date " + today + " expecting " + user.getBirthdate());
            }
        }
        System.out.println("Cron job finished at " + LocalDateTime.now());
    }    

    private boolean isBirthdayToday(User user, MonthDay today) {
        return MonthDay.from(user.getBirthdate()).equals(today);
    }    

    private void createPromoForUser(User user) {
        int currentYear = LocalDate.now().getYear();
        String couponName = "BIRTHDAY_" + user.getUuid() + "_" + currentYear;

        CouponCode existingCoupon = couponCodeRepository.findByName(couponName).orElse(null);
        if (existingCoupon != null) {
            System.out.println("Coupon déjà existant pour l'utilisateur " + user.getUuid() + " pour l'année " + currentYear);
            return;
        }

        String lastYearCouponName = "BIRTHDAY_" + user.getUuid() + "_" + (currentYear - 1);
        CouponCode lastYearCoupon = couponCodeRepository.findByName(lastYearCouponName).orElse(null);
        if (lastYearCoupon != null) {
            UserCoupon lastYearUserCoupon = userCouponRepository.findByUserAndCoupon(user, lastYearCoupon).orElse(null);
            if (lastYearUserCoupon != null && lastYearUserCoupon.isClaimed()) {
                System.out.println("Coupon d'anniversaire pour l'année " + (currentYear - 1) + " déjà réclamé.");
            }
        }

        CouponCode coupon = new CouponCode();
        coupon.setName(couponName);
        coupon.setDiscountRate(10.0);
        coupon.setDiscountType(CouponCode.DiscountType.PERCENTAGE);
        coupon.setDateStart(LocalDateTime.now());
        coupon.setDateEnd(LocalDateTime.now().plusWeeks(1));
        coupon.setTargetType(CouponCode.TargetType.USER);
        coupon.setTargetUsers(List.of(user.getUuid().toString()));
        coupon.setMaxUse(1);
    
        couponCodeRepository.save(coupon);
    
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setClaimed(false);
        userCouponRepository.save(userCoupon);
    
        try {
            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Bon anniversaire, " + user.getFirstname() + " !",
                    "promoBirthdayTemplate.html",
                    null,
                    null,
                    "${user.firstName}", user.getFirstname(),
                    "${promoCode}", coupon.getName(),
                    "${discountAmount}", coupon.getDiscountRate().toString() + (coupon.getDiscountType() == CouponCode.DiscountType.PERCENTAGE ? "%" : "€"),
                    "${validUntil}", coupon.getDateEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "${appName}", "Mobalpa"
            );
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }    

    public Optional<User> findByIdWithDetails(UUID userUuid) {
        return userRepository.findByIdWithDetails(userUuid);
    }
}
