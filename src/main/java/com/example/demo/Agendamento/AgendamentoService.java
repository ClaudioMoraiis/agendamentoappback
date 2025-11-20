package com.example.demo.Agendamento;

import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.Servico.ServicoRepository;
import com.example.demo.Servico.ServicoVO;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import com.example.demo.UsuarioToken.UsuarioTokenRepository;
import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public AgendamentoVO dtoForVo(AgendamentoDTO mDTO, AgendamentoVO mAgendamentoVO){
        Optional<ProfissionalVO> mProfissionalVO = fProfissionalRepository.findById(mDTO.getProfissionalId());
        Optional<ServicoVO> mServicoVO = fServicoRepository.findById(mDTO.getServicoId());
        Optional<UsuarioVO> mUsuarioVO = fUsuarioRepository.findById(mDTO.getUsuarioId());

        mAgendamentoVO.setData(mDTO.getData());
        mAgendamentoVO.setHorario(mAgendamentoVO.getHorario());
        mAgendamentoVO.setStatus(mDTO.getStatus());
        mAgendamentoVO.setValor(mDTO.getValor());
        mAgendamentoVO.setNomeProfissional(mProfissionalVO.get().getNome());
        mAgendamentoVO.setNomeServico(mServicoVO.get().getNome());
        mAgendamentoVO.setUsuarioCadastrado(mDTO.getUsuarioCadastrado());

        if (!mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE")) { //Preenche somente o nome, não preenche o campo age_usu_id
            mAgendamentoVO.setNomeUsuario(mDTO.getNomeUsuario().toUpperCase());
        }else {
            mAgendamentoVO.setNomeUsuario(mUsuarioVO.get().getNome().toUpperCase());
            mAgendamentoVO.setUsuarioVO(mUsuarioVO.get());
        }

        mAgendamentoVO.setProfissionalVO(mProfissionalVO.get());
        mAgendamentoVO.setServicoVO(mServicoVO.get());
        mAgendamentoVO.setHorario(mDTO.getHorario());

        return mAgendamentoVO;
    }

    public ResponseEntity<?>isValid(AgendamentoDTO mDTO){
        Optional<ProfissionalVO> mVO = fProfissionalRepository.findById(mDTO.getProfissionalId());
        if (fProfissionalRepository.findById(mDTO.getProfissionalId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum profissional localizado com esse id!")
            );
        }

        if (fServicoRepository.findById(mDTO.getServicoId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum serviço localizado com esse id!")
            );
        }

        if (fUsuarioRepository.findById(mDTO.getUsuarioId()).isEmpty() && (mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE"))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum usuário localizado com esse id!")
            );
        }

        if (!mDTO.getStatus().toString().toUpperCase().matches("CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "No campo stattus informe entre CONFIRMADO|CANCELADO|PENDENTE|CONCLUIDO!")
            );
        }

        if (mDTO.getData().isBefore(LocalDate.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Data não pode ser menor que a atual")
            );
        }

        if (!mDTO.getUsuarioCadastrado().toUpperCase().matches("TRUE|FALSE")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "No campo usuarioCadastrado informe entre TRUE ou FALSE!")
            );
        }

        if (!mDTO.getUsuarioCadastrado().toUpperCase().equals("TRUE") && (mDTO.getNomeUsuario().isEmpty())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", "Quando não for usuario com cadastro preencha o campo 'nomeUsuario'")
            );
        }

        return null;
    }

    public ResponseEntity<?>register(AgendamentoDTO mDTO){
        ResponseEntity<?>mValidacao = isValid(mDTO);
        if (mValidacao != null){
            return mValidacao;
        }

        AgendamentoVO mAgendamentoVO = new AgendamentoVO();
        try {
            mAgendamentoVO = dtoForVo(mDTO, mAgendamentoVO);
            fRepository.save(mAgendamentoVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("Sucesso", "Agendamento cadastrado com sucesso!")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }

    public List<? extends Object> list(){
        List<AgendamentoVO> mProfissionalVO = fRepository.findAll();
        return mProfissionalVO.stream()
                .map(mVO -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nomeProfissional", mVO.getNomeProfissional());
                    map.put("profissionalId", mVO.getProfissionalVO().getId());
                    map.put("usuarioNome", mVO.getNomeUsuario());
                    map.put("usuarioId", mVO.getUsuarioVO().getId());
                    map.put("servico", mVO.getNomeServico());
                    map.put("servicoId", mVO.getServicoVO().getId());
                    map.put("valor", mVO.getValor());
                    map.put("data", mVO.getData());
                    map.put("horario", mVO.getHorario());
                    map.put("status", mVO.getStatus());
                    return map;
                })
                .toList();
    }
}
