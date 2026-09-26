# RotaClara

Sistema web para monitoramento de tempo parado em roteiros de entrega. O projeto foi desenvolvido como atividade de Engenharia de Software II e integra um frontend React com uma API REST em Spring Boot e PostgreSQL.

O sistema permite cadastrar motoristas e pontos, montar roteiros diários, registrar chegada e saída em cada ponto, calcular tempo parado e custo estimado, consultar histórico e acompanhar indicadores operacionais.

## Funcionalidades

- Autenticação de usuários com JWT.
- Controle de acesso por perfil: administrador, gerente/coordenador e motorista.
- Cadastro de motoristas e motoboys.
- Cadastro de pontos com endereço e coordenadas.
- Montagem de roteiros por motorista, data e ordem dos pontos.
- Registro de chegada e saída em dispositivo móvel.
- Avanço automático entre roteiros do dia.
- Cálculo de tempo parado por ponto e por roteiro.
- Cálculo de custo estimado por distância e custo por quilômetro.
- Painel com KPIs e gráfico de tempo parado.
- Histórico de pontos e paradas por período e motorista.
- Configuração de custo, jornada padrão e limite de alerta.

## Arquitetura

O projeto está dividido em duas aplicações:

```text
RotaClara/
├── rotaclara-backend/    API REST Java/Spring Boot
├── rotaclara-frontend/   Interface web React/TypeScript
├── Protótipo de telas.html
├── Especificação de Requisitos — Trabalho2.pdf
└── README.md
```

Fluxo principal:

```text
Navegador
   │
   │ HTTP/JSON + Bearer JWT
   ▼
Frontend React/Vite :5173 ou :5174
   │
   │ REST
   ▼
Backend Spring Boot :8080
   │
   ▼
PostgreSQL :5432
```

## Tecnologias

### Backend

- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA/Hibernate
- Spring Security
- JWT com JJWT
- PostgreSQL
- Flyway
- Maven

### Frontend

- React 18
- TypeScript
- Vite
- React Router
- Recharts
- Fetch API

## Pré-requisitos

Instale e deixe disponíveis:

- JDK 17 ou superior
- Maven
- Node.js e npm
- PostgreSQL

O backend espera um banco com estas configurações locais:

```text
Host: localhost
Porta: 5432
Banco: rotaclara
Usuário: rotaclara
Senha: rotaclara
```

Crie o usuário e o banco, caso ainda não existam:

```sql
CREATE USER rotaclara WITH PASSWORD 'rotaclara';
CREATE DATABASE rotaclara OWNER rotaclara;
```

As migrations do Flyway criam as tabelas e os dados de demonstração automaticamente na primeira execução.

## Como executar no Windows

O caminho deste projeto contém o caractere acentuado `á` em `Matérias`. Em alguns ambientes, o launcher forkado do Maven corrompe esse caminho. Para evitar isso, crie uma unidade virtual com caminho ASCII:

```powershell
subst R: "C:\Users\jujuc\OneDrive\Desktop\ESTUDOS\Matérias\ES 2\Projetos\Atividade 2"
```

Se a unidade `R:` já existir, não execute o comando novamente.

### 1. Backend

Abra um terminal e execute:

