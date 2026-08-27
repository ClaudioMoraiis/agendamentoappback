# Agendamento API

API REST de agendamento para barbearia/salão. Java + Spring Boot.

**Front:** https://agendamento.claudiomoraisdev.com.br  
**API (prod):** https://api-agendamento.claudiomoraisdev.com.br

## Stack

- Java 17
- Spring Boot 3.5
- MySQL
- JWT
- WebSocket (chat)
- Swagger: `/swagger-ui.html`

## Rodar local

Pré-requisitos: Java 17, MySQL, Maven.

1. Crie o banco `agendamentoAPP` (ou deixe o MySQL criar).
2. Ajuste `src/main/resources/application.properties` se precisar (user/senha do MySQL).
3. Suba a API:

```bash
./mvnw spring-boot:run
```

Porta padrão: **8081**

Na primeira subida, o seed cria admin, clientes, serviços, profissionais, horários, agendamentos e mensagens de exemplo.

## Logins demo

| Perfil  | Email                    | Senha |
|---------|--------------------------|-------|
| Admin   | ADM@GMAIL.COM            | 123   |
| Cliente | CARLOS.SILVA@EMAIL.COM   | 123   |

## Principais rotas

| Prefixo               | Uso                          |
|-----------------------|------------------------------|
| `/usuario`            | login, cadastro, senha       |
| `/agendamento`        | agendamentos e horários      |
| `/servico`            | serviços                     |
| `/profissional`       | profissionais                |
| `/profissional-horario` | horários de atendimento    |
| `/especialidade`      | especialidades               |
| `/mensagem`           | chat                         |
| `/ws-chat`            | WebSocket                    |

Rotas autenticadas exigem header: `Authorization: Bearer {token}`

## Configuração

Arquivo: `application.properties`

| Propriedade | Descrição |
|-------------|-----------|
| `server.port` | Porta da API (8081) |
| `spring.datasource.*` | MySQL |
| `api.security.token.secret` | Secret do JWT |
| `app.frontend.reset-url` | URL do front para e-mail de senha |
| `spring.mail.*` | SMTP (recuperação de senha) |

Variáveis de ambiente aceitas para e-mail: `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`, `SPRING_MAIL_FROM`.

## Tunnel (cloudflared)

Para expor local na internet:

```bash
cloudflared tunnel --url http://localhost:8081
```

Aponte o front para a URL gerada em `constants/api.js` (front) ou use domínio fixo no tunnel.

## Repo do front

https://github.com/ClaudioMoraiis/AgendamentoAPP
