package br.com.aisdigital.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "feriados")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Feriado {

    @Id
    @GeneratedValue
    @Column(nullable=false)
    private Integer id;

    private LocalDate data;

    public Feriado(LocalDate data) {
        this.data = data;
    }
}
