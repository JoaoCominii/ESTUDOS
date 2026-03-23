
# Lista de Exercícios #2 – Árvores de Decisão - João Comini Cesar de Andrade

### Questão 01. Sobre os conceitos de Árvores de Decisão, responda:
1. Como uma árvore de decisão é gerada? Qual o significado do atributo que está na raiz da árvore?
	R: Um algoritmo de decisão adota uma estratégia gulosa de dividir para conquistar, dessa forma a raiz da árvore é sempre o atributo mais relevante ou seja aquele que faz a maior diferença para a classificação para que ele seja testado primeiro e gere a menor árvore possível.
2. Qual a diferença entre os nós internos e os nós de folha de uma árvore de decisão?
	R: Em uma árvore de decisão os nós internos são o teste do valor de um dos atributos de entrada e os nós folha são as saídas ou seja o valor a ser retornado.
3. O que você pode fazer com uma árvore de decisão gerada a partir de uma base de dados?
	R:  Com uma árvore de decisão gerada a partir de uma base de dados podemos usa-la para fazer previsões com base nas características dos dados de entrada em novos exemplos que ainda não tenham sido vistos no ambiente de treinamento.
4. Quais as principais vantagens e desvantagens de um algoritmo de árvore de decisão?
	R: As vantagens de um algoritmo de árvore de decisão são que ele é mais flexível já que ele não assume nenhuma distribuição para os dados, os métodos são não paramétricos, o espaço de objetos é dividido em subespaços e a cobertura completa das instancias, outra vantagem é na seleção de atributos em que os modelos tendem a descartar atributos irrelevantes e redundantes, as seleções complexas são divididas em varias mais simples e as decisões são baseadas nos atributos usados para descrever o problema além disso ele é eficiente por ser um algoritmo guloso construído de cima para baixo.
	As desvantagens são que o algoritmo deve lidar com valores ausentes, a operação de ordenação consome cerca de 70% do tempo de indução da árvore, pequenas variações no conjunto de treinamento podem levar a grandes mudanças na árvore final e as interferências feitas nas folhas geralmente são menos confiáveis que aquelas feitas na raiz.
5. Como avaliamos a qualidade de uma árvore?
	R: Para avaliar a qualidade de uma árvore temos algumas métricas de avaliação:
	- A precisão que é a taxa de instancias que foram corretamente classificadas como pertencente a classe em questão dentre todos os que foram classificados na classe em questão.
	- A sensibilidade ou *recall*  é a taxa de instancias que foram corretamente classificadas como pertencente a classe em questão dentre todos que realmente pertencem a classe em questão.
	- Acurácia que é a taxa total de instancias corretamente classificadas.
	- F-Measure ou F1-Score que é uma media harmônica entre precisão e sensibilidade, atribuindo o mesmo peso para sensibilidade e precisão.
6. Como podemos obter as regras a partir de uma árvore de decisão?
	R: As regras são obtidas percorrendo os caminhos da raiz até as folhas da árvore. Cada caminho gera uma regra do tipo “se-então”, formada pelas condições dos nós e pela decisão final representada na folha.

## Questão 02. Considere o conjunto de dados sobre “Esperar ou não pelo restaurante” (verifique o arquivo “restaurante.csv” no CANVAS) e faça:
1. Calcule o ganho de informação para cada atributo. Qual será o atributo raiz da árvore?
	R: Primeiro calcular a entropia:
	Contagem: SIM = 6, NÃO = 6, TOTAL = 12
	p(Sim) = 6 /12 = 0,5
	p(Não) = 6/12 = 0,5
	H(S) = -(0.5log$2$ 0.5 + 0.5log$2$ 0.5 ) 
	H(S) = 1, entropia do conjunto = 1
	Calcular o ganho de informação:
	A função importância tem valor máximo quando o atributo for perfeito e mínimo quando ele for inútil. 
	1. divide os dados por atributo
	2.  calcula a entropia de cada grupo
	3. faz a média ponderada
	4. subtrai da entropia original
	Ganho de cada atributo:

| Atributo    | Ganho     |
| :---------- | :-------- |
| Alternativo | 0.000     |
| Bar         | 0.000     |
| SexSab      | 0.021     |
| Fome        | 0.196     |
| Cliente     | **0.541** |
| Preco       | 0.196     |
| Chuva       | 0.021     |
| Res         | 0.021     |
| Tipo        | 0.000     |
| Tempo       | 0.208     |
Como cliente tem o maior ganho de informações ele será o nó raiz

