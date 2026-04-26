# Análise de Algoritmos de Detecção de Pontes: Naive vs Tarjan

Projeto de análise comparativa entre duas estratégias para detecção de pontes no algoritmo de Fleury. Este projeto implementa e avalia empiricamente:

- **Estratégia Naive**: Testes repetidos de conectividade para detecção de pontes
- **Algoritmo de Tarjan**: Pré-computação linear de todas as pontes em O(V+E)

## Estrutura do Projeto

```
CODIGOS/
├── FleuryMain.java              # Execução interativa/linha de comando do algoritmo
├── FleuryEngine.java            # Core do algoritmo de Fleury parametrizável
├── BridgeFinderNaive.java       # Implementação da estratégia Naive
├── BridgeFinderTarjan.java      # Implementação do algoritmo de Tarjan
├── FleuryBenchmark.java         # Benchmark para medir desempenho
├── GraphTestGenerator.java      # Gerador de grafos de teste
├── amostras-10/                 # Grafos gerados (tamanho 100-100K, 10 instâncias cada)
```

## Requisitos

- **Java**: versão 8 ou superior
- **Compilador**: `javac` (padrão do JDK)

## Compilação

### Passo 1: Compilar todos os arquivos

```bash
cd "CODIGOS"
javac *.java
```

Ou para compilar um arquivo específico:

```bash
javac FleuryMain.java
```

## Como Usar

### 1. Gerar Grafos de Teste

O gerador cria 3 tipos de grafos (Euleriano, Semi-Euleriano, Não-Euleriano) em múltiplos tamanhos.

**Sintaxe:**
```bash
java GraphTestGenerator <pasta_saida> [seed] [instancias_por_teste]
```

**Exemplos:**

```bash
# Gerar com padrão (4 tamanhos: 100, 1K, 10K, 100K; 10 instâncias cada)
java GraphTestGenerator ./amostras-padrao

# Gerar com seed específica e 15 instâncias por tamanho
java GraphTestGenerator ./amostras-15 12345 15

# Gerar com seed diferente
java GraphTestGenerator ./novo-lote 98765 10
```

**O que será gerado:**
- `grafo-euleriano-100-01.txt` até `grafo-euleriano-100-10.txt`
- `grafo-euleriano-1000-01.txt` até `grafo-euleriano-1000-10.txt`
- `grafo-euleriano-10000-01.txt` até `grafo-euleriano-10000-10.txt`
- `grafo-euleriano-100000-01.txt` até `grafo-euleriano-100000-10.txt`
- E o mesmo para `semi-euleriano` e `nao-euleriano`

**Total**: 120 grafos (3 tipos × 4 tamanhos × 10 instâncias)

### 2. Executar o Algoritmo de Fleury (Modo Interativo)

```bash
java FleuryMain
```

O programa solicitará:
1. Caminho do arquivo de grafo
2. Escolha da estratégia:
   - `1` = Naive (testes repetidos de conectividade)
   - `2` = Tarjan (pré-computação linear)

**Exemplo de uso interativo:**
```
Digite o caminho do arquivo de grafo: grafo-com-ponte-teste.txt
Escolha a estratégia de deteccao de pontes:
  1. Naive
  2. Tarjan
Digite sua opcao (1 ou 2): 2
```

### 3. Executar via Linha de Comando

```bash
java FleuryMain <arquivo> <opcao>
```

**Exemplos:**

```bash
# Usar estratégia Naive
java FleuryMain grafo-teste.txt 1

# Usar estratégia Tarjan
java FleuryMain grafo-teste.txt 2

# Com pasta
java FleuryMain amostras-10/grafo-euleriano-100-01.txt 2
```


### 4. Executar Benchmark de Desempenho

Compara o tempo de execução entre Naive e Tarjan em um lote de grafos.

**Sintaxe:**
```bash
java FleuryBenchmark <pasta_com_grafos> [opcao] [maxN]
```

**Parâmetros:**
- `pasta_com_grafos`: Pasta contendo os arquivos de grafo
- `opcao` (opcional): `1` (Naive), `2` (Tarjan), ou vazio para ambos
- `maxN` (opcional): Tamanho máximo de grafo a processar

**Exemplos:**

```bash
# Benchmark completo com ambas as estratégias
java FleuryBenchmark amostras-10

# Apenas estratégia Tarjan
java FleuryBenchmark amostras-10 2

# Apenas estratégia Naive, grafos até 10K vértices
java FleuryBenchmark amostras-10 1 10000

# Ambas as estratégias, grafos até 50K vértices
java FleuryBenchmark amostras-10 "" 50000
```

**Saída do Benchmark:**
- Arquivo CSV com tempos agregados para cada tipo/tamanho
- Exemplo: `benchmark-fleury-naive.csv`, `benchmark-fleury-tarjan.csv`

