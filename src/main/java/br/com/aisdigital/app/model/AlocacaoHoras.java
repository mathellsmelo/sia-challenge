package br.com.aisdigital.app.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "AlocacaoHoras")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlocacaoHoras {

    @Id
    @GeneratedValue
    private Integer id;

    private String projeto;

    private LocalDate data;

    private LocalTime horas;

    @ManyToOne(optional=false)
    @JoinColumn(name="idUsuario",referencedColumnName="id")
    private Usuario usuario;


}
