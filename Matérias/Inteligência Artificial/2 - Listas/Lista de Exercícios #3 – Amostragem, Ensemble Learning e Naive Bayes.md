
# Lista de Exercícios #3 – Amostragem, Ensemble Learning e Naive Bayes- João Comini Cesar de Andrade

### Questão 01. Sobre os conceitos de métodos de amostragem, responda:
1. Qual a importância de separar um conjunto de dados em conjuntos de treinamento e de teste?
	R: Se o conjunto de treinamento for igual ao conjunto de testes temos uma estimativa muito otimista da previsão assim para resolver esse viés otimista precisamos garantir que não tenham elementos em comum entre o conjunto de testes e o conjunto de treinamento.
2. Qual a principal limitação do método *holdout* em relação à partição dos dados e de que forma a Amostragem Aleatória busca mitigar esse problema? 
	R: O método *holdout* divide o conjunto de dados em uma proporção de p para treino e 1-p para testes, esse particionamento deve ser feito de forma que o conjunto de treinamento seja sempre maior que o de teste. A principal limitação desse método é que dependendo da forma que o conjunto de dados for particionado pode acontecer de os exemplos mais simples e fáceis caírem no conjunto de teste. Uma forma de mitigar esse problema é através da amostragem aleatória. Nesse método, o *holdout* é aplicado múltiplas vezes, criando várias partições diferentes do conjunto de dados. Isso reduz a variação do erro, já que o modelo é avaliado em diversas instâncias de teste distintas. Por fim, o erro final é calculado como a média dos erros de todas as *r* hipóteses induzidas.
3. Em um cenário de 5-fold *cross-validation* (validação cruzada), qual a porcentagem média de exemplos compartilhados entre os diferentes subconjuntos de treinamento?
	R: Na validação cruzada o conjunto de exemplos é dividido em r subconjuntos (*folds*) de tamanho aproximadamente igual, este processo é repetido r vezes, utilizando em cada ciclo uma partição diferente para teste. O problema desse método é que aproximadamente 80% dos dados é compartilhada entre os subconjuntos de treinamento
4. Qual a principal diferença entre os métodos de Amostragem Aleatória e Bootstrap?
	R: No *Bootstrap* são gerados *r* subconjuntos de treinamento a partir do conjunto de dados original da mesma forma que a Amostragem Aleatória, o diferencial desse método no entanto é que os conjuntos são formados aleatoriamente com reposição, ou seja um exemplo pode estar presente em mais de um subconjunto de treinamento. Aqui os exemplos que não aparecerem no treinamento (não foram amostrados) se tornam o conjunto de teste.
5. Em métodos de aprendizado de máquina, qual a diferença entre as definições de “parâmetros” e “hiperparâmetros”? Cite exemplos no contexto de árvores de decisão.
	R: Os parâmetros: são aprendidos automaticamente pelo modelo a partir dos dados durante o treinamento, como os nós de decisão, atributos de divisão etc. Já os hiperparâmetros: são definidos manualmente pelo cientista de dados antes do início do treinamento, como a profundidade máxima, critério de divisão e etc.
6. Qual a importância de um conjunto de validação distinto do conjunto de teste final?
	R: Em alguns casos precisamos realizar um ajuste dos hiperparâmetros de um indutor, queremos definir quais seriam os melhores valores para esses hiperparâmetros. Nesses casos precisamos reservar uma parte do conjunto de dados para ajustar os hiperparâmetros, um conjunto de validação e uma outra parte para ser o conjunto de testes.

