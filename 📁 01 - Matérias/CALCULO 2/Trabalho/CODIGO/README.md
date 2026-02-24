# Symbolic Integrator (Frações Parciais)

Este projeto implementa algoritmos para integração de funções racionais usando decomposição em frações parciais.

## Representação computacional
- Polinômios são representados pela classe `Polynomial` como `List<Double>` onde o índice é a potência (0 = termo constante).
  - Ex: 5x - 3 => `List.of(-3.0, 5.0)` (índices [constante, x^1]).

## Funcionalidades implementadas
- `solveCase1(A,B,x1,x2)` — resolve integrais do tipo ∫(Ax+B)/((x-x1)(x-x2)) dx.
- `solveCase2(A,B,a,b,c)` — resolve ∫(Ax+B)/(ax^2+bx+c) dx com subcasos: Δ>0, Δ=0, Δ<0.
- `solveCase3(A,B,c1,c2)` — resolve ∫(Ax+B)/((x^2+c1)(x^2+c2)) dx (assume c1,c2>0 distintos).
- `integrate(List<Double> numer, List<Double> denom)` — rotina automática mínima:
  - realiza divisão polinomial quando necessário (integra o quociente polinomial separadamente);
  - detecta denominador grau 2 (usa `solveCase2`) e alguns denominadores grau 4 da forma (x^2+c1)(x^2+c2) (usa `solveCase3`);
  - retorna resultado simbólico e informa qual caso foi detectado.
- `parseAndIntegrate(String expr)` — parser simples que aceita expressões como `(5x-3)/(x^2-2x-3)`, converte em listas de coeficientes e chama `integrate(...)`.

## Como rodar
No Powershell, na pasta do projeto:

```powershell
javac Polynomial.java SymbolicIntegrator.java
java SymbolicIntegrator
```

## Exemplos de entrada textual (parser)
- `(5x-3)/(x^2-2x-3)`
- `(2x+1)/((x^2+1)(x^2+4))`

## Limitações
- O parser é simples e aceita apenas polinômios expressos por soma/subtração de termos do tipo `c`, `cx`, `cx^k`.
- Não trata multiplicação implícita complexa (ex.: `2(x^2+1)`), nem expressões não polinomiais.
- A rotina automática não realiza fatoração numérica geral; casos mistos complexos (combinação de fatores lineares e quadráticos arbitrários) não são tratados automaticamente.

## Próximos passos sugeridos
- Implementar fatoração numérica e montagem do sistema linear para cobrir casos mistos e multiplicidades.
- Melhorar o parser para suportar multiplicação implícita e expressões mais gerais.


## Contato
Edite os arquivos `SymbolicIntegrator.java` e `Polynomial.java` para ajustar a formatação ou adicionar novos casos.
