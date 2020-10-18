package br.com.aisdigital.app.model;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "username" })
})
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue
    @Column(nullable=false)
    private Integer id;

    private String username;

    private String password;

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
