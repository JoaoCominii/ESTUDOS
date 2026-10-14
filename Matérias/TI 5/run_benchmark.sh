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

cleanup() {
    rm -rf -- "${TEST_DIR}"
}

trap cleanup EXIT
cleanup
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
echo ">> [1/4] Executando Perfil A: Leitura Sequencial (4 KB, iodepth=1, runtime=5s)..."

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
echo ">> [2/4] Executando Perfil B: Leitura Aleatória (4 KB, iodepth=1, runtime=5s)..."

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

# ==============================================================================
# 3. PERFIL C — Escrita Sequencial (128 KB)
# ==============================================================================
echo ""
echo ">> [3/4] Executando Perfil C: Escrita Sequencial (128 KB, runtime=5s)..."

fio --name=perfil_c_write_seq \
    --directory="${TEST_DIR}" \
    --rw=write \
    --bs=128k \
    --size=64M \
    --runtime=5 \
    --time_based \
    --ioengine=sync \
    --output-format=json \
    --output="docs/experiments/fio-test-03.json"

# Extrai métricas de escrita e envia ao Zabbix
IOPS_C=$(jq -r '.jobs[0].write.iops' "docs/experiments/fio-test-03.json")
BW_BYTES_C=$(jq -r '.jobs[0].write.bw_bytes' "docs/experiments/fio-test-03.json")
BW_MB_C=$(awk -v bw="${BW_BYTES_C}" 'BEGIN { printf "%.2f", bw / 1048576 }')
LAT_NS_C=$(jq -r '.jobs[0].write.lat_ns.mean' "docs/experiments/fio-test-03.json")
LAT_MS_C=$(awk -v lat="${LAT_NS_C}" 'BEGIN { printf "%.4f", lat / 1000000 }')
P99_NS_C=$(jq -r '.jobs[0].write.clat_ns.percentile["99.000000"] // 0' "docs/experiments/fio-test-03.json")
P99_MS_C=$(awk -v lat="${P99_NS_C}" 'BEGIN { printf "%.4f", lat / 1000000 }')
P999_NS_C=$(jq -r '.jobs[0].write.clat_ns.percentile["99.900000"] // 0' "docs/experiments/fio-test-03.json")
P999_MS_C=$(awk -v lat="${P999_NS_C}" 'BEGIN { printf "%.4f", lat / 1000000 }')

echo "   -> Enviando métricas do Perfil C para o Zabbix..."
send_metric "storage.iops" "${IOPS_C}"
send_metric "storage.bandwidth" "${BW_MB_C}"
send_metric "storage.latency.avg" "${LAT_MS_C}"
send_metric "storage.latency.p99" "${P99_MS_C}"
send_metric "storage.latency.p999" "${P999_MS_C}"
echo "   ✓ Enviado com sucesso!"

# ==============================================================================
# 4. PERFIL D — Concorrência (variação de numjobs e iodepth)
# ==============================================================================
echo ""
echo ">> [4/4] Executando Perfil D: Concorrência (numjobs e iodepth: 1, 2, 4 e 8)..."

CONCURRENCY_CSV="docs/results/profile-d-concurrency.csv"
printf "numjobs,iodepth,iops,bw_bytes,bw_mb,latency_avg_ms,p99_ms,p99_9_ms\n" > "${CONCURRENCY_CSV}"

for NUMJOBS in 1 2 4 8; do
    for IODEPTH in 1 2 4 8; do
        D_OUTPUT="docs/experiments/fio-test-04-numjobs-${NUMJOBS}-iodepth-${IODEPTH}.json"

        fio --name="perfil_d_j${NUMJOBS}_d${IODEPTH}" \
            --directory="${TEST_DIR}" \
            --filename="${TEST_DIR}/profile-d.dat" \
            --rw=randread \
            --bs=4k \
            --size=64M \
            --runtime=5 \
            --time_based \
            --ioengine=libaio \
            --direct=1 \
            --numjobs="${NUMJOBS}" \
            --iodepth="${IODEPTH}" \
            --group_reporting \
            --output-format=json \
            --output="${D_OUTPUT}"

        IOPS_D=$(jq -r '.jobs[0].read.iops' "${D_OUTPUT}")
        BW_BYTES_D=$(jq -r '.jobs[0].read.bw_bytes' "${D_OUTPUT}")
        BW_MB_D=$(awk -v bw="${BW_BYTES_D}" 'BEGIN { printf "%.2f", bw / 1048576 }')
        LAT_NS_D=$(jq -r '.jobs[0].read.lat_ns.mean' "${D_OUTPUT}")
        LAT_MS_D=$(awk -v lat="${LAT_NS_D}" 'BEGIN { printf "%.4f", lat / 1000000 }')
        P99_NS_D=$(jq -r '.jobs[0].read.clat_ns.percentile["99.000000"] // 0' "${D_OUTPUT}")
        P99_MS_D=$(awk -v lat="${P99_NS_D}" 'BEGIN { printf "%.4f", lat / 1000000 }')
        P999_NS_D=$(jq -r '.jobs[0].read.clat_ns.percentile["99.900000"] // 0' "${D_OUTPUT}")
        P999_MS_D=$(awk -v lat="${P999_NS_D}" 'BEGIN { printf "%.4f", lat / 1000000 }')

        printf "%s,%s,%s,%s,%s,%s,%s,%s\n" \
            "${NUMJOBS}" "${IODEPTH}" "${IOPS_D}" "${BW_BYTES_D}" \
            "${BW_MB_D}" "${LAT_MS_D}" "${P99_MS_D}" "${P999_MS_D}" \
            >> "${CONCURRENCY_CSV}"

        echo "   -> Enviando métricas do Perfil D (numjobs=${NUMJOBS}, iodepth=${IODEPTH}) ao Zabbix..."
        send_metric "storage.iops" "${IOPS_D}"
        send_metric "storage.bandwidth" "${BW_MB_D}"
        send_metric "storage.latency.avg" "${LAT_MS_D}"
        send_metric "storage.latency.p99" "${P99_MS_D}"
        send_metric "storage.latency.p999" "${P999_MS_D}"
    done
done

# ==============================================================================
# 5. Consolidação de Resultados (CSV para Artigo / Relatório)
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
echo " - docs/experiments/fio-test-03.json"
echo " - docs/experiments/fio-test-04-numjobs-*-iodepth-*.json"
echo " - ${CONCURRENCY_CSV}"
echo " - ${CSV_FILE}"
echo "=================================================================="
