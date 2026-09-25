# RotaClara — Back-end

API REST do MVP de monitoramento de tempo parado em roteiros
(Engenharia de Software II, PUC Minas), em **Java 17 + Spring Boot 3 +
PostgreSQL**.
## Como rodar


**1. Rodar a aplicação:**

```bash
mvn spring-boot:run
```

Na primeira execução, o Flyway cria o schema automaticamente
(`V1__init.sql`) e insere dados de exemplo (`V2__seed_exemplo.sql`) —
os mesmos motoristas e o "Roteiro A" usados no protótipo.

A API sobe em `http://localhost:8080`.

**2. Testar o login:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"administrador","senha":"admin123"}'
```

Outros logins de exemplo: `ana.souza` / `gerente123` (perfil GERENTE),
`carlos.mendes` / `motorista123` (perfil MOTORISTA).

## Rodar os testes

```bash
mvn test
```

Os testes em `RegrasNegocioServiceTest` e `RoteiroServiceTest` não
dependem de banco de dados (são testes unitários com JUnit 5 +
Mockito), então rodam sem precisar do PostgreSQL no ar.

## Estrutura

```
src/main/java/com/rotaclara/
  domain/           Entidades JPA (Motorista, GerenteCoordenador, Ponto,
                     Roteiro, PontoRoteiro, Parametro, Usuario)
  repository/       Interfaces Spring Data JPA
  service/          Regras de negócio (RegrasNegocioService = RN01-RN07),
                     orquestração (RoteiroService), dashboard, auth
  security/         JWT (emissão, validação, filtro) e UserDetailsService
  config/           SecurityConfig (CORS + autorização por perfil, RNF04)
  web/dto/          Records de request/response
  web/controller/   Endpoints REST
  exception/        Exceções de domínio + handler global

src/main/resources/
  application.yml
  db/migration/     V1__init.sql (schema), V2__seed_exemplo.sql (dados de exemplo)
```

## Principais endpoints

| Método | Rota | Requisito | Descrição |
|---|---|---|---|
| POST | `/auth/login` | RNF04 | Login, retorna JWT |
| GET/POST | `/motoristas` | RF01 | Cadastro de motoristas |
| GET/POST | `/gerentes` | RF02 | Cadastro de gerentes/coordenadores |
| GET/POST | `/pontos` | RF03 | Catálogo de pontos (endereço + coordenadas) |
| POST | `/roteiros` | RF04 | Monta o roteiro diário (valida RN05, RN06) |
| GET | `/roteiros?inicio=&fim=` | RF07 | Histórico por período |
| POST | `/roteiros/{id}/pontos/{id}/chegada` | RF05 | Registra chegada (RN01) |
| POST | `/roteiros/{id}/pontos/{id}/saida` | RF05 | Registra saída, calcula tempo parado (RN02, RN03) |
| GET | `/dashboard?inicio=&fim=` | RF08 | KPIs + gráfico + lista de roteiros do período |
| GET/PUT | `/parametros` | RF09, RF10 | Custo de combustível, custo/km, jornada, limite de alerta |

Todas as rotas exceto `/auth/login` exigem `Authorization: Bearer <token>`.

## Decisões de modelagem que vale explicar na banca

- **`Ponto` foi separado em duas entidades**: `Ponto` (catálogo
  reutilizável de endereço/coordenadas, RF03) e `PontoRoteiro`
  (ocorrência de um ponto dentro de um roteiro específico, com ordem,
  chegada, saída e tempo parado — RF04/RF05/RF06). A especificação
  original descreve os dois como um único conceito; a normalização
  evita recadastrar o mesmo endereço toda vez que um roteiro passa por
  ele de novo.
- **`tempoTotalParadoMinutos` e `custoEstimado` em `Roteiro` são campos
  de cache**, recalculados por `RegrasNegocioService` sempre que um
  ponto é atualizado. Isso é o que permite o dashboard responder rápido
  mesmo consultando muitos roteiros de uma vez (RNF03).
- **`Parametro` é um singleton lógico** (id fixo `"GLOBAL"`): sempre
  existe uma única linha, criada com valores padrão na primeira
  consulta caso ainda não exista.
- **Perfis de acesso (RNF04)** ficam em uma entidade `Usuario`
  separada dos dados operacionais de `Motorista`/`GerenteCoordenador`,
  porque nem todo motorista/gerente precisa necessariamente de login,
  e o perfil `ADMINISTRADOR` não tem cadastro operacional próprio na
  especificação.

## Pendências conhecidas

- Sem paginação nas listagens (`/motoristas`, `/pontos`) — ok para o
  volume esperado de um MVP, mas seria o primeiro ponto a melhorar.
- RNF05 (auditoria de alterações) ainda não implementada — um próximo
  passo natural seria Hibernate Envers ou uma tabela de log genérica.
- RF12 (exportar relatório) ainda não tem endpoint dedicado.
- CORS está liberado só para `http://localhost:5173` — ajustar
  `rotaclara.cors.origem-permitida` ao publicar o front-end em outro
  domínio.
