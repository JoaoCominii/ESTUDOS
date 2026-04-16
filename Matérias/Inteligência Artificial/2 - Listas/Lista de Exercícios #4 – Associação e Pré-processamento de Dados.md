# Lista de Exercícios #4 – Associação e Pré-processamento de Dados - João Comini Cesar de Andrade

### Questão 01. Considere que em um supermercado foram efetuadas as seguintes transações:
| Transação | Itens |
| :---: | :--- |
| 1 | Detergente, Limão, Feijão |
| 2 | Álcool, Detergente, Limão |
| 3 | Álcool, Detergente, Arroz |
| 4 | Limão |
| 5 | Limão |
| 6 | Álcool, Limão, Arroz, Feijão |
| 7 | Álcool, Detergente, Limão |
Utilizando o algoritmo Apriori com um suporte mínimo de 0.3 e confiança mínima de 0.6, gere:
1. Os conjuntos de itens (itemsets) frequentes, com seus respectivos suportes

| N   | Detergente | Limão | Feijão | Álcool | Arroz |
| --- | ---------- | ----- | ------ | ------ | ----- |
| 1   | Sim        | Sim   | Sim    | Não    | Não   |
| 2   | Sim        | Sim   | Não    | Sim    | Não   |
| 3   | Sim        | Não   | Não    | Sim    | Sim   |
| 4   | Não        | Sim   | Não    | Não    | Não   |
| 5   | Não        | Sim   | Não    | Não    | Não   |
| 6   | Não        | Sim   | Sim    | Sim    | Sim   |
| 7   | Sim        | Sim   | Não    | Sim    | Não   |
Suporte de uma regra é o número de transações que contem todos os itens da transação dividido pelo número total de transações.

Itemset = 1

| Produto    | Suporte    |
| ---------- | ---------- |
| Detergente | 4/7 = 0,57 |
| Limão      | 6/7 = 0,86 |
| Feijão     | 2/7 = 0,29 |
| Álcool     | 4/7 = 0,57 |
| Arroz      | 2/7 = 0,29 |
Como suporte mínimo é igual a 0,3, Feijão e Arroz serão elimados.

Itemset = 2


| Produto            | Suporte    |
| ------------------ | ---------- |
| Detergente, Limão  | 3/7 = 0,43 |
| Detergente, Álcool | 3/7 = 0,43 |
| Limão, Álcool      | 3/7 = 0,43 |
Todos tiveram suporte maior que 0,3.

Itemset = 3

| Produto                   | Suporte    |
| ------------------------- | ---------- |
| Detergente, Limão, Álcool | 2/7 = 0,29 |
Como o trio foi eliminado, o algoritmo para de buscar combinações por aqui.

Conjuntos que passaram no teste de suporte mínimo:

| Produto            | Suporte    |
| ------------------ | ---------- |
| Detergente         | 4/7 = 0,57 |
| Limão              | 6/7 = 0,86 |
| Álcool             | 4/7 = 0,57 |
| Detergente, Limão  | 3/7 = 0,43 |
| Detergente, Álcool | 3/7 = 0,43 |
| Limão, Álcool      | 3/7 = 0,43 |

2. As regras de associação derivadas, com os respectivos valores de confiança e lift 
Para gerar as regras, usamos apenas os conjuntos com 2 ou mais itens (os pares que encontramos). A regra de associação tem o formato Se A, então B (A $\rightarrow$ B).
Precisamos calcular a Confiança e o Lift para cada possível regra e eliminar as que tiverem Confiança menor que 0,6. (Os com confiança menor que 0,6 não precisa calcular o Lift)
Fórmulas:
- **Confiança (A $\rightarrow$ B):** Suporte(A e B) / Suporte(A)
- **Lift (A $\rightarrow$ B):** Confiança(A $\rightarrow$ B) / Suporte(B)

