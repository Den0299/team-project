package co.develhope.team_project.controllers;

import co.develhope.team_project.entities.Utente;
import co.develhope.team_project.auth.jwt.JWTService;
import co.develhope.team_project.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Utente utente) {
        utenteService.registerUser(utente);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Utente utente) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(utente.getEmail(), utente.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }
        UserDetails userDetails = utenteService.loadUserByUsername(utente.getEmail());
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }
}
