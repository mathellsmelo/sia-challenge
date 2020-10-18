package br.com.aisdigital.app.repository;

import br.com.aisdigital.app.model.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface FeriadoRepository extends JpaRepository<Feriado, Integer> {

    Boolean existsByData(LocalDate data);

    @Query("select count(feriado) from Feriado feriado where month(feriado.data) = :mes")
    Integer numeroFeriadosNoMes(Integer mes);

}
