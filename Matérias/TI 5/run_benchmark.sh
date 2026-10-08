#!/usr/bin/env bash
# ==============================================================================
# Script de Execução de Benchmark e Envio ao Zabbix - Sprint 2
# Projeto TI 5: Monitoramento de Desempenho de Armazenamento
# ==============================================================================

set -euo pipefail

ZABBIX_SERVER="${1:-localhost}"
ZABBIX_PORT="${2:-10051}"
HOST_NAME="${3:-agente-storage-01}"
TEST_DIR="/tmp/fio_benchmarks"

mkdir -p "${TEST_DIR}"
mkdir -p "docs/experiments"
mkdir -p "docs/results"

echo "=================================================================="
echo "    Iniciando Pipeline de Benchmark e Monitoramento (Sprint 2)    "
echo " Servidor Zabbix : ${ZABBIX_SERVER}:${ZABBIX_PORT}"
echo " Host de Destino : ${HOST_NAME}"
echo "=================================================================="

# Função para enviar métrica individual via zabbix_sender
send_metric() {
    local key="$1"
    local value="$2"
    zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "${key}" -o "${value}" > /dev/null 2>&1 || true
}

# ==============================================================================
# 1. PERFIL A — Leitura Sequencial (4 KB)
# ==============================================================================
echo ""
echo ">> [1/2] Executando Perfil A: Leitura Sequencial (4 KB, iodepth=1, runtime=5s)..."

fio --name=perfil_a_seq \
    --directory="${TEST_DIR}" \
    --rw=read \
    --bs=4k \
    --size=64M \
    --runtime=5 \
    --time_based \
    --ioengine=sync \
    --output-format=json \
    --output="docs/experiments/fio-test-01.json"

# Extração de métricas com jq
IOPS_A=$(jq -r '.jobs[0].read.iops' "docs/experiments/fio-test-01.json")
BW_BYTES_A=$(jq -r '.jobs[0].read.bw_bytes' "docs/experiments/fio-test-01.json")
BW_MB_A=$(awk -v bw="$BW_BYTES_A" 'BEGIN { printf "%.2f", bw / 1048576 }')
LAT_NS_A=$(jq -r '.jobs[0].read.lat_ns.mean' "docs/experiments/fio-test-01.json")
LAT_MS_A=$(awk -v lat="$LAT_NS_A" 'BEGIN { printf "%.4f", lat / 1000000 }')
P99_NS_A=$(jq -r '.jobs[0].read.clat_ns.percentile["99.000000"] // 0' "docs/experiments/fio-test-01.json")
P99_MS_A=$(awk -v lat="$P99_NS_A" 'BEGIN { printf "%.4f", lat / 1000000 }')
P999_NS_A=$(jq -r '.jobs[0].read.clat_ns.percentile["99.900000"] // 0' "docs/experiments/fio-test-01.json")
P999_MS_A=$(awk -v lat="$P999_NS_A" 'BEGIN { printf "%.4f", lat / 1000000 }')

echo "   [Métricas Perfil A]"
echo "   - IOPS:             ${IOPS_A} ops/s"
echo "   - Throughput:       ${BW_MB_A} MB/s"
echo "   - Latência Média:   ${LAT_MS_A} ms"
echo "   - Latência p99:     ${P99_MS_A} ms"
echo "   - Latência p99.9:   ${P999_MS_A} ms"

echo "   -> Enviando métricas do Perfil A para o Zabbix..."
send_metric "storage.iops" "${IOPS_A}"
send_metric "storage.bandwidth" "${BW_MB_A}"
send_metric "storage.latency.avg" "${LAT_MS_A}"
send_metric "storage.latency.p99" "${P99_MS_A}"
send_metric "storage.latency.p999" "${P999_MS_A}"
echo "   ✓ Enviado com sucesso!"

sleep 2

# ==============================================================================
# 2. PERFIL B — Leitura Aleatória (4 KB)
# ==============================================================================
echo ""
echo ">> [2/2] Executando Perfil B: Leitura Aleatória (4 KB, iodepth=1, runtime=5s)..."

fio --name=perfil_b_rand \
    --directory="${TEST_DIR}" \
    --rw=randread \
    --bs=4k \
    --size=64M \
    --runtime=5 \
    --time_based \
    --ioengine=sync \
    --output-format=json \
    --output="docs/experiments/fio-test-02.json"

