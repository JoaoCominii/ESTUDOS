# 📘 Aula 03 - Árvores de decisão e Regressão

## 📄 Slides da aula
![[3 - Árvores de Decisão.pdf]]

## Tarefas de Aprendizado
Preditivas -> Encontrar uma função(hipótese) a partir dos dados de treinamento para prever um rótulo ou valor com base nos atributos de entrada.
- Algoritmos seguem o paradigma do aprendizado supervisionado (Classificação/Regressão)
- Ou seja existe um supervisor externo que conhece a saída desejada para cada exemplo
Descritivas -> Explorar ou descrever um conjunto de dados
- Algoritmos seguem o paradigma do aprendizado não supervisionado (Agrupamento/Associação)
- Não existe atributo de saída

## O que são Árvores de Decisão/Regressão
- São algoritmos de aprendizado preditivo
- Os nós internos são o teste do valor de um dos atributos de entrada
- Os nós da folha são o valor a ser retornado
![[Pasted image 20260302151255.png]]
## Árvore de Decisão
Decisão é tomada a partir de uma sequência de testes
- Várias perguntas de sim/não
- Hierarquicamente arranjadas
- Estrutura da árvore determinada por meio de aprendizado
- Considerar vários fatores e seguir uma lógica
![[Pasted image 20260302151516.png]]
- Árvores de decisão podem fazer predições para qualquer exemplo de entrada.
## Indução de Árvore de Decisão
##### Como gerar?
Exemplo: Esperar uma mesa ou não?
Problema: Estou em um restaurante e preciso decidir se vou esperar uma mesa.
- Esse é um problema de classificação
Objetivo: Construir uma árvore de decisão para decidir a espera ou não de uma mesa em um restaurante. Aqui, o objetivo é aprender uma definição para o predicado VaiEsperar -> (sim/não).
Queremos uma árvore que seja consistente com os exemplos e seja a menor possível.
Atributos disponiveis: 
1. 1. Alternativa: Há outro restaurante por perto? 
2. Bar: Existe uma área de bar confortável onde se possa esperar? 
3. Sex/Sáb: Hoje é sexta ou sábado? 
4. Faminto: Estamos com fome? 
5. Clientes: Quantas pessoas estão no restaurante? (Nenhum, Alguns e Cheio). 
6. Preço: Faixa de preços do restaurante  
7. Chovendo: Está chovendo do lado de fora? 
8. Reserva: Fizemos uma reserva? 
9. Tipo: Francês, italiano, tailandês, hambúrguer, etc. 
10. EsperaEstimada: 0-10 minutos, 10-30, 30-60, >60
![[Pasted image 20260302151904.png]]
O algoritmo de aprendizagem em árvore de decisão adota uma estratégia gulosa de dividir para conquistar
 - Sempre testa o atributo mais importante primeiro (o que faz mais diferença para a classificação)
##### Decidindo qual atributo testar.
Vamos testar o atributo “Tipo”?
- Ele tem 5 tipos que levaram a decisão de esperar e 5 que levaram a decisão de não esperar
- Então Tipo é um atributo fraco e pouco relevante já que cada resultado tem o mesmo número de exemplos positivos e negativos.
Vamos testar o atributo “Clientes”?
- “Cliente” é um atributo bastante importante.
- Ele mostra que quando não tem nenhum cliente no restaurante nunca decide esperar.
- Quando tem alguns clientes sempre decide esperar.
- Quando está cheio em 2 casos decide esperar e em 4 não esperar.
![[Pasted image 20260302152349.png]]

![[Pasted image 20260302152428.png]]
## Vantagens e Desvantagens
##### ADS - Vantagens
Flexibilidade:
- Não assume nenhuma distribuição para os dados
- Eles são métodos não paramétricos
- O espaço de objetos é  dividido em subespaços, e a cada subespaço é ajustado com diferentes modelos
- Cobertura exaustiva do espaço de instâncias
Seleção de atributos:
- Modelos tendem a ser bastante robustos contra a adição de atributos irrelevantes e redundantes
Interpretabilidade:
- Decisões complexas e globais podem ser divididas em uma serie de decisões mais simples e locais (subproblemas)
- Todas decisões são baseadas nos atributos usados para descrever o problema
Eficiência:
- Algoritmo guloso construído de cima pra baixo
- Utiliza estratégia de dividir para conquistar sem backtracking (voltar quando existe uma falha)

##### ADS - Desvantagens
Valores ausentes:
- Algoritmos devem empregar mecanismos especiais para abordar falta de valores
Atributos contínuos:
- A operação de ordenação consome cerca de 70% do tempo necessário para induzir uma árvore de decisão em grades conjuntos de dados contínuos
Instabilidade:
- Pequenas variações no conjunto de dados do treinamento podem causar grandes variações na árvore final
- Forte tendência a interferência feitas próximo das folhas serem menos confiáveis que aquelas feitas próximas a raiz