### Questão 02. Uma pessoa cientista de dados treinou um modelo de classificação utilizando ressubstituição para avaliação e obteve acurácia de 100%. Entretanto, ao aplicar o modelo em novos dados, a acurácia caiu para 62%. Explique por que isso ocorreu e proponha uma estratégia de avaliação mais adequada.
No método da ressubstituição se utiliza o mesmo conjunto de dados para teste e treinamento, o que traz uma acurácia de 100% pelo modelo já conhecer o conjunto de exemplos previamente ao teste porém quando diante de um novo conjunto de exemplos a sua acurácia cai drasticamente. Como o nosso objetivo é treinar um modelo para ter uma alta taxa de acertos em conjuntos desconhecidos de teste essa acurácia de 62% não é boa, assim uma forma de melhorar essa precisão seria utilizando um método diferente que tem maior assertividade como a Amostragem Aleatória.

### Questão 03. Considere um modelo avaliado por 10-fold cross-validation. Se o conjunto de dados possui 200 exemplos, quantos exemplos são usados para teste em cada iteração?

r = 10  n = 200 
Teste -> n/r -> 200 / 10 = 20 exemplos usados para teste
Treinamento -> n(r-1)/r -> 200 * 9 / 10  = 180 exemplos usados para treinamento

### Questão 04. Sobre métodos de ensemble, responda:
1. Explique a razão pela qual métodos ensemble tendem a apresentar melhor desempenho do que classificadores individuais.
	R: Nos métodos ensemble um grupo de modelos individuais chamados modelos base são combinados para tomar uma decisão geral e mais assertiva. Eles tendem a apresentar melhor desempenho porque os classificadores base são independentes entre si e cometem erros em instâncias diferentes do conjunto de dados. Assim, quando suas previsões são agregadas (por votação majoritária ou média, por exemplo), os acertos da maioria compensam os erros isolados, resultando em uma classificação mais correta e robusta.
2. Quais os principais tipos de métodos ensemble e qual a principal diferença entre eles?
	R: Os principais tipos de métodos ensemble são *Bagging*, *Boosting* e *Stacking*. O *Bagging* ou *bootstrap aggregating* foca na redução da variância dos modelos base, criando múltiplos subconjuntos do conjunto de treino igual ao *Bootstrap* e treinando um indutor em cada subconjunto de forma independente e simultânea. O *Boosting* treina um modelo simples, analisa o que ele errou e treina um novo modelo dando um peso maior aos exemplos classificados incorretamente pelo anterior e então repete o processo varias vezes para depois juntar todos os modelos em uma única previsão final. Já o *Stacking* combina diversos modelos base através de um modelo final que induz a melhor forma de combiná-los.
3. Como o algoritmo Random Forest garante que as árvores criadas sejam diferentes entre si, além do uso de Bootstrap?
	R: O Random Forest usa a estratégia do *Bagging* que tem o problema tradicional de que se um atributo for muito forte, todas as arvores tendem a escolhe-lo para a raiz e assim as arvores ficam muito parecidas. Para resolver esse problema o método da Random Forest possui duas formas de aleatoriedade, o bootstrap nos dados e formar um subconjunto aleatório de atributos em cada divisão da árvore.

### Questão 05. Em um ensemble baseado em bagging, dez classificadores retornaram as seguintes previsões:

| Classificador | Previsão |
| ------------- | -------- |
| C1            | Positivo |
| C2            | Positivo |
| C3            | Negativo |
| C4            | Positivo |
| C5            | Negativo |
| C6            | Negativo |
| C7            | Positivo |
| C8            | Negativo |
| C9            | Negativo |
| C10           | Negativo |
### Qual será a predição final? Justifique sua resposta.
Como é um problema de classificação a predição final será dada pela classe majoritária das previsões, nesse caso a previsão final será Negativo por ter tido 6 predições contra 4 Positivo.

