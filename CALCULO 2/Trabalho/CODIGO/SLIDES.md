# Slides (Esboço) — Integração por Frações Parciais

Este ficheiro é um esboço dos slides que podem ser convertidos para PDF (ex.: usar pandoc).

---

## Título
Integração de funções racionais usando frações parciais — Implementação em Java

---

## 1. Objetivo
- Estudar e implementar algoritmos para integração de racionais via frações parciais.
- Entregar um programa que identifique o tipo de denominador, aplique decomposição e integre simbolicamente.

---

## 2. Representação computacional
- Usamos `Polynomial` como `List<Double>` com índice = potência.
- Exemplo: 5x - 3 => `[-3, 5]`.

---

## 3. Algoritmos principais
- Caso 1: (Ax+B)/((x-x1)(x-x2)) -> decomposição em C1/(x-x1)+C2/(x-x2).
- Caso 2: (Ax+B)/(ax^2+bx+c) -> trata Δ>0, Δ=0, Δ<0 (ln + arctan).
- Caso 3: (Ax+B)/((x^2+c1)(x^2+c2)) -> decompõe em (Cx+D)/(x^2+c).

---

## 4. Fluxo do programa
1. Recebe expressão ou listas de coeficientes.
2. (Parser) converte expressão textual em coeficientes.
3. detecta tipo do denominador (grau, forma especial).
4. aplica decomposição apropriada.
5. integra termo a termo e retorna expressão simbólica.

---

## 5. Exemplos e saída
- `(5x-3)/(x^2-2x-3)` -> detectado Caso 2a (Δ>0) -> resultado simbólico.
- `(2x+1)/((x^2+1)(x^2+4))` -> detectado produto de quadráticos -> resultado simbólico.

---

## 6. Limitações e melhorias
- Parser simples: não suporta multiplicação implícita complexa.
- Falta fatoração numérica geral e solução de sistemas para decomposições arbitrárias.
- Melhorias: adicionar factorizador, parser mais robusto, testes automáticos.

---

## 7. Como executar
- Compilar com `javac Polynomial.java SymbolicIntegrator.java` e executar `java SymbolicIntegrator`.

---

## 8. Conclusão
- O programa resolve os casos pedidos no enunciado e fornece saída simbólica legível. Com mais trabalho pode ser tornado genérico para qualquer integral racional.