**Formato do CSV:**
```
n,euleriano_media,euleriano_mediana,euleriano_p95,...
100,0.72,0.37,2.29,...
1000,5.62,4.42,15.72,...
...
```

## Formato de Arquivo de Grafo

Arquivo de texto simples com a seguinte estrutura:

```
V E
u1 v1
u2 v2
...
uE vE
```

**Descrição:**
- Primeira linha: `V` (número de vértices) e `E` (número de arestas)
- Próximas `E` linhas: cada aresta `u v` (vértices são 0-indexados, inteiros)
- Grafo não-direcionado (cada aresta é bidirecional)

**Exemplo:** Grafo com 4 vértices e 5 arestas

```
4 5
0 1
1 2
2 3
3 0
0 2
```

## Exemplos Práticos Completos

### Exemplo 1: Testar um único grafo com ambas estratégias

```bash
# Compilar
javac *.java

# Testar com Naive
java FleuryMain grafo-teste.txt 1

# Testar com Tarjan
java FleuryMain grafo-teste.txt 2
```

### Exemplo 2: Gerar e fazer benchmark

```bash
# Compilar
javac *.java

# Gerar novo lote de grafos
java GraphTestGenerator meus-testes

# Fazer benchmark
java FleuryBenchmark meus-testes

# Ou apenas estratégia Tarjan
java FleuryBenchmark meus-testes 2
```

### Exemplo 3: Workflow completo de análise

```bash
# 1. Compilar (uma única vez)
javac *.java

# 2. Gerar grafos com seed reproduzível
java GraphTestGenerator ./lote-1 42 10

# 3. Calcular benchmark (ambas estratégias)
java FleuryBenchmark ./lote-1

# 4. Examinar resultados específicos
java FleuryBenchmark ./lote-1 1 1000    # Naive até 1K vértices
java FleuryBenchmark ./lote-1 2 100000  # Tarjan até 100K vértices

# 5. Testar um grafo individual para validação
java FleuryMain ./lote-1/grafo-euleriano-100-01.txt 1
java FleuryMain ./lote-1/grafo-euleriano-100-01.txt 2
```

## Saída Esperada

### Execução de Algoritmo

```
Tempo de execução: 1234 ms
Trilha Euleriana encontrada com 156 arestas
Caminho: 0 -> 1 -> 2 -> 3 -> 0 -> 4 -> ...
```

### Benchmark

```
=== Processando grafo-euleriano-100-01.txt ===
  Naive: 0.723 ms
  Tarjan: 0.620 ms
  
[Resumo de estatísticas por tamanho]
n=100:   Naive media=0.72    Tarjan media=0.62    (Aceleracao: 1.16x)
n=1000:  Naive media=5.62    Tarjan media=7.88    (Aceleracao: 0.71x)
n=10000: Naive media=723.30  Tarjan media=607.48  (Aceleracao: 1.19x)
```

## Interpretação dos Resultados

- **Aceleração > 1.0**: Tarjan é mais rápido (recomendado)
- **Aceleração < 1.0**: Naive é mais rápido neste caso (raro em grafos grandes)
- **Padrão esperado**: Tarjan domina em grafos grandes (> 10K vértices), pois sua pré-computação O(V+E) amortiza melhor

## Troubleshooting

### Erro: "ClassNotFoundException"

```bash
# Certifique-se de estar no diretório correto
cd CODIGOS

# Recompile
javac *.java

# Tente novamente
java FleuryMain
```

### Erro: "FileNotFoundException"

```bash
# Verifique o caminho do arquivo
# Use caminho relativo à pasta CODIGOS ou caminho absoluto

# Relativo
java FleuryMain grafo-com-ponte-teste.txt 1

# Absoluto (Windows)
java FleuryMain "C:\Users\...\CODIGOS\grafo-com-ponte-teste.txt" 1
```

### Programa demora muito (timeout)

- Reduza o tamanho máximo com o parâmetro `maxN`
- Use apenas a estratégia de interesse
- Teste com grafos menores primeiro

```bash
java FleuryBenchmark amostras-10 2 10000  # Apenas Tarjan até 10K
```

## Referências

- **Fleury's Algorithm**: https://en.wikipedia.org/wiki/Fleury%27s_algorithm
- **Tarjan's Bridge Finding**: Tarjan, R. (1974). "A Note on Finding the Bridges of a Graph"
- **Graph Theory**: Diestel, R. (2017). Graph Theory
- **Algorithms**: Cormen, T. et al. (2009). Introduction to Algorithms

## Autores

João Comini Cesar de Andrade  
Rafael Rehfeld Martins de Oliveira

PUCMG - Pontifícia Universidade Católica de Minas Gerais  
Disciplina: Teoria dos Grafos e Computabilidade
