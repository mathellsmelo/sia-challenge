package br.com.aisdigital.app.service;

import br.com.aisdigital.app.model.AlocacaoHoras;
import br.com.aisdigital.app.model.PontoEletronico;
import br.com.aisdigital.app.model.Usuario;
import br.com.aisdigital.app.payload.AlocacaoHorasRequest;
import br.com.aisdigital.app.payload.HorasTrabalhadasResponse;
import br.com.aisdigital.app.payload.PontoEletronicoRequest;
import br.com.aisdigital.app.repository.AlocacaoHorasRepository;
import br.com.aisdigital.app.repository.FeriadoRepository;
import br.com.aisdigital.app.repository.PontoEletronicoRepository;
import br.com.aisdigital.app.repository.UsuarioRepository;
import br.com.aisdigital.app.vo.RegrasVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

@Transactional(rollbackFor=Exception.class)
@Service
public class PontoEletronicoService {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AlocacaoHorasService alocacaoHorasService;

    @Autowired
    PontoEletronicoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    FeriadoRepository feriadoRepository;

    @Autowired
    AlocacaoHorasRepository alocacaoHorasRepository;

    public String pontoEletronicoManha(PontoEletronicoRequest pontoEletronicoRequest, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(usuarioService.getIdUsuario(auth)).get();

        pontoEletronicoRequest.setPeriodo("M");
        RegrasVO regras = verificaRegras(pontoEletronicoRequest);
        if(!regras.getValido()) {
            return regras.getMsg();
        }

        PontoEletronico entidade = mapper(pontoEletronicoRequest);
        entidade.setUsuario(usuario);

        repository.save(entidade);

        return "Ponto registrado com sucesso!";
    }

    public String atualizarPontoEletronicoManha(PontoEletronicoRequest pontoEletronicoRequest, Authentication auth, Integer idPonto) {
        Usuario usuario = usuarioRepository.findById(usuarioService.getIdUsuario(auth)).get();
        pontoEletronicoRequest.setPeriodo("M");
        RegrasVO regras = verificaRegrasAtualizacao(pontoEletronicoRequest);
        if(!regras.getValido()) {
            return regras.getMsg();
        }

        PontoEletronico entidade = mapper(pontoEletronicoRequest);
        entidade.setUsuario(usuario);
        entidade.setId(idPonto);

        repository.save(entidade);

        return "Ponto atualizado com sucesso!";
    }

    public String atualizarPontoEletronicoTarde(PontoEletronicoRequest pontoEletronicoRequest, Authentication auth, Integer idPonto) {
        Usuario usuario = usuarioRepository.findById(usuarioService.getIdUsuario(auth)).get();
        pontoEletronicoRequest.setPeriodo("T");
        RegrasVO regras = verificaRegrasAtualizacao(pontoEletronicoRequest);
        if(!regras.getValido()) {
            return regras.getMsg();
        }

        PontoEletronico entidade = mapper(pontoEletronicoRequest);
        entidade.setUsuario(usuario);
        entidade.setId(idPonto);

        repository.save(entidade);

        return "Ponto atualizado com sucesso!";
    }

    public String pontoEletronicoTarde(PontoEletronicoRequest pontoEletronicoRequest, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(usuarioService.getIdUsuario(auth)).get();

        pontoEletronicoRequest.setPeriodo("T");
        RegrasVO regras = verificaRegras(pontoEletronicoRequest);
        if(!regras.getValido()) {
            return regras.getMsg();
        }

        PontoEletronico entidade = mapper(pontoEletronicoRequest);
        entidade.setUsuario(usuario);

        repository.save(entidade);

        return "Ponto registrado com sucesso!";
    }

    public static Boolean verificaHoraSaidaMaiorQueHoraEntrada(PontoEletronicoRequest pontoEletronicoRequest) {
        return pontoEletronicoRequest.getHoraSaida().isAfter(pontoEletronicoRequest.getHoraEntrada());
    }

    public static Boolean ehFimDeSemana(LocalDate ld) {
        DayOfWeek d = ld.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }

    public Boolean verificaPontoPeriodo(PontoEletronicoRequest pontoEletronicoRequest) {
        return repository.existsByPeriodoAndData(pontoEletronicoRequest.getPeriodo(), pontoEletronicoRequest.getData());
    }

    public Boolean verificaHorarioAlmoco(PontoEletronicoRequest pontoEletronicoRequest) {
        switch(pontoEletronicoRequest.getPeriodo()) {
            case "M":
                return verificaHorarioAlmocoManha(pontoEletronicoRequest);
            case "T":
                return verificaHorarioAlmocoTarde(pontoEletronicoRequest);
        }
        return false;
    }

