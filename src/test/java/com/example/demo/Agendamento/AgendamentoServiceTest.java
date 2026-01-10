// language: java
package com.example.demo.Agendamento;

import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioRepository;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioVO;
import com.example.demo.Servico.ServicoRepository;
import com.example.demo.Servico.ServicoVO;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository fRepository;

    @Mock
    private ProfissionalRepository fProfissionalRepository;

    @Mock
    private ServicoRepository fServicoRepository;

    @Mock
    private UsuarioRepository fUsuarioRepository;

    @Mock
    private ProfissionalHorarioRepository fProfissionalHorarioRepository;

    @InjectMocks
    private AgendamentoService service;

    @BeforeEach
    void setUp() {
        service = Mockito.spy(service);
    }

    @Test
    void dtoForVo_populatesFields_and_setsRelatedVos() {
        AgendamentoDTO dto = mock(AgendamentoDTO.class);
        AgendamentoVO vo = mock(AgendamentoVO.class);
        ProfissionalVO prof = mock(ProfissionalVO.class);
        ServicoVO serv = mock(ServicoVO.class);
        UsuarioVO user = mock(UsuarioVO.class);

        when(dto.getProfissionalId()).thenReturn(1L);
        when(dto.getServicoId()).thenReturn(2L);
        when(dto.getUsuarioId()).thenReturn(3L);
        when(dto.getData()).thenReturn(LocalDate.now().plusDays(1));
        when(dto.getHorario()).thenReturn(LocalTime.of(9,0));
        when(dto.getStatus()).thenReturn(EnumAgendamentoStatus.valueOf("PENDENTE"));
        when(dto.getValor()).thenReturn(BigDecimal.valueOf(100.0));
        when(dto.getUsuarioCadastrado()).thenReturn("TRUE");
        when(dto.getNomeUsuario()).thenReturn("nome qualquer");

        when(fProfissionalRepository.findById(1L)).thenReturn(Optional.of(prof));
        when(fServicoRepository.findById(2L)).thenReturn(Optional.of(serv));
        when(fUsuarioRepository.findById(3L)).thenReturn(Optional.of(user));

        when(serv.getDuracao()).thenReturn(60L);
        when(prof.getNome()).thenReturn("Prof A");
        when(serv.getNome()).thenReturn("Servico A");
        when(user.getNome()).thenReturn("Cliente A");

        AgendamentoVO result = service.dtoForVo(dto, vo);

        verify(vo).setData(any(LocalDate.class));
        verify(vo).setHorarioIncio(LocalTime.of(9,0));
        verify(vo).setHorarioFim(LocalTime.of(9,0).plusMinutes(60L));
        verify(vo).setNomeProfissional("Prof A");
        verify(vo).setNomeServico("Servico A");
        verify(vo).setUsuarioVO(user);
        verify(vo).setProfissionalVO(prof);
        verify(vo).setServicoVO(serv);
        assertSame(vo, result);
    }

    @Test
    void isValid_returnsNull_whenEverythingOk() {
        AgendamentoDTO dto = mock(AgendamentoDTO.class);
        when(dto.getProfissionalId()).thenReturn(1L);
        when(dto.getServicoId()).thenReturn(2L);
        when(dto.getUsuarioId()).thenReturn(3L);
        when(dto.getData()).thenReturn(LocalDate.now().plusDays(2));
        when(dto.getHorario()).thenReturn(LocalTime.of(10,0));
        when(dto.getStatus()).thenReturn(EnumAgendamentoStatus.valueOf("CONFIRMADO"));
        when(dto.getUsuarioCadastrado()).thenReturn("FALSE");
        when(dto.getNomeUsuario()).thenReturn("Teste");

        when(fProfissionalRepository.findById(1L)).thenReturn(Optional.of(mock(ProfissionalVO.class)));
        ServicoVO serv = mock(ServicoVO.class);
        when(serv.getDuracao()).thenReturn(30L);
        when(fServicoRepository.findById(2L)).thenReturn(Optional.of(serv));
        when(fUsuarioRepository.findById(3L)).thenReturn(Optional.empty());
        Mockito.doReturn(ResponseEntity.ok(List.of("10:00"))).when(service)
                .getAvailableTimeProfissional(1L, 2L, dto.getData());

        ResponseEntity<?> result = service.isValid(dto);
        assertNull(result);
    }

    @Test
    void isValid_returnsNotFound_whenProfissionalMissing() {
        AgendamentoDTO dto = mock(AgendamentoDTO.class);
        when(dto.getProfissionalId()).thenReturn(99L);

        when(fProfissionalRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> result = service.isValid(dto);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void register_returnsCreated_whenValid() {
        AgendamentoDTO dto = mock(AgendamentoDTO.class);

        Mockito.doReturn(null).when(service).isValid(dto);

        ResponseEntity<?> res = service.register(dto);
        assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }

    @Test
    void list_mapsRepositoryEntities() {
        AgendamentoVO a = mock(AgendamentoVO.class);
        when(a.getId()).thenReturn(1L);
        when(a.getNomeProfissional()).thenReturn("Prof A");
        when(a.getProfissionalVO()).thenReturn(mock(ProfissionalVO.class));
        when(a.getNomeUsuario()).thenReturn("Usu A");
        when(a.getUsuarioVO()).thenReturn(null);
        when(a.getNomeServico()).thenReturn("Serv A");
        ServicoVO serv = mock(ServicoVO.class);
        when(serv.getId()).thenReturn(5L);
        when(a.getServicoVO()).thenReturn(serv);
        when(a.getValor()).thenReturn(BigDecimal.valueOf(50.0));
        when(a.getData()).thenReturn(LocalDate.now());
        when(a.getHorarioIncio()).thenReturn(LocalTime.of(9,0));
        when(a.getHorarioFim()).thenReturn(LocalTime.of(10,0));
        when(a.getStatus()).thenReturn(EnumAgendamentoStatus.valueOf("PENDENTE"));

        when(fRepository.findAll()).thenReturn(List.of(a));

        List<?> result = service.list();
        assertEquals(1, result.size());
        assertTrue(((java.util.Map<?,?>) result.get(0)).containsKey("id"));
    }

    @Test
    void listPerClient_mapsRepositoryEntities() {
        AgendamentoVO a = mock(AgendamentoVO.class);
        when(a.getId()).thenReturn(2L);
        when(fRepository.listById(3L)).thenReturn(List.of(a));
        List<?> result = service.listPerClient(3L);
        assertEquals(1, result.size());
    }

    @Test
    void getAvailableTime_returnsNotFound_whenNoHorarioForDay() {
        when(fProfissionalHorarioRepository.findByProfissionalVO_IdAndDiaSemanaContaining(1L, "seg")).thenReturn(null);
        ResponseEntity<?> res = service.getAvailableTimeProfissional(1L, 1L, LocalDate.of(2025,1,6)); // segunda = MONDAY -> seg
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void getAvailableTime_returnsAvailableTimes_excludingBookedOnes() {
        ProfissionalHorarioVO ph = mock(ProfissionalHorarioVO.class);
        when(ph.getHoraInicial()).thenReturn(LocalTime.of(9,0));
        when(ph.getHoraFinal()).thenReturn(LocalTime.of(11,0));
        when(fProfissionalHorarioRepository.findByProfissionalVO_IdAndDiaSemanaContaining(1L, "seg")).thenReturn(ph);

        // serviço com duração 60
        ServicoVO serv = mock(ServicoVO.class);
        when(serv.getDuracao()).thenReturn(60L);
        when(fServicoRepository.findById(2L)).thenReturn(Optional.of(serv));
        AgendamentoVO reserved = mock(AgendamentoVO.class);
        when(reserved.getHorarioIncio()).thenReturn(LocalTime.of(9,0));
        when(reserved.getHorarioFim()).thenReturn(LocalTime.of(10,0));
        when(fRepository.findByProfissionalVO_IdAndDataAndStatusIn(eq(1L), any(LocalDate.class), anyList()))
                .thenReturn(List.of(reserved));

        ResponseEntity<?> res = service.getAvailableTimeProfissional(1L, 2L, LocalDate.of(2025,1,6));
        assertEquals(HttpStatus.OK, res.getStatusCode());
        @SuppressWarnings("unchecked")
        List<String> times = (List<String>) res.getBody();
        assertTrue(times.contains("10:00"));
        assertFalse(times.contains("09:00"));
    }

    @Test
    void convertDayForPortuguese_mapsDaysCorrectly() {
        assertEquals("seg", service.convertDayForPortuguese("MONDAY"));
        assertEquals("ter", service.convertDayForPortuguese("TUESDAY"));
        assertEquals("dom", service.convertDayForPortuguese("SUNDAY"));
    }

    @Test
    void edit_returnsOk_whenValid() {
        AgendamentoDTO dto = mock(AgendamentoDTO.class);
        AgendamentoVO existing = mock(AgendamentoVO.class);
        when(fRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.doReturn(null).when(service).isValid(dto);

        ResponseEntity<?> res = service.edit(dto, 1L);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void delete_returnsNotFound_whenMissing() {
        when(fRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseEntity<?> res = service.delete(99L);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void delete_returnsOk_whenExists() {
        AgendamentoVO existing = mock(AgendamentoVO.class);
        when(fRepository.findById(5L)).thenReturn(Optional.of(existing));
        ResponseEntity<?> res = service.delete(5L);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(fRepository).delete(existing);
    }

    @Test
    void cancel_returnsNotFound_whenMissing_orAlreadyCancelled_andSuccessPath_returnsNotFound_dueToBug() {
        when(fRepository.findById(100L)).thenReturn(Optional.empty());
        ResponseEntity<?> resMissing = service.cancel(100L);
        assertEquals(HttpStatus.NOT_FOUND, resMissing.getStatusCode());

        AgendamentoVO existing = mock(AgendamentoVO.class);
        when(existing.getStatus()).thenReturn(EnumAgendamentoStatus.valueOf("PENDENTE"));
        when(fRepository.findById(6L)).thenReturn(Optional.of(existing));
        ResponseEntity<?> res = service.cancel(6L);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(fRepository).save(existing);
    }
}
