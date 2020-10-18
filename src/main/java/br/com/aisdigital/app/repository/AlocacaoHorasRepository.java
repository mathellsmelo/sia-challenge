package br.com.aisdigital.app.repository;


import br.com.aisdigital.app.model.AlocacaoHoras;
import br.com.aisdigital.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlocacaoHorasRepository extends JpaRepository<AlocacaoHoras, Integer> {

    List<AlocacaoHoras> findByDataAndUsuario(LocalDate data, Usuario usuario);

}