    public Boolean verificaHorarioAlmocoManha(PontoEletronicoRequest pontoEletronicoRequest) {
        PontoEletronico pontoTarde = repository.findByPeriodoAndData("T", pontoEletronicoRequest.getData());

        if (pontoTarde == null) { return true; }

        Integer diff = Math.toIntExact(MINUTES.between(pontoEletronicoRequest.getHoraSaida(), pontoTarde.getHoraEntrada()));

        return diff < 60 ? false : true;
    }

    public Boolean verificaHorarioAlmocoTarde(PontoEletronicoRequest pontoEletronicoRequest) {
        PontoEletronico pontoManha = repository.findByPeriodoAndData("M", pontoEletronicoRequest.getData());

        if (pontoManha == null) { return true; }

        Integer diff = Math.toIntExact(MINUTES.between(pontoManha.getHoraSaida(), pontoEletronicoRequest.getHoraEntrada()));

        return diff < 60 ? false : true;
    }

    public Boolean verificaSobreposicaoPeriodos(PontoEletronicoRequest pontoEletronicoRequest) {
        PontoEletronico pontoPeriodoContrario = null;
        switch(pontoEletronicoRequest.getPeriodo()) {
            case "M":
                pontoPeriodoContrario = repository.findByPeriodoAndData("T", pontoEletronicoRequest.getData());
                break;
            case "T":
                pontoPeriodoContrario = repository.findByPeriodoAndData("M", pontoEletronicoRequest.getData());
                break;
        }
        if(pontoPeriodoContrario == null) { return false; }

        return pontoEletronicoRequest.getHoraEntrada().isBefore(pontoPeriodoContrario.getHoraSaida()) &&
                pontoPeriodoContrario.getHoraEntrada().isBefore(pontoEletronicoRequest.getHoraSaida());
    }

    public Boolean verificaFeriado(LocalDate data) {
        return feriadoRepository.existsByData(data);
    }

    public Boolean verificaHorasAlocadas(PontoEletronicoRequest pontoEletronicoRequest) {
        Integer horasTrabalhadasPeriodoContrario = null;
        switch(pontoEletronicoRequest.getPeriodo()) {
            case "M":
                horasTrabalhadasPeriodoContrario = horasTrabalhadasTarde(pontoEletronicoRequest.getData());
                break;
            case "T":
                horasTrabalhadasPeriodoContrario = horasTrabalhadasManha(pontoEletronicoRequest.getData());
                break;
        }

        List<AlocacaoHoras> listaHorasAlocadas = alocacaoHorasRepository.findByData(pontoEletronicoRequest.getData());
        Integer horasAlocadas = contarNumeroHorasAlocadas(listaHorasAlocadas);
        Integer horasTrabalhadas =  Math.toIntExact(MINUTES.between(pontoEletronicoRequest.getHoraEntrada(), pontoEletronicoRequest.getHoraSaida()));

        if((horasTrabalhadas + horasTrabalhadasPeriodoContrario) < horasAlocadas) {
            return false;
        }

        return true;
    }

    public RegrasVO verificaRegras(PontoEletronicoRequest pontoEletronicoRequest) {
        if(!verificaHoraSaidaMaiorQueHoraEntrada(pontoEletronicoRequest)) {
            return new RegrasVO(false, "Hora de entrada não deve ser maior que hora de saída.");
        }
        if(ehFimDeSemana(pontoEletronicoRequest.getData())){
            return new RegrasVO(false, "Não é permitido trabalhar aos fins de semana.");
        }
        if(verificaFeriado(pontoEletronicoRequest.getData())) {
            return new RegrasVO(false, "Não é permitido trabalhar em feriado.");
        }
        if(verificaPontoPeriodo(pontoEletronicoRequest)) {
            return new RegrasVO(false, "Já existe um ponto registrado neste dia, neste período.");
        }
        if(verificaSobreposicaoPeriodos(pontoEletronicoRequest)) {
            return new RegrasVO(false, "Os períodos, manhã e tarde, do dia não podem se sobrepor.");
        }
        if(!verificaHorarioAlmoco(pontoEletronicoRequest)) {
            return new RegrasVO(false, "É obrigatório ter pelo menos 1 hora de almoço.");
        }

        return new RegrasVO(true, "");
    }

    public RegrasVO verificaRegrasAtualizacao(PontoEletronicoRequest pontoEletronicoRequest) {
        if(verificaSobreposicaoPeriodos(pontoEletronicoRequest)) {
            return new RegrasVO(false, "Os períodos, manhã e tarde, do dia não podem se sobrepor.");
        }
        if(!verificaHorarioAlmoco(pontoEletronicoRequest)) {
            return new RegrasVO(false, "É obrigatório ter pelo menos 1 hora de almoço.");
        }
        if(!verificaHorasAlocadas(pontoEletronicoRequest)){
            return new RegrasVO(false, "O tempo alocado não pode ser maior que o novo tempo trabalhado no dia.");
        }

        return new RegrasVO(true, "");
    }