2.  Qual atributo estará no segundo nível da árvore? Faça os cálculos e apresente a árvore gerada até o segundo nível.
	R: Devemos analisar cada ramo da árvore:
	Primeiro nível:
	Clientes = Alguns -> Sim
	Clientes = Nenhum -> Não
	Clientes = Cheio -> Sim = 2 e Não = 4
	O maior ganho é quando tem Alguns clientes.
	Segundo nível:
	Agora dividimos por **Tempo**.
	O atributo que aparece no segundo nível da árvore é Tempo utilizado para dividir o ramo onde Cliente = Cheio.

## Questão 03. Leia o artigo “A comparative study of decision tree ID3 and C4.5” que está no CANVAS e responda:
1. Quais as diferenças entre os algoritmos de árvore ID3 e C4.5?
	R:   
	O algoritmo C4.5 é uma evolução do ID3, desenvolvido para resolver algumas limitações do ID3.  
	Principais diferenças:  
	- Critério de divisão  
	- ID3 utiliza Information Gain (ganho de informação).  
	- C4.5 utiliza Gain Ratio, que reduz o viés para atributos com muitos valores.  
	- Tipos de atributos  
	- ID3 trabalha principalmente com atributos categóricos.  
	- C4.5 suporta atributos categóricos e numéricos (contínuos).  
	- Valores faltantes  
	- ID3 não trata valores ausentes.  
	- C4.5 consegue lidar com dados com valores faltantes.  
	- Poda da árvore  
	- ID3 não possui mecanismo de poda.  
	- C4.5 realiza pruning para reduzir o tamanho da árvore e evitar overfitting.  
	- Desempenho  
	- Em geral, C4.5 apresenta melhor desempenho e maior capacidade de generalização que o ID3.
2. Como o algoritmo C4.5 lida com os atributos de entrada que são numéricos? 
	R: O algoritmo C4.5 consegue trabalhar com atributos contínuos (numéricos) utilizando limiares de divisão.  
O processo funciona da seguinte forma:  
  
1. Os valores numéricos do atributo são ordenados.  
2. O algoritmo testa possíveis pontos de corte.  
3. Para cada ponto de corte, os dados são divididos em dois grupos:  
- valores menores ou iguais ao limiar  
- valores maiores que o limiar  
4. O ponto que gera o melhor Gain Ratio é escolhido para a divisão do nó.  
  
Exemplo de regra gerada:  
Se petal_length ≤ 2.45 → Setosa  
Se petal_length > 2.45 → outra classe

## Questão 04. A figura abaixo mostra uma árvore de decisão construída a partir de um conjunto de dados no qual as instâncias são descritas por quatro atributos: (i) Tamanho da Pétala, (ii) Largura da Pétala, (iii) Tamanho da Sépala e (iv) Largura da Sépala
1. Iris-versicolor
2. Iris-setosa
3. Iris-virginica
4. Iris-virginica

## Questão 05. Considere a seguinte matriz de confusão obtida para o problema do restaurante (i.e., o objetivo era decidir se espero uma mesa em um restaurante) por meio de uma Árvore de Decisão:

Quais são os valores das métricas abaixo para cada uma das classes (SIM/NÃO)?

|     | TVP   | TFN  | TFP  | TVN   | Precisão | Recall | F1   |
| --- | ----- | ---- | ---- | ----- | -------- | ------ | ---- |
| SIM | 15/18 | 3/18 | 5/20 | 15/20 | 0,75     | 0,83   | 0,79 |
| NÃO | 10/15 | 5/15 | 3/13 | 10/13 | 0,77     | 0,67   | 0,72 |
## Questão 06. Considere agora uma matriz de confusão obtida por meio de uma Árvore de Decisão para um problema genérico de classificação de quatro classes:

Quais são os valores das métricas abaixo para cada uma das classes A, B, C e D?

|     | TVP   | TFN   | TFP  | TVN   | Precisão | Recall | F1   |
| --- | ----- | ----- | ---- | ----- | -------- | ------ | ---- |
| A   | 10/17 | 7/17  | 7/17 | 10/17 | 0,58     | 0,58   | 0,59 |
| B   | 15/18 | 3/18  | 8/23 | 15/23 | 0,65     | 0,83   | 0,73 |
| C   | 20/30 | 10/30 | 6/26 | 20/26 | 0,76     | 0,66   | 0,72 |
| D   | 50/57 | 7/57  | 6/56 | 50/56 | 0,89     | 0,87   | 0,89 |
