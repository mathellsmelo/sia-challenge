package br.com.aisdigital.app.controller;

import br.com.aisdigital.app.model.Usuario;
import br.com.aisdigital.app.payload.AlocacaoHorasRequest;
import br.com.aisdigital.app.payload.JwtAuthenticationResponse;
import br.com.aisdigital.app.payload.LoginRequest;
import br.com.aisdigital.app.payload.PontoEletronicoRequest;
import br.com.aisdigital.app.security.JwtTokenProvider;
import br.com.aisdigital.app.security.UsuarioPrincipal;
import br.com.aisdigital.app.service.PontoEletronicoService;
import br.com.aisdigital.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/ponto-eletronico")
public class PontoEletronicoController {

    @Autowired
    PontoEletronicoService pontoEletronicoService;

    @PostMapping("/manha")
    public ResponseEntity<?> pontoEletronicoManha(@Valid @RequestBody PontoEletronicoRequest pontoEletronicoRequest, Authentication auth) {
        return ResponseEntity.ok(pontoEletronicoService.pontoEletronicoManha(pontoEletronicoRequest, auth));
    }

    @PostMapping("/tarde")
    public ResponseEntity<?> pontoEletronicoTarde(@Valid @RequestBody PontoEletronicoRequest pontoEletronicoRequest, Authentication auth) {
        return ResponseEntity.ok(pontoEletronicoService.pontoEletronicoTarde(pontoEletronicoRequest, auth));
    }

    @PutMapping("{idPonto}/manha")
    public ResponseEntity<?> atualizarPontoEletronicoManha(@Valid @RequestBody PontoEletronicoRequest pontoEletronicoRequest, Authentication auth, @PathVariable Integer idPonto) {
        return ResponseEntity.ok(pontoEletronicoService.atualizarPontoEletronicoManha(pontoEletronicoRequest, auth, idPonto));
    }

    @PutMapping("{idPonto}/tarde")
    public ResponseEntity<?> atualizarPontoEletronicoTarde(@Valid @RequestBody PontoEletronicoRequest pontoEletronicoRequest, Authentication auth, @PathVariable Integer idPonto) {
        return ResponseEntity.ok(pontoEletronicoService.atualizarPontoEletronicoTarde(pontoEletronicoRequest, auth, idPonto));
    }

    @GetMapping("/{mes}/horas")
    public ResponseEntity<?> horasMes(@PathVariable Integer mes, Authentication auth) {
        return ResponseEntity.ok(pontoEletronicoService.horasTrabalhadas(mes, auth));
    }

    @PostMapping("/alocacao-horas")
    public ResponseEntity<?> alocacaoHoras(@Valid @RequestBody AlocacaoHorasRequest alocacaoHorasRequest, Authentication auth) {
        return ResponseEntity.ok(pontoEletronicoService.alocarHorasTrabalhadas(alocacaoHorasRequest, auth));
    }


}
