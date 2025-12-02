package com.example.demo.Agendamento;

import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioRepository;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioVO;
import com.example.demo.Servico.ServicoRepository;
import com.example.demo.Servico.ServicoVO;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository fRepository;

    @Autowired
    private ProfissionalRepository fProfissionalRepository;

    @Autowired
    private ServicoRepository fServicoRepository;

    @Autowired
    private UsuarioRepository fUsuarioRepository;

    @Autowired
    private ProfissionalHorarioRepository fProfissionalHorarioRepository;

    private Long fAgendamentoId;

    public AgendamentoVO dtoForVo(AgendamentoDTO mDTO, AgendamentoVO mAgendamentoVO) {
        Optional<ProfissionalVO> mProfissionalVO = fProfissionalRepository.findById(mDTO.getProfissionalId());
        Optional<ServicoVO> mServicoVO = fServicoRepository.findById(mDTO.getServicoId());
        Optional<UsuarioVO> mUsuarioVO = fUsuarioRepository.findById(mDTO.getUsuarioId());

        mAgendamentoVO.setData(mDTO.getData());
        mAgendamentoVO.setHorarioIncio(mDTO.getHorario());

        Long mDuracaoServico = mServicoVO.get().getDuracao();
        mAgendamentoVO.setHorarioFim(mAgendamentoVO.getHorarioIncio().plusMinutes(mDuracaoServico));

        mAgendamentoVO.setStatus(mDTO.getStatus());
        mAgendamentoVO.setValor(mDTO.getValor());
        mAgendamentoVO.setNomeProfissional(mProfissionalVO.get().getNome());
        mAgendamentoVO.setNomeServico(mServicoVO.get().getNome());
        mAgendamentoVO.setUsuarioCadastrado(mDTO.getUsuarioCadastrado());

        if (!mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE")) { //Preenche somente o nome, não preenche o campo age_usu_id
            mAgendamentoVO.setNomeUsuario(mDTO.getNomeUsuario().toUpperCase());
        } else {
            mAgendamentoVO.setNomeUsuario(mUsuarioVO.get().getNome().toUpperCase());
            mAgendamentoVO.setUsuarioVO(mUsuarioVO.get());
        }

        mAgendamentoVO.setProfissionalVO(mProfissionalVO.get());
        mAgendamentoVO.setServicoVO(mServicoVO.get());

        return mAgendamentoVO;
    }

    public ResponseEntity<?> isValid(AgendamentoDTO mDTO) {
        Optional<AgendamentoVO> mAgendamentoVO = Optional.of(new AgendamentoVO());
        if (fAgendamentoId != null) {
            mAgendamentoVO = fRepository.findById(fAgendamentoId);
            if (mAgendamentoVO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponseUtil.response("Erro", "Nenhum agendamento encontrado com esse id!")
                );
            }
        }

        Optional<ProfissionalVO> mVO = fProfissionalRepository.findById(mDTO.getProfissionalId());
        if (fProfissionalRepository.findById(mDTO.getProfissionalId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum profissional localizado com esse id!")
            );
        }

        Optional<ServicoVO> mServicoVO = fServicoRepository.findById(mDTO.getServicoId());
        if (mServicoVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum serviço localizado com esse id!")
            );
        }

        if (fUsuarioRepository.findById(mDTO.getUsuarioId()).isEmpty() && (mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE"))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum usuário localizado com esse id!")
            );
        }

        if (!mDTO.getStatus().toString().toUpperCase().matches("CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "No campo stattus informe entre CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO!")
            );
        }

        if (mDTO.getData().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Data não pode ser menor que a atual")
            );
        }

        if (!mDTO.getUsuarioCadastrado().toUpperCase().matches("TRUE|FALSE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "No campo usuarioCadastrado informe entre TRUE ou FALSE!")
            );
        }

        if (!mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE") && (mDTO.getNomeUsuario().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Quando não for usuario com cadastro preencha o campo 'nomeUsuario'")
            );
        }

        List<String> mHorariosDisponiveis =
                Optional.ofNullable(
                        (List<String>) getAvailableTime(
                                mDTO.getProfissionalId(),
                                mServicoVO.get().getId(),
                                mDTO.getData()
                        ).getBody()
                ).orElseGet(Collections::emptyList);

        if (mHorariosDisponiveis == null || mHorariosDisponiveis.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Nenhum horário disponível para essa data")
            );
        }

        String mHorarioStr = mDTO.getHorario().toString();
        boolean mExisteAgendamento = mAgendamentoVO.isEmpty();
        boolean mHorarioMudou =
                mAgendamentoVO != null &&
                        mAgendamentoVO.get().getHorarioIncio() != null &&
                        !mHorarioStr.equals(mAgendamentoVO.get().getHorarioIncio().toString());

        if (!mExisteAgendamento || mHorarioMudou) {
            if (!mHorariosDisponiveis.contains(mHorarioStr)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ApiResponseUtil.response("Erro", "Horário não disponível")
                );
            }
        }

        return null;
    }

    public ResponseEntity<?> register(AgendamentoDTO mDTO) {
        ResponseEntity<?> mValidacao = isValid(mDTO);
        if (mValidacao != null) {
            return mValidacao;
        }

        AgendamentoVO mAgendamentoVO = new AgendamentoVO();
        try {
            mAgendamentoVO = dtoForVo(mDTO, mAgendamentoVO);
            fRepository.save(mAgendamentoVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("Sucesso", "Agendamento cadastrado com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public List<? extends Object> list() {
        List<AgendamentoVO> mAgendamentoVO = fRepository.findAll();
        return mAgendamentoVO.stream()
                .map(mVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", mVO.getId());
                    map.put("nomeProfissional", mVO.getNomeProfissional());
                    map.put("profissionalId", mVO.getProfissionalVO().getId());
                    map.put("usuarioNome", mVO.getNomeUsuario());
                    map.put("usuarioId",
                            mVO.getUsuarioVO() != null ? mVO.getUsuarioVO().getId() : null);
                    map.put("servico", mVO.getNomeServico());
                    map.put("servicoId", mVO.getServicoVO().getId());
                    map.put("valor", mVO.getValor());
                    map.put("data", mVO.getData());
                    map.put("horarioInicio", mVO.getHorarioIncio());
                    map.put("horarioFim", mVO.getHorarioFim());
                    map.put("status", mVO.getStatus());
                    return map;
                })
                .toList();
    }

    public List<? extends Object> listPerClient(Long mId) {
        List<AgendamentoVO> mAgendamentoVO = fRepository.findAllById(mId);
        return mAgendamentoVO.stream()
                .map(mVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", mVO.getId());
                    map.put("nomeProfissional", mVO.getNomeProfissional());
                    map.put("profissionalId", mVO.getProfissionalVO().getId());
                    map.put("usuarioNome", mVO.getNomeUsuario());
                    map.put("usuarioId",
                            mVO.getUsuarioVO() != null ? mVO.getUsuarioVO().getId() : null);
                    map.put("servico", mVO.getNomeServico());
                    map.put("servicoId", mVO.getServicoVO().getId());
                    map.put("valor", mVO.getValor());
                    map.put("data", mVO.getData());
                    map.put("horarioInicio", mVO.getHorarioIncio());
                    map.put("horarioFim", mVO.getHorarioFim());
                    map.put("status", mVO.getStatus());
                    return map;
                })
                .toList();
    }

    public ResponseEntity<?> getAvailableTime(Long mProfissionalId, Long mServicoId, LocalDate mData) {
        String mDayOfWeek = convertDayForPortuguese(mData.getDayOfWeek().toString().toUpperCase());
        ProfissionalHorarioVO mProfissionalHorarioVO =
                fProfissionalHorarioRepository.findByProfissionalVO_IdAndDiaSemanaContaining(
                        mProfissionalId, mDayOfWeek);

        if (mProfissionalHorarioVO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum horário disponível para esse profissional")
            );
        }

        List<AgendamentoVO> mAgendamentos = fRepository
                .findByProfissionalVO_IdAndDataAndStatusIn(
                        mProfissionalId,
                        mData,
                        List.of(EnumAgendamentoStatus.CONCLUIDO, EnumAgendamentoStatus.CONFIRMADO)
                );

        Optional<ServicoVO> mServicoVO = fServicoRepository.findById(mServicoId);
        Long mDuracaoServico = mServicoVO.get().getDuracao();

        LocalTime mHorainicio = mProfissionalHorarioVO.getHoraInicial();
        LocalTime mHorafim = mProfissionalHorarioVO.getHoraFinal();
        LocalTime mHoraAtual = mHorainicio;
        List<LocalTime> mHorariosPossiveis = new ArrayList<>();

        while (mHoraAtual.isBefore(mHorafim)) {
            mHorariosPossiveis.add(mHoraAtual);
            mHoraAtual = mHoraAtual.plusMinutes(mDuracaoServico);
        }

        List<LocalTime> mHorariosLivres = mHorariosPossiveis.stream()
                .filter(horario -> mAgendamentos.stream().noneMatch(a -> {
                    LocalTime inicio = a.getHorarioIncio();
                    LocalTime fim = a.getHorarioFim();

                    if (inicio == null || fim == null) {
                        return false;
                    }

                    return horario.isBefore(fim) &&
                            horario.plusMinutes(mDuracaoServico).isAfter(inicio);
                }))
                .toList();

        List<String> mResposta = mHorariosLivres.stream()
                .map(LocalTime::toString)
                .toList();

        return ResponseEntity.ok().body(mResposta);
    }


    public String convertDayForPortuguese(String mDay) {
        String mConvertDay = "";
        switch (mDay) {
            case "MONDAY":
                mConvertDay = "seg";
                break;
            case "TUESDAY":
                mConvertDay = "ter";
                break;
            case "WEDNESDAY":
                mConvertDay = "qua";
                break;
            case "THURSDAY":
                mConvertDay = "qui";
                break;
            case "FRIDAY":
                mConvertDay = "sex";
                break;
            case "SATURDAY":
                mConvertDay = "sab";
                break;
            case "SUNDAY":
                mConvertDay = "dom";
                break;
        }

        return mConvertDay;
    }

    public ResponseEntity<?> edit(AgendamentoDTO mDTO, Long mId) {
        Optional<AgendamentoVO> mAgendamentoVO = fRepository.findById(mId);
        fAgendamentoId = mId;

        ResponseEntity mValidacao = isValid(mDTO);
        if (mValidacao != null) {
            return mValidacao;
        }
        try {
            mAgendamentoVO = Optional.ofNullable(dtoForVo(mDTO, mAgendamentoVO.get()));
            fRepository.save(mAgendamentoVO.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Agendamento alterado com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public ResponseEntity<?> delete(Long mId) {
        Optional<AgendamentoVO> mAgendamentoVO = fRepository.findById(mId);
        if (mAgendamentoVO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum agendamento localizado com esse id!")
            );
        }

        try {
            fRepository.delete(mAgendamentoVO.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponseUtil.response("Sucesso", "Agendamento excluido com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public ResponseEntity<?> cancel(Long mId){
        Optional<AgendamentoVO> mAgendamentoVO = fRepository.findById(mId);
        if (mAgendamentoVO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum agendamento localizado com esse ID!")
            );
        }

        AgendamentoVO mVO = mAgendamentoVO.get();
        if (mVO.getStatus().equals(String.valueOf(EnumAgendamentoStatus.CANCELADO))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Agendamento já cancelado!")
            );
        }

        mVO.setStatus(EnumAgendamentoStatus.CANCELADO);
        try {
            fRepository.save(mVO);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Sucesso", "Agendamento cancelado!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }
}
