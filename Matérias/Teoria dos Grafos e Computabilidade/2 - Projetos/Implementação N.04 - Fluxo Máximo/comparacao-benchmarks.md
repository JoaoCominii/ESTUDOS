# Benchmark Fluxo Maximo - grafo-esparso

Medições do fluxo máximo entre os vértices 1 e n, com 5 repetições por arquivo.

| Arquivo | Vertices | Arestas | Caminhos disjuntos | Caminhos listados | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |
|---|---:|---:|---:|---:|---:|---:|---:|
| grafo-esparso-100.txt | 100 | 198 | 1 | 1 | 1.812 | 1.503 | 2.121 |
| grafo-esparso-1000.txt | 1000 | 1998 | 1 | 1 | 4.034 | 2.592 | 5.770 |
| grafo-esparso-10000.txt | 10000 | 19998 | 1 | 1 | 12.120 | 8.164 | 16.650 |
| grafo-esparso-50000.txt | 50000 | 99998 | 2 | 2 | 49.998 | 40.682 | 62.431 |

## Gráfico: Tempo de Execução vs Tamanho do Grafo (Esparso)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#4CAF50; width:3.6%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1.812 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#4CAF50; width:8.1%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">4.034 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#4CAF50; width:24.3%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">12.120 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;"><div style="background:#e8f5e9; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#4CAF50; width:100%; height:22px;"></div></div></td>
    <td style="padding: 8px; text-align:right;">49.998 ms</td>
  </tr>
</table>

## Gráfico: Quantidade de Caminhos vs Tamanho do Grafo (Esparso)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Caminhos disjuntos</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#FF9800; width:50%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#FF9800; width:50%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#FF9800; width:50%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;"><div style="background:#fff3e0; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#FF9800; width:100%; height:22px;"></div></div></td>
    <td style="padding: 8px; text-align:right;">2</td>
  </tr>
</table>

# Benchmark Fluxo Maximo - grafo-camadas

Medições do fluxo máximo entre os vértices 1 e n, com 5 repetições por arquivo.

| Arquivo | Vertices | Arestas | Caminhos disjuntos | Caminhos listados | Tempo medio (ms) | Tempo min (ms) | Tempo max (ms) |
|---|---:|---:|---:|---:|---:|---:|---:|
| grafo-camadas-100.txt | 100 | 294 | 8 | 8 | 1.704 | 1.293 | 2.446 |
| grafo-camadas-1000.txt | 1000 | 2994 | 8 | 8 | 5.675 | 3.686 | 9.523 |
| grafo-camadas-10000.txt | 10000 | 29994 | 10 | 10 | 23.549 | 21.286 | 27.570 |
| grafo-camadas-50000.txt | 50000 | 149994 | 11 | 11 | 108.228 | 100.717 | 117.630 |

## Gráfico: Tempo de Execução vs Tamanho do Grafo (Camadas)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Tempo de execução</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#2196F3; width:1.6%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">1.704 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#2196F3; width:5.2%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">5.675 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#2196F3; width:21.8%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">23.549 ms</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;"><div style="background:#e3f2fd; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#2196F3; width:100%; height:22px;"></div></div></td>
    <td style="padding: 8px; text-align:right;">108.228 ms</td>
  </tr>
</table>

## Gráfico: Quantidade de Caminhos vs Tamanho do Grafo (Camadas)

<table style="width:100%; border-collapse: collapse; margin: 16px 0; font-family: Arial, sans-serif;">
  <tr>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc; width: 120px;">Tamanho</th>
    <th style="text-align:left; padding: 8px; border-bottom: 1px solid #ccc;">Caminhos disjuntos</th>
    <th style="text-align:right; padding: 8px; border-bottom: 1px solid #ccc; width: 90px;">Valor</th>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">100</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#9C27B0; width:72.7%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">8</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">1.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#9C27B0; width:72.7%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">8</td>
  </tr>
  <tr>
    <td style="padding: 8px; border-bottom: 1px solid #eee;">10.000</td>
    <td style="padding: 8px; border-bottom: 1px solid #eee;"><div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#9C27B0; width:90.9%; height:22px;"></div></div></td>
    <td style="padding: 8px; border-bottom: 1px solid #eee; text-align:right;">10</td>
  </tr>
  <tr>
    <td style="padding: 8px;">50.000</td>
    <td style="padding: 8px;"><div style="background:#f3e5f5; width:100%; height:22px; border-radius:4px; overflow:hidden;"><div style="background:#9C27B0; width:100%; height:22px;"></div></div></td>
    <td style="padding: 8px; text-align:right;">11</td>
  </tr>
</table>

## Comparação de Eficiência

| Tamanho | Esparso (ms) | Camadas (ms) | Caminhos Esparsos | Caminhos em Camadas |
|---|---:|---:|---:|---:|
| 100 | 1.812 | 1.704 | 1 | 8 |
| 1000 | 4.034 | 5.675 | 1 | 8 |
| 10000 | 12.120 | 23.549 | 1 | 10 |
| 50000 | 49.998 | 108.228 | 2 | 11 |

### Observações

1. **Grafo esparso**:
   - Produziu poucos caminhos disjuntos, como esperado.
   - O custo do fluxo máximo cresceu de forma moderada.

2. **Grafo em camadas**:
   - Produziu muito mais caminhos disjuntos.
   - A medição de tempo cresceu mais rapidamente, refletindo a maior quantidade de fluxo a decompor.

3. **Conclusão geral**:
   - O método de fluxo máximo é adequado para identificar caminhos disjuntos em arestas.
   - A estrutura em camadas é a mais interessante para demonstrar a capacidade do algoritmo de encontrar vários caminhos simultaneamente.
