package tn.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.cinema.dto.request.LoginRequest;
import tn.cinema.dto.request.RegisterRequest;
import tn.cinema.dto.response.AuthResponse;
import tn.cinema.entity.Utilisateur;
import tn.cinema.enums.Role;
import tn.cinema.exception.BusinessException;
import tn.cinema.repository.UtilisateurRepository;
import tn.cinema.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Cet email est déjà utilisé");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .role(Role.CLIENT)
                .actif(true)
                .build();

        utilisateurRepository.save(utilisateur);

        UserDetails userDetails = userDetailsService.loadUserByUsername(utilisateur.getEmail());
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .userId(utilisateur.getId())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(utilisateur.getEmail());
        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .userId(utilisateur.getId())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole())
                .build();
    }
}
