package com.rotaclara.service;

import com.rotaclara.domain.Usuario;
import com.rotaclara.exception.RegraDeNegocioException;
import com.rotaclara.repository.UsuarioRepository;
import com.rotaclara.security.JwtService;
import com.rotaclara.web.dto.AuthDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public AuthDto.LoginResponse autenticar(AuthDto.LoginRequest request) {
        Usuario usuario = usuarioRepository.findByLogin(request.login())
                .orElseThrow(() -> new RegraDeNegocioException("Login ou senha inválidos."));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new RegraDeNegocioException("Login ou senha inválidos.");
        }

        String nome = switch (usuario.getPerfil()) {
            case MOTORISTA -> usuario.getMotorista() != null ? usuario.getMotorista().getNome() : usuario.getLogin();
            case GERENTE -> usuario.getGerente() != null ? usuario.getGerente().getNome() : usuario.getLogin();
            case ADMINISTRADOR -> usuario.getLogin();
        };

        String token = jwtService.gerarToken(usuario.getLogin(), usuario.getPerfil().name());
        return new AuthDto.LoginResponse(token, usuario.getPerfil().name(), nome);
    }
}
