package br.com.aisdigital.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "PontosUsuarios")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PontoEletronico {

    @Id
    @GeneratedValue
    @Column(nullable=false)
    private Integer id;

    private LocalDate data;

    private LocalTime horaEntrada;

    private LocalTime horaSaida;

    private String periodo;

    @ManyToOne(optional=false)
    @JoinColumn(name="idUsuario",referencedColumnName="id")
    private Usuario usuario;

}
