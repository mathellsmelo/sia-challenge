package br.com.aisdigital.app.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HorasTrabalhadasResponse {

    Integer horasTrabalhadas;
    Integer horasMensais;
    String mensagem;

    public HorasTrabalhadasResponse(Integer horasTrabalhadas, Integer horasMensais, String mensagem) {
        this.horasTrabalhadas = horasTrabalhadas;
        this.horasMensais = horasMensais;
        this.mensagem = mensagem;
    }

}
