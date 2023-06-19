package com.rest.cinemaapi.services;

import com.rest.cinemaapi.configs.security.JwtService;
import com.rest.cinemaapi.models.LoginFormDTO;
import com.rest.cinemaapi.models.TokenJwt;
import com.rest.cinemaapi.models.TokenJwtDTO;
import com.rest.cinemaapi.repositories.UsherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UsherService {
    private final UsherRepository usherRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public UsherService(UsherRepository usherRepository,
                        AuthenticationManager authenticationManager,
                        JwtService jwtService) {
        this.usherRepository = usherRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenJwt loginToCinema(LoginFormDTO loginFormDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginFormDTO.getLogin(),
                loginFormDTO.getPassword()
        ));
        var usher = this.usherRepository.findUsherByEmail(loginFormDTO.getLogin()).orElseThrow();
        var jwtToken = this.jwtService.generateToken(usher);
        return TokenJwt.builder().token(jwtToken).build();
    }

    public TokenJwtDTO getTokenExpirationDate(TokenJwt token) {
        return TokenJwtDTO.builder()
                .token(token.getToken())
                .expirationDate(this.jwtService.getExpirationDate(token))
                .build();
    }
}
