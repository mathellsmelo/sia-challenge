package br.com.aisdigital.app.controller;

import br.com.aisdigital.app.model.Usuario;
import br.com.aisdigital.app.payload.ApiResponse;
import br.com.aisdigital.app.payload.JwtAuthenticationResponse;
import br.com.aisdigital.app.payload.LoginRequest;
import br.com.aisdigital.app.payload.SignUpRequest;
import br.com.aisdigital.app.repository.UsuarioRepository;
import br.com.aisdigital.app.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Nome de usuário já existe"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        Usuario usuario = new Usuario(signUpRequest.getUsername(), signUpRequest.getPassword());

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario resultado = usuarioRepository.save(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(usuario.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Usuário Registrado com sucesso"));
    }

}