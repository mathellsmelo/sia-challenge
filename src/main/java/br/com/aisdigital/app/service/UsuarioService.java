package br.com.aisdigital.app.service;

import br.com.aisdigital.app.security.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor=Exception.class)
@Service
public class UsuarioService {

    public Integer getIdUsuario(Authentication auth) {
        UserDetails client = (UserDetails) auth.getPrincipal();
        return ((UsuarioPrincipal) client).getId();
    }

}
