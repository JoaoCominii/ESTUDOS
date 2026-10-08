# TI 5 - Sprint 2: Ambiente de Monitoramento de Armazenamento com Zabbix

Este repositório contém a infraestrutura reproduzível, o template de métricas e a automação de testes para a **Sprint 2** da disciplina de **TI 5**.

O objetivo desta sprint é implementar e validar um primeiro ambiente funcional de monitoramento capaz de executar uma carga de I/O em uma máquina Linux, coletar métricas de desempenho de armazenamento (via `fio`) e disponibilizá-las em um servidor central por meio do **Zabbix** utilizando itens do tipo *Trapper* (`zabbix_sender`).

---

## 1. Arquitetura da Solução

```
+------------------------------------+             +-------------------------------------+
|      AGENTE / HOST LINUX (WSL)     |             |       SERVIDOR CENTRAL ZABBIX       |
|                                    |             |                                     |
|  [fio] (Carga Sequencial/Aleatória)|             |  [Zabbix Server 7.0 LTS] (Port 10051|
|                 │                  |             |                  ▲                  |
|                 ▼                  |             |                  │                  |
|          Resultado JSON            |             |                  ▼                  |
|                 │                  |   TCP 10051 |         [PostgreSQL 16 DB]          |
|                 ▼                  |  (Trapper)  |                  ▲                  |
|       [run_benchmark.sh]           |────────────►|                  │                  |
|         (parser com jq)            |             |                  ▼                  |
|                 │                  |             |   [Zabbix Web UI] (Port 8080)       |
|                 ▼                  |             |     (Dashboards, Histórico, Gráficos)
|          [zabbix_sender]           |             +-------------------------------------+
+------------------------------------+
```

### Portas e Serviços
| Serviço | Porta | Protocolo | Descrição |
| :--- | :--- | :--- | :--- |
| **Zabbix Web UI** | `8080` | HTTP | Interface gráfica de monitoramento e dashboards |
| **Zabbix Server** | `10051` | TCP | Recepção de dados via `zabbix_sender` (Trapper) e agentes |
| **PostgreSQL DB** | `5432` | TCP | Banco de dados relacional interno dos containers |

---

## 2. Estrutura do Repositório

```text
.
├── docker-compose.yml                     # Subida do Zabbix Server, PostgreSQL e Web Nginx
├── .env.example                           # Exemplo de variáveis de ambiente do banco
├── zabbix_template_storage_benchmark.yaml # Template Zabbix 7.0 com chaves Trapper e gráficos
├── run_benchmark.sh                       # Script do experimento real (fio + parser + zabbix_sender)
├── test_zabbix_sender.sh                  # Script de teste rápido do canal de telemetria
├── README.md                              # Guia completo do projeto e reprodução
└── docs/
    ├── architecture/                      # Diagramas da arquitetura
    ├── experiments/                       # Saídas brutas em JSON do fio
    │   ├── fio-test-01.json               # Perfil A: Leitura Sequencial
    │   └── fio-test-02.json               # Perfil B: Leitura Aleatória
    ├── results/                           # Resultados consolidados em CSV
    │   ├── experiment-comparison.csv      # Tabela comparativa dos regimes de carga
    │   ├── experiment-01.csv              # Métricas isoladas do Experimento 01
    │   └── experiment-02.csv              # Métricas isoladas do Experimento 02
    ├── screenshots/                       # Prints do Zabbix (hosts, gráficos e dashboards)
    └── sprint-2/
        └── relatorio_sprint_2.md          # Relatório acadêmico da Sprint 2
```

---

## 3. Como Subir o Servidor Zabbix (Ambiente Reproduzível)

### Pré-requisitos
- Docker e Docker Compose instalados (ou Docker Desktop no Windows com WSL 2).

### Execução
1. Suba os containers do servidor:
   ```bash
   docker compose up -d
   ```
2. Verifique o status dos serviços:
   ```bash
   docker compose ps
   ```
3. Acesse a interface web:
   - **URL:** [http://localhost:8080](http://localhost:8080)
   - **Usuário padrão:** `Admin`
   - **Senha padrão:** `zabbix`

---

## 4. Configuração no Zabbix Web

### Passo A: Importar o Template de Métricas
1. Acesse **Data collection** → **Templates**.
2. Clique no botão **Import** (canto superior direito).
3. Selecione o arquivo [`zabbix_template_storage_benchmark.yaml`](./zabbix_template_storage_benchmark.yaml) e confirme.
   - Isso cadastra automaticamente todas as chaves Trapper e os gráficos de IOPS, banda e latências.

### Passo B: Cadastrar o Host do Agente
1. Acesse **Data collection** → **Hosts** → **Create host**.
2. Preencha:
   - **Host name:** `agente-storage-01` *(deve ser idêntico ao configurado nos scripts)*
   - **Templates:** Vincule `Template Storage Benchmark FIO`
   - **Host groups:** Selecione `Linux servers`
3. Clique em **Add**.

---

## 5. Execução do Benchmark e Coleta de Métricas

### Pré-requisitos na Máquina do Agente (Linux / WSL)
Instale as dependências necessárias:
```bash
sudo apt update && sudo apt install -y fio zabbix-sender jq
```

### Executar o Experimento Completo
Execute o pipeline automatizado:
```bash
chmod +x run_benchmark.sh
./run_benchmark.sh localhost 10051 agente-storage-01
```

O script executa automaticamente:
1. **Perfil A (Leitura Sequencial 4KB):** Roda o `fio`, salva o JSON, extrai métricas com `jq` e envia ao Zabbix.
2. **Perfil B (Leitura Aleatória 4KB):** Roda o `fio`, salva o JSON, extrai métricas com `jq` e envia ao Zabbix.
3. **Consolidação:** Gera a tabela comparativa em `docs/results/experiment-comparison.csv`.

---

## 6. Resultados Obtidos na Sprint 2

| Métrica | Origem | Chave Zabbix | Perfil A (Sequencial) | Perfil B (Aleatório) | Unidade |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **IOPS** | fio / agente | `storage.iops` | 695.512,60 | 607.999,40 | ops/s |
| **Throughput** | fio / agente | `storage.bandwidth` | 2.716,85 | 2.375,00 | MB/s |
| **Latência Média** | fio / agente | `storage.latency.avg` | 0,0012 | 0,0012 | ms |
| **Latência (p99)** | fio / agente | `storage.latency.p99` | 0,0044 | 0,0041 | ms |
| **Latência (p99.9)**| fio / agente | `storage.latency.p999`| 0,0381 | 0,0291 | ms |

---

## 7. Definition of Done (DoD) Atendidas

- [x] **EPIC 1 (Ambiente):** Arquitetura documentada, portas/IPs descritos e ambiente conteinerizado via Docker.
- [x] **EPIC 2 (Agente):** Carga gerada com perfis sequencial e aleatório via `fio`.
- [x] **EPIC 3 (Métricas):** Modelo de métricas de I/O definido e padronizado.
- [x] **EPIC 4 (Integração Zabbix):** Itens Trapper criados e dados enviados com sucesso via `zabbix_sender`.
- [x] **EPIC 5 (Primeiro Experimento):** Testes executados comprovando a sensibilidade das métricas sob diferentes perfis.
- [x] **EPIC 6 (Evidências):** Estrutura `docs/` populada com JSONs brutos, tabelas CSV e relatório acadêmico.
