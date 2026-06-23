# Puzzle 8

## Instruções de Execução

### Requisitos

* Python 3.8 ou superior
* Não há dependências externas

### Executando o Programa

No terminal, execute:

```bash
python3 puzzle8.py
```

## Exemplos de Entrada

O estado inicial deve ser informado por **9 números inteiros**, separados por espaço, onde **0 representa o espaço vazio**.

### Exemplos

| Entrada             | Descrição                         |
| ------------------- | --------------------------------- |
| `0 1 2 3 4 5 6 7 8` | Estado inicial igual ao objetivo  |
| `1 2 5 3 4 0 6 7 8` | Instância fácil (3 movimentos)    |
| `8 7 6 5 4 3 2 1 0` | Instância difícil (28 movimentos) |

## Funcionamento

1. O programa solicita ao usuário o estado inicial do tabuleiro.
2. É realizada uma verificação para determinar se a instância é solucionável.
3. Caso seja solucionável, um menu é apresentado para seleção do algoritmo de busca.
4. O algoritmo escolhido é executado para encontrar a solução.

## Saída

Ao final da execução, o programa exibe:

* Caminho completo da solução passo a passo;
* Número de nós visitados;
* Número de nós gerados;
* Profundidade da solução;
* Tempo total de execução.

## Observações

* O valor `0` representa a posição vazia do tabuleiro.
* Instâncias não solucionáveis são identificadas antes da execução do algoritmo.
* O desempenho pode variar conforme o algoritmo selecionado e a dificuldade da instância.
