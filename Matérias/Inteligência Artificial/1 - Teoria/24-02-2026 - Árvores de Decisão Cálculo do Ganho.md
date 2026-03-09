# 📘 Aula 03 - Árvores de Decisão Cálculo do Ganho
## 📄 Slides da aula
![[4 - Árvores de Decisão - Como Calcular.pdf]]
## Entropia e Ganho de Informação
### O que são e como calcular?
Entropia -> medida da incerteza de uma variável aleatória
- A quantidade média de informação ou surpresa de um evento aleatório X
- Um evento certo (p(x) = 1) não traz novas informações dessa forma a entropia é zero.
- Um evento com probabilidade alta traz mais informações e tem uma entropia maior.
- Exemplo: Saber que “vai chover no deserto do Saara” (raro) traz mais informação do que “vai chover na Amazônia”(evento esperado)
## E nas Árvores de Decisão?
![[Pasted image 20260305150340.png]]
A função IMPORTÂNCIA deverá ter seu valor máximo quando o atributo for perfeito e mínimo quando o atributo for totalmente irrelevante.
- A equação de entropia foi adaptada para medir incerteza de classificação em algoritmos como ID3, C4.5 e seus sucessores.
![[Pasted image 20260305150459.png]]
- Qualquer atributo A com valores distintos d divide o conjunto de treinamento E em subconjuntos $E_1, \dots, E_d$ de acordo com os valores de cada atributo
	- É como se criasse d "pastas" de dados, uma para cada valor do atributo
	- Cada subconjunto $E_k$ tem $P_k$ exemplos positivos e $n_k$ exemplos negativos  
	- Precisamos ver o impacto de cada subconjunto no todo
- O ganho de informação a partir do teste de atributo é a diferença entre a entropia original e a entropia do atributo
![[Pasted image 20260305150925.png]]

## Algoritmo ID3
Constrói a árvore de decisão usando entropia e ganho de informação
É recursivo e baseado em busca gulosa, procurando o conjunto de atributos que melhor dividem as amostras gerando sub-árvores
### Limitações
- Não lida com atributos contínuos
- Não lida com valores desconhecidos, todos os atributos do conjunto de treinamento devem ter valores conhecidos
- Não lida com nenhum mecanismo de pós-poda, que poderia amenizar em árvores mais complexas

## Algoritmo C4.5
Uma extensão do ID3, a ideia era lidar com as limitações existentes no algoritmo anterior.
- Lidar com atributos contínuos e discretos
	- Para atributos numéricos, o algoritmo define um limiar e divide os dados em dois grupos: valores maiores que o limiar e valores menores ou iguais ao limiar
- Poda de árvores após a criação
	- O algoritmo retrocede na árvore após criada e tenta remover ramificações que não ajudam no processo de decisão, substituindo-os por nós folha
- Implementa a Razão de Ganho no lugar do ganho de informações tradicional
	 - O ID3 tinha um problema em que ele favorecia atributos com muitos valores diferentes(Ex : CPF), o que gerava árvores inúteis
	 - A Razão de Ganho foi introduzida para corrigir o viés do Ganho de Informação: ela normaliza o ganho de informação ao considerar o número de valores distintos de um atributo
	![[Pasted image 20260305151933.png]]
	O C4.5 resolve isso normalizando o ganho. Ele divide o ganho de informação por uma métrica chamada *SplitInfo* (Informação de Divisão).

## Algoritmo CART
CART – Classification and Regression Trees
 - É amplamente utilizado tanto para tarefas de classificação quanto para regressão
 - Constrói árvores binárias (cada nó interno tem exatamente dois filhos)
 - Utiliza critérios como o índice de Gini (classificação) ou o erro quadrático médio (regressão) para determinar as divisões nos nós