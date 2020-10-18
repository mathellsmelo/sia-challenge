package br.com.aisdigital.app.controller;

import br.com.aisdigital.app.model.Feriado;
import br.com.aisdigital.app.payload.FeriadoRequest;
import br.com.aisdigital.app.payload.JwtAuthenticationResponse;
import br.com.aisdigital.app.payload.LoginRequest;
import br.com.aisdigital.app.repository.FeriadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/feriados")
public class FeriadoController {

    @Autowired
    FeriadoRepository repository;

    @PostMapping()
    public ResponseEntity<?> cadastrarFeriado(@Valid @RequestBody FeriadoRequest req) {
        Feriado feriado = new Feriado(req.getData());

        repository.save(feriado);

        return ResponseEntity.ok("Feriado Cadastrado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cadastrarFeriado(@PathVariable Integer id) {
        repository.deleteById(id);

        return ResponseEntity.ok("Feriado Deletado com sucesso.");
    }


}
