package br.com.aisdigital.app.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {

    private String username;

    private String password;

}
