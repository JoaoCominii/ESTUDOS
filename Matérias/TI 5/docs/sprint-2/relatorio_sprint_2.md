# Relatório de Entrega - Sprint 2
**Matéria:** TI 5  
**Objetivo:** Implementar e validar um primeiro ambiente funcional de monitoramento, capaz de executar uma carga de I/O em máquina Linux, coletar métricas de desempenho de armazenamento e disponibilizá-las em um servidor central por meio do Zabbix.

---

## 1. Arquitetura da Solução

- **Servidor Central de Monitoramento:**
  - Instância do **Zabbix Server 7.0 LTS** executando em container Docker.
  - Banco de Dados **PostgreSQL 16** para persistência histórica.
  - Frontend Web **Nginx** para visualização gráfica e dashboards.
- **Ambiente de Testes (Agente):**
  - Máquina Linux (Ubuntu via WSL 2).
  - Gerador de carga sintética: **fio** (Flexible I/O Tester).
  - Parser de telemetria em formato JSON integrado ao **`zabbix_sender`**.
  - Canal de comunicação: protocolo nativo do Zabbix (TCP porta `10051`) via itens do tipo **Trapper**.

---

## 2. Metodologia dos Experimentos

Foram realizados dois regimes de carga distintos para comprovar a sensibilidade do sistema de monitoramento:

1. **Perfil A — Leitura Sequencial (4 KB):**
   - Bloco: 4 KB
   - Modo de acesso: Sequencial (`rw=read`)
   - Duração: 5 segundos
2. **Perfil B — Leitura Aleatória (4 KB):**
   - Bloco: 4 KB
   - Modo de acesso: Aleatório (`rw=randread`)
   - Duração: 5 segundos

---

## 3. Resultados Preliminares Coletados

Os testes foram executados de ponta a ponta e as métricas foram enviadas ao Zabbix:

| Métrica | Perfil A (Sequencial) | Perfil B (Aleatório) | Unidade |
| :--- | :--- | :--- | :--- |
| **IOPS** | 695.512,60 | 607.999,40 | ops/s |
| **Throughput** | 2.716,85 | 2.375,00 | MB/s |
| **Latência Média** | 0,0012 | 0,0012 | ms |
| **Latência (p99)** | 0,0044 | 0,0041 | ms |
| **Latência (p99.9)** | 0,0381 | 0,0291 | ms |

### Conclusão Preliminar
O pipeline comprovou que:
> *"Quando mudamos o regime de carga, nosso sistema de monitoramento é capaz de observar de forma determinística e em tempo real a variação nas métricas de desempenho do armazenamento."*

---

## 4. Evidências do Repositório

- Arquivos de saída bruta do benchmark:
  - [`docs/experiments/fio-test-01.json`](../experiments/fio-test-01.json)
  - [`docs/experiments/fio-test-02.json`](../experiments/fio-test-02.json)
- Tabelas consolidadas:
  - [`docs/results/experiment-comparison.csv`](../results/experiment-comparison.csv)
  - [`docs/results/experiment-01.csv`](../results/experiment-01.csv)
  - [`docs/results/experiment-02.csv`](../results/experiment-02.csv)
- Configuração do Zabbix:
  - [`docker-compose.yml`](../../docker-compose.yml)
  - [`zabbix_template_storage_benchmark.yaml`](../../zabbix_template_storage_benchmark.yaml)
  - [`run_benchmark.sh`](../../run_benchmark.sh)
