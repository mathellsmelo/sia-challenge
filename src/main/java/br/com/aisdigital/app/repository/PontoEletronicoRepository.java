package br.com.aisdigital.app.repository;


import br.com.aisdigital.app.model.PontoEletronico;
import br.com.aisdigital.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PontoEletronicoRepository extends JpaRepository<PontoEletronico, Integer> {

    Boolean existsByPeriodoAndData(String periodo, LocalDate data);

    PontoEletronico findByPeriodoAndData(String periodo, LocalDate data);

    List<PontoEletronico> findByData(LocalDate data);

    @Query("select pe from PontoEletronico pe where month(pe.data) = :mes and pe.usuario.id = :idUsuario")
    List<PontoEletronico> pontoEletronicoPorMesEUsuario(Integer mes, Integer idUsuario);

}
