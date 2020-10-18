package br.com.aisdigital.app.service;

import br.com.aisdigital.app.model.AlocacaoHoras;
import br.com.aisdigital.app.payload.AlocacaoHorasRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor=Exception.class)
@Service
public class AlocacaoHorasService {

    public AlocacaoHoras mapper(AlocacaoHorasRequest req) {
        AlocacaoHoras entidade = new AlocacaoHoras();
        entidade.setData(req.getData());
        entidade.setHoras(req.getHoras());
        entidade.setProjeto(req.getProjeto());
        return entidade;
    }

}