    public HorasTrabalhadasResponse horasTrabalhadas(Integer mes, Authentication auth) {
        List<PontoEletronico> listaHoras = repository.pontoEletronicoPorMesEUsuario(mes, usuarioService.getIdUsuario(auth));

        Integer numeroDeHoras = contarNumeroHoras(listaHoras) / 60;

        Integer numeroHorasMes = numeroDeHorasMes(mes) / 60;

        HorasTrabalhadasResponse objetoRetorno = new HorasTrabalhadasResponse(numeroDeHoras, numeroHorasMes, "Saldo de horas: " + (numeroDeHoras - numeroHorasMes));
        return objetoRetorno;
    }

    public Integer contarNumeroHoras(List<PontoEletronico> listaHoras) {
        Integer numeroDeHoras = 0;
        for (PontoEletronico ponto : listaHoras) {
            numeroDeHoras = numeroDeHoras + Math.toIntExact(MINUTES.between(ponto.getHoraEntrada(), ponto.getHoraSaida()));
        }
        return numeroDeHoras;
    }

    public Integer contarNumeroHorasAlocadas(List<AlocacaoHoras> listaHoras) {
        Integer numeroDeHoras = 0;
        for (AlocacaoHoras alocacao : listaHoras) {
            numeroDeHoras = numeroDeHoras + alocacao.getHoras().get(ChronoField.MINUTE_OF_DAY);
        }
        return numeroDeHoras;
    }

    public int numeroDeHorasMes(Integer mes) {
        LocalDate data = LocalDate.of(LocalDate.now().getYear(), mes, 1);
        return diasUteisMes(data) * 8 * 60;
    }

    public int diasUteisMes(LocalDate data) {
        YearMonth yearMonth = YearMonth.from(data);
        Integer dias = yearMonth.lengthOfMonth();
        Integer diasDaSemana = yearMonth.atDay(1).getDayOfWeek().getValue();
        Integer numeroFeriados = feriadoRepository.numeroFeriadosNoMes(data.getMonthValue());
        return (diasDaSemana <= 5 ? Math.min(dias - 8, 26 - diasDaSemana) : Math.max(dias + diasDaSemana - 16, 20)) - numeroFeriados;
    }

    public String alocarHorasTrabalhadas(AlocacaoHorasRequest req, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(usuarioService.getIdUsuario(auth)).get();

        AlocacaoHoras entidade = alocacaoHorasService.mapper(req);
        entidade.setUsuario(usuario);

        List<PontoEletronico> listaHoras = repository.findByData(req.getData());
        List<AlocacaoHoras> listaHorasAlocadas = alocacaoHorasRepository.findByData(req.getData());

        Integer horasTrabalhadasNoDia = contarNumeroHoras(listaHoras);
        Integer horasAlocadas = contarNumeroHorasAlocadas(listaHorasAlocadas);
        Integer horasASeremAlocadas = req.getHoras().get(ChronoField.MINUTE_OF_DAY);

        if(horasASeremAlocadas > horasTrabalhadasNoDia) {
            return "Não é possível alocar mais horas do que total trabalhado no dia";
        }
        if((horasAlocadas + horasASeremAlocadas) > horasTrabalhadasNoDia) {
            return "Não é possível alocar mais horas do que total trabalhado no dia";
        }

        alocacaoHorasRepository.save(entidade);

        return "Horas alocadas com sucesso";
    }

    public Integer horasTrabalhadasTarde(LocalDate data) {
        PontoEletronico pontoTarde = repository.findByPeriodoAndData("T", data);

        if (pontoTarde == null) { return 0; }

        return Math.toIntExact(MINUTES.between(pontoTarde.getHoraEntrada(), pontoTarde.getHoraSaida()));
    }

    public Integer horasTrabalhadasManha(LocalDate data) {
        PontoEletronico pontoTarde = repository.findByPeriodoAndData("M", data);

        if (pontoTarde == null) { return 0; }

        return Math.toIntExact(MINUTES.between(pontoTarde.getHoraEntrada(), pontoTarde.getHoraSaida()));
    }

    public PontoEletronico mapper(PontoEletronicoRequest req) {
        PontoEletronico entidade = new PontoEletronico();
        entidade.setData(req.getData());;
        entidade.setHoraEntrada(req.getHoraEntrada());
        entidade.setHoraSaida(req.getHoraSaida());
        entidade.setPeriodo(req.getPeriodo());

        return entidade;
    }

}