| Produto                         | Confiança                                                     | Lift               |
| ------------------------------- | ------------------------------------------------------------- | ------------------ |
| Detergente $\rightarrow$ Limão  | Sup(Detergente e Limão) / Sup(Detergente) = 3/7 / 4/7 = 0,75  | 0,75 / 0,86 = 0,87 |
| Limão $\rightarrow$ Detergente  | Sup(Detergente e Limão) / Sup(Limão) = 3/7 / 6/7 = 0,5        | Eliminada          |
| Detergente $\rightarrow$ Álcool | Sup(Detergente e Álcool) / Sup(Detergente) = 3/7 / 4/7 = 0,75 | 0,75 / 0,57 = 1,31 |
| Álcool $\rightarrow$ Detergente | Sup(Detergente e Álcool) / Sup(Álcool) = 3/7 / 4/7 = 0,75     | 0,75 / 0,57 = 1,31 |
| Álcool $\rightarrow$ Limão      | Sup(Limão e Álcool) / Sup(Álcool) = 3/7 / 4/7 = 0,75          | 0,75 / 0,86 = 0,87 |
| Limão $\rightarrow$ Álcool      | Sup(Limão e Álcool) / Sup(Limão) = 3/7 / 6/7 = 0,5            | Eliminada          |
### Questão 02. Sobre técnicas de pré-processamento de dados, responda:
1. Quando falamos de balanceamento de dados, qual a diferença entre as técnicas de oversampling e undersampling?
	R:  Vários algoritmos de Aprendizado de Máquina têm seu desempenho prejudicado na presença de dados desbalanceados, quando alimentados com dados desbalanceados, esses algoritmos tendem a favorecer a classificação de dados na classe majoritária. Por isso temos as técnicas para redefinir o tamanho do conjunto de dados que são o oversampling e undersampling para resolver esse problema. Quando adicionamos instâncias a classe minoritária temos o método de oversampling, quando removemos instâncias da classe majoritária temos o método de undersampling.
	Problemas do método Oversampling:
	- Existe o risco de que as instâncias adicionadas representarem situações muito inusuais que quase nunca ocorrerão, induzindo o modelo a um treinamento inadequado.
	- Pode trazer o problema de overfitting, em que o modelo é superajustado aos dados de treinamento e não consegue resolver novos problemas externos ao treinamento.
	Problemas do método Undersampling:
	- Quando dados da classe majoritária são eliminados é possível que dados de grande importância para o treinamento do modelo sejam perdidos.
	- Isso pode levar ao problema de underfitting, em que o modelo não se ajusta aos dados de treinamento.
2. Para o tratamento de dados ausentes, qual a estratégia mais recomendada quando não podemos/queremos remover instâncias e atributos do conjunto de dados?
	R: Se não podemos ou queremos remover instâncias do conjunto de dados o que podemos fazer é definir e preencher valores para os atributos com dados ausentes, o que não é muito recomendado se a quantidade de instâncias ou atributos com valores ausentes for muito grande pois vai demandar um tempo humano impraticável. Outra alternativa é utilizar algoritmos de Aprendizado de Máquina que lidam internamente com valores ausentes, como o Missforest e KNNimputer, mas a forma mais recomendada de se lidar com esse valores ausentes é utilizando algum método ou heurística para definir automaticamente valores para os atributos ausentes, pode ser definido utilizando moda, média dos valores dos atributos, assim podemos estimar de forma automática um valor um pouco maispreciso que não vai prejudicar o modelo.
3. Qual a principal forma de se verificar se um atributo é redundante sem saber se ele foi gerado a partir de outro atributo?
	R: Um atributo é redundante quando seu valor para todas as instâncias pode ser deduzido a partir do valor de um ou mais atributos, uma forma de verificar sem saber se um atributo foi gerado a partir de outro é usando a correlação, a redundância de um atributo está relacionada à sua correlação com um ou mais atributos do conjunto de dados dessa forma calculamos a correlação entre dois ou mais atributos e se essa correlação foi maior que 0,90 esses dados tem grandes chances de apresentar redundância..
