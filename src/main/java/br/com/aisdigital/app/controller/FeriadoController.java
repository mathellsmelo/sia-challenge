package br.com.aisdigital.app.controller;

import br.com.aisdigital.app.model.Feriado;
import br.com.aisdigital.app.payload.JwtAuthenticationResponse;
import br.com.aisdigital.app.payload.LoginRequest;
import br.com.aisdigital.app.repository.FeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/feriados")
public class FeriadoController {

    @Autowired
    FeriadoRepository repository;

    @PostMapping()
    public ResponseEntity<?> cadastrarFeriado(@Valid @RequestBody LocalDate data) {
        repository.save(new Feriado(data));

        return ResponseEntity.ok("Feriado Cadastrado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cadastrarFeriado(@PathVariable Integer id) {
        repository.deleteById(id);

        return ResponseEntity.ok("Feriado Deletado com sucesso.");
    }


}
