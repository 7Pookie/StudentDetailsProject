package com.example.studentDetailsBackEnd.Config;

import com.example.studentDetailsBackEnd.repository.StudentRepository;
import com.example.studentDetailsBackEnd.repository.FacultyRepository;
import com.example.studentDetailsBackEnd.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;

    public OAuth2LoginSuccessHandler(StudentRepository studentRepository, FacultyRepository facultyRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        HttpSession session = request.getSession();
        session.setAttribute("EMAIL", email);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes session timeout

        // ✅ Retrieve role from session
        String role = (String) session.getAttribute("USER_ROLE");

        if (role == null) {
            System.out.println("🚨 No role found in session, redirecting to role selection page.");
            response.sendRedirect("http://localhost:5173/");
            return;
        }

        System.out.println("✅ User logged in: " + email + ", Role: " + role);

        // ✅ Check if the email exists in the corresponding table
        boolean exists = false;
        switch (role) {
            case "student":
                exists = studentRepository.existsByEmail(email);
                break;
            case "faculty":
                exists = facultyRepository.existsByEmail(email);
                break;
            case "admin":
                exists = adminRepository.existsByEmail(email);
                break;
        }

        if (!exists) {
            System.out.println("🚨 User email not found in " + role + " table. Redirecting to invalid-user route.");
            response.sendRedirect("http://localhost:5173/invalid-user");
            return;
        }

        // ✅ Redirect to the correct dashboard if the user exists
        String redirectUrl = "http://localhost:5173/" + role + "/dashboard";
        System.out.println("✅ Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