```powershell
cd R:\rotaclara-backend
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

Mantenha esse terminal aberto.

### 2. Frontend

Abra outro terminal:

```powershell
cd R:\rotaclara-frontend
npm install
npm run dev
```

Acesse a URL informada pelo Vite. Normalmente será:

```text
http://localhost:5173
```

Se a porta 5173 estiver ocupada, o Vite utilizará a porta 5174.

O frontend aceita a URL da API pela variável `VITE_API_URL`. Sem configuração, utiliza:

```text
http://localhost:8080
```

### Alternativa: executar pelo JAR

Se o `spring-boot:run` apresentar problema de caminho, execute pelo JAR:

```powershell
cd R:\rotaclara-backend
mvn package -DskipTests
java -jar target\rotaclara-backend-0.1.0.jar
```

## Usuários de demonstração

| Usuário | Senha | Perfil |
|---|---|---|
| `administrador` | `admin123` | Administrador |
| `ana.souza` | `gerente123` | Gerente/coordenadora |
| `carlos.mendes` | `motorista123` | Motorista |

## Principais endpoints

Todas as rotas abaixo exigem o header `Authorization: Bearer <token>`, exceto o login e o health check.

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/login` | Autenticação e emissão de JWT |
| GET/POST | `/motoristas` | Listagem e cadastro de motoristas |
| GET/POST | `/pontos` | Listagem e cadastro de pontos |
| GET/PUT | `/parametros` | Consulta e atualização de parâmetros |
| GET | `/roteiros?inicio=&fim=` | Lista roteiros por período |
| GET | `/roteiros/{id}` | Consulta um roteiro |
| POST | `/roteiros` | Cria um roteiro |
| POST | `/roteiros/{roteiroId}/pontos/{pontoRoteiroId}/chegada` | Registra chegada |
| POST | `/roteiros/{roteiroId}/pontos/{pontoRoteiroId}/saida` | Registra saída |
| GET | `/dashboard?inicio=&fim=` | Retorna KPIs e dados do painel |
| GET | `/actuator/health` | Verifica a saúde da aplicação |

## Testes

### Backend

```powershell
cd R:\rotaclara-backend
mvn test
```

### Frontend

```powershell
cd R:\rotaclara-frontend
npm run lint
npm run build
```

## Verificação rápida da API

Para confirmar que o backend está escutando na porta correta:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen
```

Para confirmar que o login está funcionando:

```powershell
$res = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/auth/login" `
  -ContentType "application/json" `
  -Body '{"login":"administrador","senha":"admin123"}'

$res
```

Se a porta 8080 estiver ocupada, não inicie uma segunda instância. Isso normalmente significa que o backend já está rodando.

## Regras de negócio implementadas

- O ponto de partida não contabiliza tempo parado.
- O tempo parado é calculado pela diferença entre chegada e saída.
- O tempo total do roteiro soma os tempos dos pontos, exceto a partida.
- O custo estimado utiliza distância e custo por quilômetro.
- Um motorista não pode possuir dois roteiros na mesma data.
- A ordem dos pontos deve ser sequencial, começando em 1.
- O ponto acima do limite configurado aparece como alerta.

## Solução de problemas

### Erro `ClassNotFoundException` no `mvn spring-boot:run`

Execute o Maven pela unidade virtual `R:` descrita na seção de execução. Esse erro ocorre quando o caminho com acento é corrompido no classpath do processo Java.

### Erro `Port 8080 was already in use`

Verifique o processo responsável:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen
```

Se o backend já estiver rodando, apenas reutilize a instância existente. Para encerrá-la, use `Ctrl+C` no terminal correspondente.

### Erro 403 no frontend

Confirme que:

1. O backend está rodando na porta 8080.
2. O frontend foi aberto em `localhost:5173` ou `localhost:5174`.
3. Você saiu e entrou novamente para renovar o JWT.
4. A configuração CORS do backend foi recarregada após alterações.

### O frontend não atualiza

Use `Ctrl+F5` no navegador ou reinicie o Vite com:

```powershell
npm run dev
```

## Limitações conhecidas

- A exportação de relatório ainda é apenas um botão de demonstração.
- As listagens não possuem paginação, pois o projeto está dimensionado como MVP.
- O banco PostgreSQL precisa estar disponível localmente antes de iniciar o backend.
- A chave JWT presente no arquivo de configuração deve ser substituída em produção.

## Documentação complementar

- [README do backend](rotaclara-backend/README.md)
- [README do frontend](rotaclara-frontend/README.md)
- [Especificação de Requisitos](Especificação%20de%20Requisitos%20—%20Trabalho2.pdf)
- [Protótipo de telas](Protótipo%20de%20telas.html)
