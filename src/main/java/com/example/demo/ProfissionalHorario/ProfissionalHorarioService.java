package com.example.demo.ProfissionalHorario;

import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.Util.ApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProfissionalHorarioService {
    @Autowired
    private ProfissionalRepository fProfissionalRepository;

    @Autowired
    private ProfissionalHorarioRepository fRepository;

    public ResponseEntity<?>register(ProfissionalHorarioDTO mDTO){
        String mDiasSemanas = String.join(",", mDTO.getDiaSemana());

        Optional<ProfissionalVO> mProfissionalVO = fProfissionalRepository.findById(mDTO.getProfissionalId());
        if (mProfissionalVO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponseUtil.response("Erro", "Nenhum profissional localizado com esse id!")
            );
        }

        if (fRepository.existsByProfissionalVO_IdAndDiaSemanaAndHoraInicialAndHoraFinal(
                mDTO.getProfissionalId(), mDiasSemanas, mDTO.getHoraInicio(), mDTO.getHoraFinal()
        )){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponseUtil.response("Erro", "Já existe horário agendado para esse profissional nessa data e horário")
            );
        }

        ProfissionalHorarioVO mProfissionalHorarioVO = new ProfissionalHorarioVO();
        mProfissionalHorarioVO.setProfissionalVO(mProfissionalVO.get());
        mProfissionalHorarioVO.setDiaSemana(mDiasSemanas);
        mProfissionalHorarioVO.setHoraFinal(mDTO.getHoraFinal());
        mProfissionalHorarioVO.setHoraInicial(mDTO.getHoraInicio());
        try {
            fRepository.save(mProfissionalHorarioVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponseUtil.response("OK", "Registro incluido com sucesso!")
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponseUtil.response("Erro", e.getMessage())
            );
        }
    }
}