### Questão 06. Sobre Naive Bayes, responda:
1. Por que o classificador Naive Bayes é chamado de “ingênuo”? Explique a suposição de independência e discuta se ela é realista em problemas do mundo real.
	R: O classificador recebe esse nome porque faz uma suposição muito forte: ele assume que todos os atributos de um conjunto de dados são completamente independentes entre si, desde que saibamos a qual classe eles pertencem. Se tentássemos calcular a probabilidade de um evento considerando todas as interações e combinações possíveis entre dezenas de atributos, o problema sofreria uma "explosão combinatória" e ficaria difícil de resolver. Para contornar isso, o algoritmo é "ingênuo" e simplifica a matemática. Ele calcula a probabilidade final apenas multiplicando as probabilidades individuais de cada atributo. Na grande maioria das vezes ele não é realista no mundo real já que as características das coisas raramente são totalmente independentes; elas costumam estar interligadas de alguma forma.
2. Como o Naive Bayes lida com atributos numéricos (contínuos) em comparação com atributos categóricos? 
	R: Para lidar com os atributos nominais(categóricos) basta manter um contador para cada valor de atributo por classe. Para lidar com os atributos contínuos o Naive Bayes precisa assumir uma distribuição para valores e discretizar valores.

### Questão 07. Uma loja de eletrônicos deseja prever se um cliente comprará um produto com base em sua idade, renda e se é estudante ou não. O histórico de clientes desta loja é dado pela tabela:
| Cliente | Idade       | Renda | Estudante | Compra |
|--------|------------|------|-----------|--------|
| c1     | Jovem      | Alta | Não       | Não    |
| c2     | Jovem      | Alta | Não       | Não    |
| c3     | Meia-idade | Alta | Não       | Sim    |
| c4     | Idoso      | Média| Não       | Sim    |
| c5     | Idoso      | Baixa| Sim       | Sim    |
| c6     | Idoso      | Baixa| Sim       | Não    |
| c7     | Meia-idade | Baixa| Sim       | Sim    |
| c8     | Jovem      | Média| Não       | Não    |
| c9     | Jovem      | Baixa| Sim       | Sim    |
| c10    | Idoso      | Média| Sim       | Sim    |
### Dado um novo cliente que é jovem, tem renda média e é estudante, qual será a predição realizada por um classificador Naive Bayes? Mostre os cálculos das probabilidades e indique a classe final.

| Cliente | Idade | Renda | Estudante | Compra |
| ------- | ----- | ----- | --------- | ------ |
| c11     | Jovem | Média | Sim       | ?      |


<table>
<tr>
  <th rowspan="2">Calcular P(Classe) para cada classe</th>
  <th colspan="3">Idade</th>
  <th colspan="3">Renda</th>
  <th colspan="2">Estudante</th>
</tr>
<tr>
  <th>Jovem</th>
  <th>Meia-idade</th>
  <th>Idoso</th>
  <th>Alta</th>
  <th>Média</th>
  <th>Baixa</th>
  <th>Sim</th>
  <th>Não</th>
</tr>
<tr>
  <td>Compra = Sim (6/10)</td>
  <td>1/6</td><td>2/6</td><td>3/6</td>
  <td>1/6</td><td>2/6</td><td>3/6</td>
  <td>4/6</td><td>2/6</td>
</tr>
<tr>
  <td>Compra = Não (4/10)</td>
  <td>3/4</td><td>0/4</td><td>1/4</td>
  <td>2/4</td><td>1/4</td><td>1/4</td>
  <td>1/4</td><td>3/4</td>
</tr>
</table>

P(Compra = Sim | atributos):
P(Compra = Sim) = 6/10
P(Idade = Jovem | Compra = Sim) = 1/6
P(Renda = Média | Compra = Sim) = 2/6
P(Estudande = Sim | Compra = Sim) = 2/6

P(Compra = Sim | atributos) = 6/10 * 1/6 * 2/6 * 2/6 =  0,0111

P(Compra = Não | atributos):
P(Compra = Não) = 4/10 
P(Idade = Jovem | Compra = Não) = 3/4
P(Renda = Média | Compra = Não) = 1/4
P(Estudante = Sim | Compra = Não) = 3/4

P(Compra = Não | atributos) = 4/10 * 3/4 * 1/4 * 3/4 = 0,05625

Então a classe final Compra será Não.