4. O que é a binarização de um atributo e quando não é uma boa opção utilizá-la?
	R: A binarização de um atributo é a conversão de atributos simbólicos (nominais/categóricos) em valores numéricos binários. Isso é necessário porque muitos algoritmos de aprendizado de máquina, como redes neurais e SVMs, só processam dados numéricos. Existem diversas técnicas para se fazer essa conversão como para atributos binários simplesmente atribuir 0 para uma classe 1 para a outra, para atributos com diversas classes de saída sem uma ordem natural de hierarquia usa-se a codificação 1-de-c, quando o atributo possui $c$ categorias diferentes e não há uma relação de ordem entre elas, ele é transformado em uma sequência de $c$ bits. Na prática, um único atributo com várias opções se divide em várias novas entradas na base de dados. Para cada registro, apenas a posição correspondente à sua categoria recebe o valor 1, enquanto todas as outras recebem 0.
	Não é uma boa opção para se utilizar quando o atributo a ser convertido tem muitas opções de saída, um exemplo prático: imagine tentar criar uma codificação desse tipo para representar 193 países. Você precisaria criar 193 novos atributos (bits) na sua base de dados apenas para substituir aquele único atributo original. Isso geraria uma base de dados gigantesca e esparsa (cheia de zeros).
5. O que é a discretização de um atributo e quais as principais estratégias para realizá-la?
	R: A discretização de um atributo é a conversão de um atributo numérico em um atributo categórico. O motivo para isso é que algumas técnicas de AM foram desenvolvidas para trabalhar apenas com atributos categóricos ou tem seu desempenho reduzido quando trabalhando com atributos numéricos. Quando um atributo quantitativo é discretizado, o conjunto de possíveis valores é dividido em intervalos, e cada intervalo de valores quantitativos é convertido em um valor qualitativo. Os métodos para essa discretização podem ser supervisionados ou não, os supervisionados geralmente levam a melhores resultados, uma vez que a definição de intervalos sem conhecimento das classes pode levar a uma mistura das classes. Uma abordagem supervisionada seria escolher pontos corte que maximizam a pureza dos intervalos com entropia por exemplo. 
	Algumas das técnicas utilizadas são:
	- Larguras iguais: Divide o intervalo original de valores em subintervalos todos da mesma largura, o problema é que essa estratégia pode ser afetada na presença de outliers.
	- Frequências iguais: Atribui o mesmo número de objetos a cada subintervalo, essa estratégia pode levar a intervalos com tamanhos muito diferentes.
	- Uso de um algoritmo de agrupamento de dados
	- Inspeção visual
6. Qual a diferença entre as normalizações por reescala (MinMax) e por padronização (Z-score)?
	R: Algumas vezes o valor numérico de um atributo tem que ser transformado em outro valor numérico, isso ocorre quando os limite inferiores e superiores de valores dos atributos são muito distantes, o que pode levar a uma grande variação de resultados, ou quando vários atributos estão em escalas diferentes. Uma transformação que é muito utilizada nesses casos é a normalização por amplitude. Ela pode ser feita por reescala ou por padronização, a por reescala define uma nova escala de limites máximos e mínimos para todos atributos. 
	Para que os limites superior e inferior sejam 1 e 0, respectivamente, basta fazer max=1 e min=0, dessa forma todos os atributos serão normalizados dentro de um intervalo de 0 a 1 com o 1 sendo o atributo de maior valor e o 0 de menor valor. Para a normalização por padronização para cada valor do atributo é adicionada ou removida uma medida de localização e o valor resultante é multiplicado ou dividido por uma medida de escala. Dessa forma diferentes atributos podem ter limites superiores e inferiores diferentes mas terão mesma medida de escala e espelhamento. Qual a melhor forma de normalização vai depender muito de caso a caso, geralmente a por reescala é preferível por lidar melhor com casos de outliers. 