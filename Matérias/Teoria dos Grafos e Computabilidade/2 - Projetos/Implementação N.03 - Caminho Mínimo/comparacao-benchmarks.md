# Benchmark Dijkstra - grafo-esparso

Medições do caminho minimo entre os vertices 1 e n, com 5 repeticoes por arquivo.

| Arquivo | Vertices | Arestas | Caminho encontrado | Distancia total | Arestas no caminho | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |
|---|---:|---:|---|---:|---:|---:|---:|---:|
| grafo-esparso-100.txt | 100 | 199 | sim | 525 | 11 | 0.090 | 0.072 | 0.121 |
| grafo-esparso-1000.txt | 1000 | 1999 | sim | 377 | 10 | 1.052 | 0.493 | 1.454 |
| grafo-esparso-10000.txt | 10000 | 19999 | sim | 773 | 14 | 4.231 | 3.505 | 6.206 |
| grafo-esparso-50000.txt | 50000 | 99999 | sim | 477 | 21 | 5.952 | 5.463 | 6.355 |

## Gráfico: Tempo de Execução vs Tamanho do Grafo (Esparso)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#4CAF50; width:1.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.090 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#4CAF50; width:17.7%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1.052 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#4CAF50; width:71.1%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">4.231 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;">
      <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#4CAF50; width:100%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; text-align:right;">5.952 ms</td>
  </tr>
</table>

## Gráfico: Arestas vs Tempo de Execução

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Arestas</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">199</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#FF9800; width:1.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.090 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.999</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#FF9800; width:17.7%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1.052 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">19.999</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#FF9800; width:71.1%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">4.231 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">99.999</td>
    <td style="padding: 8px;">
      <div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#FF9800; width:100%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; text-align:right;">5.952 ms</td>
  </tr>
</table>

# Benchmark Dijkstra - grafo-denso

Medições do caminho minimo entre os vertices 1 e n, com 5 repeticoes por arquivo.

| Arquivo | Vertices | Arestas | Caminho encontrado | Distancia total | Arestas no caminho | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |
|---|---:|---:|---|---:|---:|---:|---:|---:|
| grafo-denso-100.txt | 100 | 799 | sim | 91 | 3 | 0.023 | 0.021 | 0.026 |
| grafo-denso-1000.txt | 1000 | 7999 | sim | 76 | 7 | 0.069 | 0.053 | 0.094 |
| grafo-denso-10000.txt | 10000 | 79999 | sim | 53 | 5 | 0.215 | 0.190 | 0.283 |
| grafo-denso-50000.txt | 50000 | 399999 | sim | 127 | 10 | 4.711 | 4.470 | 5.032 |

## Gráfico: Tempo de Execução vs Tamanho do Grafo (Denso)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#2196F3; width:0.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.023 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#2196F3; width:1.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.069 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#2196F3; width:4.6%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.215 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;">
      <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#2196F3; width:100%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; text-align:right;">4.711 ms</td>
  </tr>
</table>

## Gráfico: Arestas vs Tempo de Execução

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Arestas</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">799</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#9C27B0; width:0.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.023 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">7.999</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#9C27B0; width:1.5%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.069 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">79.999</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">
      <div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#9C27B0; width:4.6%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">0.215 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">399.999</td>
    <td style="padding: 8px;">
      <div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;">
        <div style="background:#9C27B0; width:100%; height:22px;"></div>
      </div>
    </td>
    <td style="padding: 8px; text-align:right;">4.711 ms</td>
  </tr>
</table>



# Comparação: Grafo Esparso vs Grafo Denso

Análise comparativa do desempenho do algoritmo de Dijkstra entre os dois tipos de grafos.

## Tabela Comparativa

| Vertices | Arestas (Esparso) | Arestas (Denso) | Tempo Esparso (ms) | Tempo Denso (ms) | Razão (Denso/Esparso) |
|---:|---:|---:|---:|---:|---:|
| 100 | 199 | 799 | 0.090 | 0.023 | 0.26x |
| 1000 | 1999 | 7999 | 1.052 | 0.069 | 0.07x |
| 10000 | 19999 | 79999 | 4.231 | 0.215 | 0.05x |
| 50000 | 99999 | 399999 | 5.952 | 4.711 | 0.79x |

## Gráfico: Comparação de Tempos

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
   <tr>
      <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
      <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Esparso</th>
      <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Denso</th>
   </tr>
   <tr>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#4CAF50; width:1.5%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#4CAF50;">0.090 ms</div>
      </td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#2196F3; width:0.5%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#2196F3;">0.023 ms</div>
      </td>
   </tr>
   <tr>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#4CAF50; width:17.7%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#4CAF50;">1.052 ms</div>
      </td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#2196F3; width:1.5%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#2196F3;">0.069 ms</div>
      </td>
   </tr>
   <tr>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#4CAF50; width:71.1%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#4CAF50;">4.231 ms</div>
      </td>
      <td style="padding: 8px; border-bottom: 1px solid #eee;">
         <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#2196F3; width:4.6%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#2196F3;">0.215 ms</div>
      </td>
   </tr>
   <tr>
      <td style="padding: 8px;">50.000</td>
      <td style="padding: 8px;">
         <div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#4CAF50; width:100%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#4CAF50;">5.952 ms</div>
      </td>
      <td style="padding: 8px;">
         <div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;">
            <div style="background:#2196F3; width:100%; height:22px;"></div>
         </div>
         <div style="font-size:12px; margin-top:4px; color:#2196F3;">4.711 ms</div>
      </td>
   </tr>
</table>

## Análise de Eficiência

### Observações:

1. **Para instâncias pequenas e médias (100-10000 vértices)**:
   - O grafo denso apresenta **melhor desempenho** apesar de ter muito mais arestas
   - Razão: a densidade aumenta as chances de encontrar um caminho curto rapidamente
   - O desempate por menor número de arestas favorece caminhos mais diretos

2. **Para instâncias grandes (50000 vértices)**:
   - Os tempos se aproximam (5.952 ms vs 4.711 ms)
   - A quantidade absoluta de arestas começa a pesar

3. **Complexidade do Dijkstra**:
   - Teórica: O((V + E) log V) com fila de prioridade
   - Esparso: O(n log n) onde n = V
   - Denso: O(n log n) onde n = V (o termo E é menor ou comparável em relação ao custo de heap)

### Conclusão:

O algoritmo de Dijkstra é **robusto** para ambos os tipos de grafos com pesos positivos. A estratégia de desempate por menor número de arestas funciona corretamente em todos os casos testados.
