package com.example.demo.StartupDataConfig;

import com.example.demo.Agendamento.AgendamentoRepository;
import com.example.demo.Agendamento.AgendamentoVO;
import com.example.demo.Agendamento.EnumAgendamentoStatus;
import com.example.demo.Enum.MessageStatusEnum;
import com.example.demo.Enum.SituacaoOnline;
import com.example.demo.Enum.UserRole;
import com.example.demo.Especialidade.EspecialidadeRepository;
import com.example.demo.Especialidade.EspecialidadeVO;
import com.example.demo.Mensagem.MensagemRepository;
import com.example.demo.Mensagem.MensagemVO;
import com.example.demo.Profissional.ProfissionalRepository;
import com.example.demo.Profissional.ProfissionalVO;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioRepository;
import com.example.demo.ProfissionalHorario.ProfissionalHorarioVO;
import com.example.demo.Servico.ServicoRepository;
import com.example.demo.Servico.ServicoVO;
import com.example.demo.Usuario.UsuarioRepository;
import com.example.demo.Usuario.UsuarioVO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class StartupDataConfig {

    @Bean
    public CommandLineRunner initPortfolioData(
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            EspecialidadeRepository especialidadeRepository,
            ServicoRepository servicoRepository,
            ProfissionalRepository profissionalRepository,
            ProfissionalHorarioRepository horarioRepository,
            AgendamentoRepository agendamentoRepository,
            MensagemRepository mensagemRepository
    ) {
        return args -> {
            UsuarioVO admin = ensureAdmin(usuarioRepository, passwordEncoder);

            System.out.println("🌱 Verificando/carregando seed de portfólio (barbearia)...");

            List<EspecialidadeVO> especialidades = seedEspecialidades(especialidadeRepository);
            List<ServicoVO> servicos = seedServicos(servicoRepository);
            List<ProfissionalVO> profissionais = seedProfissionais(profissionalRepository, especialidades);
            seedHorarios(horarioRepository, profissionais);
            List<UsuarioVO> clientes = seedClientes(usuarioRepository, passwordEncoder);
            seedAgendamentos(agendamentoRepository, clientes, profissionais, servicos);
            seedMensagens(mensagemRepository, admin, clientes);

            System.out.println("✅ Seed de portfólio pronto:");
            System.out.println("   - Especialidades: " + especialidadeRepository.count());
            System.out.println("   - Serviços: " + servicoRepository.count());
            System.out.println("   - Profissionais: " + profissionalRepository.count());
            System.out.println("   - Horários: " + horarioRepository.count());
            System.out.println("   - Usuários: " + usuarioRepository.count());
            System.out.println("   - Agendamentos: " + agendamentoRepository.count());
            System.out.println("   - Mensagens: " + mensagemRepository.count());
            System.out.println("   Login admin: ADM@GMAIL.COM / 123");
            System.out.println("   Login cliente demo: CARLOS.SILVA@EMAIL.COM / 123");
        };
    }

    private UsuarioVO ensureAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        UsuarioVO admin = usuarioRepository.findByEmail("ADM@GMAIL.COM");
        if (admin == null) {
            admin = new UsuarioVO();
            admin.setNome("ADM");
            admin.setCpf("000.000.000-25");
            admin.setCelular("(48) 99996-5858");
            admin.setEmail("ADM@GMAIL.COM");
            admin.setSenha(passwordEncoder.encode("123"));
            admin.setRole(UserRole.ADMIN);
            admin.setOnline(SituacaoOnline.FALSE);
            admin = usuarioRepository.save(admin);
            System.out.println("✅ Admin padrão criado (ADM@GMAIL.COM / 123)");
        } else if (admin.getRole() != UserRole.ADMIN) {
            admin.setRole(UserRole.ADMIN);
            admin = usuarioRepository.save(admin);
            System.out.println("✅ Usuário ADM@GMAIL.COM atualizado para role ADMIN");
        }
        return admin;
    }

    private List<EspecialidadeVO> seedEspecialidades(EspecialidadeRepository repository) {
        if (repository.count() > 0) {
            return repository.findAll();
        }

        List<EspecialidadeVO> lista = new ArrayList<>();
        for (String nome : List.of(
                "CORTE MASCULINO",
                "BARBA",
                "COMBO CORTE E BARBA",
                "COLORAÇÃO",
                "TRATAMENTOS CAPILARES"
        )) {
            EspecialidadeVO esp = new EspecialidadeVO();
            esp.setNome(nome);
            lista.add(repository.save(esp));
        }
        return lista;
    }

    private List<ServicoVO> seedServicos(ServicoRepository repository) {
        if (repository.count() > 0) {
            return repository.findAll();
        }

        record Item(String nome, long duracaoMin, String valor) {}
        List<Item> itens = List.of(
                new Item("CORTE MASCULINO CLASSICO", 30, "45.00"),
                new Item("CORTE DEGRADE", 40, "55.00"),
                new Item("CORTE INFANTIL", 30, "40.00"),
                new Item("BARBA COMPLETA", 30, "35.00"),
                new Item("BARBA E BIGODE", 25, "30.00"),
                new Item("ACABAMENTO DE BARBA", 15, "20.00"),
                new Item("COMBO CORTE + BARBA", 60, "80.00"),
                new Item("COMBO DEGRADE + BARBA", 70, "90.00"),
                new Item("PIGMENTACAO DE BARBA", 45, "70.00"),
                new Item("LUZES MASCULINAS", 90, "120.00"),
                new Item("HIDRATACAO CAPILAR", 40, "65.00"),
                new Item("LIMPEZA DE PELE", 35, "50.00")
        );

        List<ServicoVO> lista = new ArrayList<>();
        for (Item item : itens) {
            ServicoVO servico = new ServicoVO();
            servico.setNome(item.nome());
            servico.setDuracao(item.duracaoMin());
            servico.setValor(new BigDecimal(item.valor()));
            lista.add(repository.save(servico));
        }
        return lista;
    }

    private List<ProfissionalVO> seedProfissionais(
            ProfissionalRepository repository,
            List<EspecialidadeVO> especialidades
    ) {
        if (repository.count() > 0) {
            return repository.findAll();
        }

        EspecialidadeVO corte = especialidades.get(0);
        EspecialidadeVO barba = especialidades.get(1);
        EspecialidadeVO combo = especialidades.get(2);
        EspecialidadeVO coloracao = especialidades.get(3);
        EspecialidadeVO tratamento = especialidades.get(4);

        record Item(String nome, String email, String celular, EspecialidadeVO esp) {}
        List<Item> itens = List.of(
                new Item("RICARDO ALMEIDA", "RICARDO.ALMEIDA@BARBERAPP.COM", "(48) 99111-2201", corte),
                new Item("FELIPE MARTINS", "FELIPE.MARTINS@BARBERAPP.COM", "(48) 99111-2202", barba),
                new Item("BRUNO OLIVEIRA", "BRUNO.OLIVEIRA@BARBERAPP.COM", "(48) 99111-2203", combo),
                new Item("ANDRE COSTA", "ANDRE.COSTA@BARBERAPP.COM", "(48) 99111-2204", coloracao),
                new Item("LUCAS FERREIRA", "LUCAS.FERREIRA@BARBERAPP.COM", "(48) 99111-2205", tratamento),
                new Item("GABRIEL SOUZA", "GABRIEL.SOUZA@BARBERAPP.COM", "(48) 99111-2206", corte)
        );

        List<ProfissionalVO> lista = new ArrayList<>();
        for (Item item : itens) {
            ProfissionalVO profissional = new ProfissionalVO();
            profissional.setNome(item.nome());
            profissional.setEmail(item.email());
            profissional.setCelular(item.celular());
            profissional.setAtivo("TRUE");
            profissional.setEspecialidadeVO(item.esp());
            lista.add(repository.save(profissional));
        }
        return lista;
    }

    private void seedHorarios(
            ProfissionalHorarioRepository repository,
            List<ProfissionalVO> profissionais
    ) {
        if (repository.count() > 0 || profissionais.isEmpty()) {
            return;
        }

        String diasUteis = "SEG,TER,QUA,QUI,SEX";
        String fimDeSemana = "SAB";

        for (int i = 0; i < profissionais.size(); i++) {
            ProfissionalVO profissional = profissionais.get(i);

            ProfissionalHorarioVO manha = new ProfissionalHorarioVO();
            manha.setProfissionalVO(profissional);
            manha.setDiaSemana(diasUteis);
            manha.setHoraInicial(LocalTime.of(9, 0));
            manha.setHoraFinal(LocalTime.of(12, 0));
            repository.save(manha);

            ProfissionalHorarioVO tarde = new ProfissionalHorarioVO();
            tarde.setProfissionalVO(profissional);
            tarde.setDiaSemana(diasUteis);
            tarde.setHoraInicial(LocalTime.of(13, 30));
            tarde.setHoraFinal(LocalTime.of(i % 2 == 0 ? 19 : 18, 0));
            repository.save(tarde);

            if (i < 4) {
                ProfissionalHorarioVO sabado = new ProfissionalHorarioVO();
                sabado.setProfissionalVO(profissional);
                sabado.setDiaSemana(fimDeSemana);
                sabado.setHoraInicial(LocalTime.of(9, 0));
                sabado.setHoraFinal(LocalTime.of(13, 0));
                repository.save(sabado);
            }
        }
    }

    private List<UsuarioVO> seedClientes(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        record Item(String nome, String email, String celular, String cpf) {}
        List<Item> itens = List.of(
                new Item("CARLOS SILVA", "CARLOS.SILVA@EMAIL.COM", "(48) 98801-1001", "111.222.333-01"),
                new Item("MARCOS PEREIRA", "MARCOS.PEREIRA@EMAIL.COM", "(48) 98801-1002", "111.222.333-02"),
                new Item("JOAO SANTOS", "JOAO.SANTOS@EMAIL.COM", "(48) 98801-1003", "111.222.333-03"),
                new Item("PEDRO LIMA", "PEDRO.LIMA@EMAIL.COM", "(48) 98801-1004", "111.222.333-04"),
                new Item("RAFAEL NUNES", "RAFAEL.NUNES@EMAIL.COM", "(48) 98801-1005", "111.222.333-05"),
                new Item("THIAGO ROCHA", "THIAGO.ROCHA@EMAIL.COM", "(48) 98801-1006", "111.222.333-06"),
                new Item("DIEGO ALVES", "DIEGO.ALVES@EMAIL.COM", "(48) 98801-1007", "111.222.333-07"),
                new Item("MATHEUS BARBOSA", "MATHEUS.BARBOSA@EMAIL.COM", "(48) 98801-1008", "111.222.333-08")
        );

        List<UsuarioVO> clientes = new ArrayList<>();
        for (Item item : itens) {
            UsuarioVO existente = repository.findByEmail(item.email());
            if (existente != null) {
                clientes.add(existente);
                continue;
            }
            UsuarioVO cliente = new UsuarioVO();
            cliente.setNome(item.nome());
            cliente.setEmail(item.email());
            cliente.setCelular(item.celular());
            cliente.setCpf(item.cpf());
            cliente.setSenha(passwordEncoder.encode("123"));
            cliente.setRole(UserRole.CLIENT);
            cliente.setOnline(SituacaoOnline.FALSE);
            clientes.add(repository.save(cliente));
        }
        return clientes;
    }

    private void seedAgendamentos(
            AgendamentoRepository repository,
            List<UsuarioVO> clientes,
            List<ProfissionalVO> profissionais,
            List<ServicoVO> servicos
    ) {
        if (repository.count() > 0 || clientes.isEmpty() || profissionais.isEmpty() || servicos.isEmpty()) {
            return;
        }

        LocalDate hoje = LocalDate.now();
        EnumAgendamentoStatus[] statusCiclo = {
                EnumAgendamentoStatus.CONCLUIDO,
                EnumAgendamentoStatus.CONFIRMADO,
                EnumAgendamentoStatus.PENDENTE,
                EnumAgendamentoStatus.CONCLUIDO,
                EnumAgendamentoStatus.CANCELADO,
                EnumAgendamentoStatus.CONFIRMADO
        };

        int[] offsetsDias = {-10, -7, -5, -3, -1, 0, 1, 2, 3, 5, 7, 10, 12, 14};
        int[] horas = {9, 10, 11, 14, 15, 16, 17};

        for (int i = 0; i < 28; i++) {
            UsuarioVO cliente = clientes.get(i % clientes.size());
            ProfissionalVO profissional = profissionais.get(i % profissionais.size());
            ServicoVO servico = servicos.get(i % servicos.size());
            LocalDate data = hoje.plusDays(offsetsDias[i % offsetsDias.length]);
            LocalTime inicio = LocalTime.of(horas[i % horas.length], i % 2 == 0 ? 0 : 30);
            LocalTime fim = inicio.plusMinutes(servico.getDuracao());

            AgendamentoVO agendamento = new AgendamentoVO();
            agendamento.setUsuarioVO(cliente);
            agendamento.setNomeUsuario(cliente.getNome());
            agendamento.setServicoVO(servico);
            agendamento.setNomeServico(servico.getNome());
            agendamento.setProfissionalVO(profissional);
            agendamento.setNomeProfissional(profissional.getNome());
            agendamento.setValor(servico.getValor());
            agendamento.setData(data);
            agendamento.setHorarioIncio(inicio);
            agendamento.setHorarioFim(fim);
            agendamento.setStatus(statusCiclo[i % statusCiclo.length]);
            agendamento.setUsuarioCadastrado("TRUE");
            repository.save(agendamento);
        }
    }

    private void seedMensagens(
            MensagemRepository repository,
            UsuarioVO admin,
            List<UsuarioVO> clientes
    ) {
        if (repository.count() > 0 || admin == null || clientes.isEmpty()) {
            return;
        }

        String[][] conversas = {
                {"Olá! Quero remarcar meu horário de sexta.", "Claro! Qual horário prefere?"},
                {"Vocês atendem no sábado de manhã?", "Sim, das 09h às 13h."},
                {"O Ricardo ainda tem vaga amanhã?", "Vou verificar a agenda e te retorno."},
                {"Obrigado pelo atendimento de hoje!", "Por nada! Até a próxima."}
        };

        for (int i = 0; i < Math.min(clientes.size(), conversas.length); i++) {
            UsuarioVO cliente = clientes.get(i);
            String[] msgs = conversas[i];

            MensagemVO msgCliente = new MensagemVO();
            msgCliente.setSender(cliente);
            msgCliente.setClient(admin);
            msgCliente.setConteudo(msgs[0]);
            msgCliente.setStatus(MessageStatusEnum.READ);
            msgCliente.setCreatedAt(LocalDateTime.now().minusHours(6 - i));
            repository.save(msgCliente);

            MensagemVO msgAdmin = new MensagemVO();
            msgAdmin.setSender(admin);
            msgAdmin.setClient(cliente);
            msgAdmin.setConteudo(msgs[1]);
            msgAdmin.setStatus(i == 0 ? MessageStatusEnum.SENT : MessageStatusEnum.READ);
            msgAdmin.setCreatedAt(LocalDateTime.now().minusHours(5 - i));
            repository.save(msgAdmin);
        }
    }
}
