#!/usr/bin/env bash
# ==============================================================================
# Script de teste para validação do pipeline zabbix_sender -> Zabbix Server
# Sprint 2 - TI 5
# ==============================================================================

ZABBIX_SERVER="${1:-localhost}"
ZABBIX_PORT="${2:-10051}"
HOST_NAME="${3:-agente-storage-01}"

echo "================================================="
echo " Enviando métricas de teste para o Zabbix Server "
echo " Destino: ${ZABBIX_SERVER}:${ZABBIX_PORT}"
echo " Host:    ${HOST_NAME}"
echo "================================================="

# Valores simulados de benchmark (exemplo de teste de leitura sequencial 4KB)
IOPS="35420.5"
BANDWIDTH="138.36"
LATENCY_AVG="0.89"
LATENCY_P99="2.14"
LATENCY_P999="4.50"
TEMPERATURE="42.0"

echo "[1/6] Enviando storage.iops (${IOPS} ops/s)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.iops" -o "${IOPS}"

echo "[2/6] Enviando storage.bandwidth (${BANDWIDTH} MB/s)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.bandwidth" -o "${BANDWIDTH}"

echo "[3/6] Enviando storage.latency.avg (${LATENCY_AVG} ms)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.latency.avg" -o "${LATENCY_AVG}"

echo "[4/6] Enviando storage.latency.p99 (${LATENCY_P99} ms)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.latency.p99" -o "${LATENCY_P99}"

echo "[5/6] Enviando storage.latency.p999 (${LATENCY_P999} ms)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.latency.p999" -o "${LATENCY_P999}"

echo "[6/6] Enviando storage.temperature (${TEMPERATURE} °C)..."
zabbix_sender -z "${ZABBIX_SERVER}" -p "${ZABBIX_PORT}" -s "${HOST_NAME}" -k "storage.temperature" -o "${TEMPERATURE}"

echo "================================================="
echo "Envio concluído! Verifique no Zabbix Web em:"
echo "Monitoring -> Latest data -> Host: ${HOST_NAME}"
echo "================================================="