# Extração de métricas com jq
IOPS_B=$(jq -r '.jobs[0].read.iops' "docs/experiments/fio-test-02.json")
BW_BYTES_B=$(jq -r '.jobs[0].read.bw_bytes' "docs/experiments/fio-test-02.json")
BW_MB_B=$(awk -v bw="$BW_BYTES_B" 'BEGIN { printf "%.2f", bw / 1048576 }')
LAT_NS_B=$(jq -r '.jobs[0].read.lat_ns.mean' "docs/experiments/fio-test-02.json")
LAT_MS_B=$(awk -v lat="$LAT_NS_B" 'BEGIN { printf "%.4f", lat / 1000000 }')
P99_NS_B=$(jq -r '.jobs[0].read.clat_ns.percentile["99.000000"] // 0' "docs/experiments/fio-test-02.json")
P99_MS_B=$(awk -v lat="$P99_NS_B" 'BEGIN { printf "%.4f", lat / 1000000 }')
P999_NS_B=$(jq -r '.jobs[0].read.clat_ns.percentile["99.900000"] // 0' "docs/experiments/fio-test-02.json")
P999_MS_B=$(awk -v lat="$P999_NS_B" 'BEGIN { printf "%.4f", lat / 1000000 }')

echo "   [Métricas Perfil B]"
echo "   - IOPS:             ${IOPS_B} ops/s"
echo "   - Throughput:       ${BW_MB_B} MB/s"
echo "   - Latência Média:   ${LAT_MS_B} ms"
echo "   - Latência p99:     ${P99_MS_B} ms"
echo "   - Latência p99.9:   ${P999_MS_B} ms"

echo "   -> Enviando métricas do Perfil B para o Zabbix..."
send_metric "storage.iops" "${IOPS_B}"
send_metric "storage.bandwidth" "${BW_MB_B}"
send_metric "storage.latency.avg" "${LAT_MS_B}"
send_metric "storage.latency.p99" "${P99_MS_B}"
send_metric "storage.latency.p999" "${P999_MS_B}"
echo "   ✓ Enviado com sucesso!"

# Limpeza dos arquivos temporários de teste
rm -rf "${TEST_DIR}"

# ==============================================================================
# 3. Consolidação de Resultados (CSV para Artigo / Relatório)
# ==============================================================================
CSV_FILE="docs/results/experiment-comparison.csv"
cat <<EOF > "${CSV_FILE}"
Metrica,Perfil_A_Sequencial,Perfil_B_Aleatorio,Unidade
IOPS,${IOPS_A},${IOPS_B},ops/s
Throughput,${BW_MB_A},${BW_MB_B},MB/s
Latencia_Media,${LAT_MS_A},${LAT_MS_B},ms
Latencia_p99,${P99_MS_A},${P99_MS_B},ms
Latencia_p99.9,${P999_MS_A},${P999_MS_B},ms
EOF

echo ""
echo "=================================================================="
echo "                   RESULTADOS CONSOLIDADOS                        "
echo "=================================================================="
printf "%-20s | %-20s | %-20s | %-10s\n" "Métrica" "Perfil A (Seq)" "Perfil B (Rand)" "Unidade"
echo "------------------------------------------------------------------"
printf "%-20s | %-20s | %-20s | %-10s\n" "IOPS" "${IOPS_A}" "${IOPS_B}" "ops/s"
printf "%-20s | %-20s | %-20s | %-10s\n" "Throughput" "${BW_MB_A}" "${BW_MB_B}" "MB/s"
printf "%-20s | %-20s | %-20s | %-10s\n" "Latência Média" "${LAT_MS_A}" "${LAT_MS_B}" "ms"
printf "%-20s | %-20s | %-20s | %-10s\n" "Latência p99" "${P99_MS_A}" "${P99_MS_B}" "ms"
printf "%-20s | %-20s | %-20s | %-10s\n" "Latência p99.9" "${P999_MS_A}" "${P999_MS_B}" "ms"
echo "=================================================================="
echo "Arquivos gerados para evidências do artigo:"
echo " - docs/experiments/fio-test-01.json"
echo " - docs/experiments/fio-test-02.json"
echo " - ${CSV_FILE}"
echo "=================================================================="
