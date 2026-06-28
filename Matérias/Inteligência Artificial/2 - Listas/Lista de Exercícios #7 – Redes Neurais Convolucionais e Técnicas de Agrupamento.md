# Lista de Exercícios #7 – Redes Neurais Convolucionais e Técnicas de Agrupamento - João Comini Cesar de Andrade

### Questão 01. Sobre os principais conceitos de redes neurais convolucionais (CNNs), responda:

1. Cite e explique os principais problemas de se usar uma rede MLP (totalmente conectada) para classificar imagens.
    
    R: O principal problema é que o MLP não considera a estrutura espacial da imagem; o pixel que compõe o olho de um gato, por exemplo, é tratado da mesma forma desconectada que o pixel de uma pata. Além disso, pelo fato de cada pixel virar um nó de entrada obrigatoriamente conectado a todos os nós intermediários, ocorre uma explosão combinatória: o modelo precisa aprender milhões de pesos, tornando o processo computacionalmente muito caro e ineficiente.
    
2. Descreva, na ordem correta, as quatro etapas principais da arquitetura de uma CNN e diga qual é a função de cada uma.
    
    R: As quatro etapas principais são:
    

- **1. Convolução:** Etapa de extração de características (features), onde filtros matemáticos (kernels) são passados pela imagem procurando padrões como linhas, bordas ou formas complexas.
    
- **2. Pooling:** Etapa de redução da dimensionalidade que seleciona as características mais relevantes e as resume, ajudando na identificação do padrão independentemente da sua exata posição.
    
- **3. Flattening:** Etapa de preparação que transforma as matrizes multidimensionais em um vetor 1D linear contínuo.
    
- **4. Rede Neural Densa:** Etapa de classificação que recebe a lista de características finais e calcula as combinações e as probabilidades matemáticas para a decisão da predição final.
    

3. O que é um kernel (filtro) na etapa de convolução e o que é o feature map (mapa de características)? Por que diferentes kernels produzem resultados diferentes?
    
    R: Um kernel é uma pequena matriz matemática que funciona como um detector de características (como uma "lupa") que desliza e processa pedaços da imagem original buscando padrões específicos. O _feature map_ (mapa de características) é a matriz resultante dessas operações matemáticas aplicadas pelo kernel, que agora exibe e mapeia a presença dos padrões que foram identificados. Diferentes kernels produzem resultados diferentes porque as matrizes numéricas que os compõem são distintas, o que faz com que cada um realize uma operação específica — um pode ser programado para achar apenas linhas verticais, outro para desfocar a imagem, e outro para destacar apenas os contornos (bordas).
    
4. Explique a diferença entre max pooling e average pooling. Considere a seguinte região $2\times2$ de um feature map e calcule o resultado para cada tipo de pooling: (29, 15, 0, 100).
    
    R: O max pooling funciona selecionando e mantendo sempre o valor máximo contido na região escaneada, enquanto o average pooling agrupa e calcula a média aritmética de todos os valores dentro daquela região determinada.
    

- **Max Pooling:** $\max(29, 15, 0, 100) = 100$.
    
- **Average Pooling:** $\frac{(29 + 15 + 0 + 100)}{4} = \frac{144}{4} = 36$.
    

5. Qual é a função da etapa de flattening dentro da CNN? Por que ela é necessária antes da rede densa?
    
    R: A etapa de flattening serve puramente para transformar o conjunto de matrizes numéricas reduzidas em um único vetor achatado em formato de linha reta (1D). Ela é obrigatória porque as redes neurais tradicionais (Redes Densas ou Multilayer Perceptrons) não são projetadas para ler matrizes quadradas nativamente, exigindo que os dados de entrada cheguem através de um formato unidimensional.
    

### Questão 02. Em relação às técnicas de agrupamento vistas em sala, responda:

1. O objetivo do agrupamento é organizar os dados em grupos. Descreva, com suas palavras, as duas propriedades que um bom agrupamento deve satisfazer em relação à similaridade dos objetos (dentro e entre grupos).
    
    R: Um bom agrupamento deve garantir que os dados contidos dentro do mesmo grupo sejam altamente similares entre si, mantendo uma grande coesão interna. De forma complementar, a segunda propriedade exige que a similaridade entre dados de grupos distintos seja a menor possível, estabelecendo uma forte separação e isolamento entre grupos diferentes.
    
2. Um mesmo conjunto de objetos pode ser agrupado de maneiras diferentes dependendo do critério escolhido. O que isso revela sobre a natureza do problema de agrupamento? Existe uma única resposta "correta"? Por quê?
    
    R: Isso revela que o agrupamento é uma técnica estritamente descritiva (não supervisionada), servindo fundamentalmente para descobrir padrões dependendo das abordagens selecionadas. Não existe uma única resposta "correta" universal, pois o formato que os dados irão assumir e as similaridades encontradas dependem do critério de interesse, da perspectiva adotada pelo analista e da definição prévia das medidas de distância de interesse.
    
3. Cite pelo menos três limitações do algoritmo K-Means e, para cada uma delas, indique uma estratégia ou técnica para contorná-la.
    
    R: Três limitações abordadas são:
    

- **Exigência de predefinir o parâmetro K (quantidade de grupos):** O algoritmo não descobre as partições naturalmente. A estratégia para contornar é aplicar o _Método do Cotovelo_ (Elbow Method), testando diferentes valores e visualizando o momento de menor variação do erro.
    
- **Sensibilidade a formatos geométricos dos grupos:** Ele assume e prefere que os dados apresentem uma divisão estritamente esférica. Pode ser contornado trocando o algoritmo por métodos focados em densidade de formas irregulares, como o DBSCAN.
    
- **Sensibilidade à presença de Outliers:** Picos e dados irregulares na amostra distorcem os cálculos matemáticos dos centroides e puxam o núcleo falsamente. A técnica para evitar isso é identificar os outliers de antemão através da aplicação de métodos como o Boxplot ou amplitude interquartil (IQR), removendo-os da amostra antes de treinar o K-Means.
    

4. O Sum of Squared Error (SSE) é uma métrica de qualidade de agrupamento. Explique o que ele mede e por que não faz sentido escolher o número de grupos k simplesmente buscando o menor SSE possível. Como o Método do Cotovelo (Elbow Method) ajuda nessa decisão?
    
    R: O SSE é uma métrica de validação interna que avalia a compactação dos grupos ao medir e somar as distâncias ao quadrado entre cada ponto de dado e o centroide correspondente ao seu próprio grupo. Não faz sentido simplesmente escolher o K que gere o menor SSE possível porque a métrica cairá infinitamente se aumentarmos a quantidade K; no limite, se transformarmos cada ponto único de dado em um cluster exclusivo, teremos matematicamente $SSE = 0$, o que arruína a finalidade e sentido da técnica. O Método do Cotovelo ajuda nesta visualização porque mapeia o erro de várias iterações lado a lado e permite observar através do "cotovelo" da curva o exato momento em que o incremento de um novo grupo já parou de produzir reduções e aprimoramentos realmente significativos no SSE.
    
5. O Silhouette Index de um ponto pode variar entre -1 e 1. Interprete o significado de um valor próximo de 1, próximo de 0 e próximo de -1. Como obtemos um indice único para avaliar o agrupamento como um todo?
    
    R: O Silhouette Index mede se as instâncias estão enquadradas e localizadas corretamente nos seus grupos, representando as seguintes realidades:
    

- **Próximo de 1:** Indica que o elemento analisado se enquadra perfeitamente e se ajusta fortemente com o seu agrupamento, estando também bastante isolado dos grupos externos.
    
- **Próximo de 0:** Indica uma proximidade indefinida. O dado se encontra no limiar e na borda do limite entre a transição dos clusters.
    
- **Próximo de -1:** Reflete um elemento que pertence incorretamente ao cluster atual e que se assemelha e deveria pertencer muito mais ao cluster vizinho em virtude das distâncias.
    
    A validação unificada em um índice único ocorre tirando e calculando-se o Silhouette Index Global, que não é nada mais do que o cálculo final da média aritmética de todos os cálculos executados para os pontos da base de dados inteira.
    

### Questão 03. Faça este agrupamento usando o algoritmo K-Means com distância euclidiana. Mostre o passo a passo das duas primeiras iterações do algoritmo.

Para o algoritmo K-Means calcularemos a distância euclidiana entre cada ponto do usuário $U_{i}$ até os dois centroides fornecidos. A fórmula a ser utilizada é $D(x,y) = \sqrt{\sum (x_{i} - y_{i})^{2}}$.

Os centroides iniciais são:

- C1: (5, 3, 1, 1)
    
- C2: (3, 1, 5, 3)
    

**Iteração 1:**

Cálculo das distâncias e atribuições iniciais:

- **Usuário 1 (5, 3, 1, 1)**
    
    - D(U1, C1) = $\sqrt{(5-5)^2 + (3-3)^2 + (1-1)^2 + (1-1)^2}$ = $\sqrt{0}$ = 0
        
    - D(U1, C2) = $\sqrt{(5-3)^2 + (3-1)^2 + (1-5)^2 + (1-3)^2}$ = $\sqrt{4 + 4 + 16 + 4}$ = $\sqrt{28}$ $\approx$ 5.29
        
    - _Atribuição:_ Cluster 1
        
- **Usuário 2 (3, 1, 5, 3)**
    
    - D(U2, C1) = $\sqrt{(3-5)^2 + (1-3)^2 + (5-1)^2 + (3-1)^2}$ = $\sqrt{4 + 4 + 16 + 4}$ = $\sqrt{28}$ $\approx$ 5.29
        
    - D(U2, C2) = $\sqrt{(3-3)^2 + (1-1)^2 + (5-5)^2 + (3-3)^2}$ = $\sqrt{0}$ = 0
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 3 (2, 1, 5, 3)**
    
    - D(U3, C1) = $\sqrt{(2-5)^2 + (1-3)^2 + (5-1)^2 + (3-1)^2}$ = $\sqrt{9 + 4 + 16 + 4}$ = $\sqrt{33}$ $\approx$ 5.74
        
    - D(U3, C2) = $\sqrt{(2-3)^2 + (1-1)^2 + (5-5)^2 + (3-3)^2}$ = $\sqrt{1 + 0 + 0 + 0}$ = $\sqrt{1}$ = 1.00
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 4 (4, 3, 4, 2)**
    
    - D(U4, C1) = $\sqrt{(4-5)^2 + (3-3)^2 + (4-1)^2 + (2-1)^2}$ = $\sqrt{1 + 0 + 9 + 1}$ = $\sqrt{11}$ $\approx$ 3.32
        
    - D(U4, C2) = $\sqrt{(4-3)^2 + (3-1)^2 + (4-5)^2 + (2-3)^2}$ = $\sqrt{1 + 4 + 1 + 1}$ = $\sqrt{7}$ $\approx$ 2.65
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 5 (5, 5, 3, 1)**
    
    - D(U5, C1) = $\sqrt{(5-5)^2 + (5-3)^2 + (3-1)^2 + (1-1)^2}$ = $\sqrt{0 + 4 + 4 + 0}$ = $\sqrt{8}$ $\approx$ 2.83
        
    - D(U5, C2) = $\sqrt{(5-3)^2 + (5-1)^2 + (3-5)^2 + (1-3)^2}$ = $\sqrt{4 + 16 + 4 + 4}$ = $\sqrt{28}$ $\approx$ 5.29
        
    - _Atribuição:_ Cluster 1
        
- **Usuário 6 (3, 1, 5, 3)** (Nota: possui dados idênticos ao Usuário 2)
    
    - D(U6, C1) = $\sqrt{28}$ $\approx$ 5.29
        
    - D(U6, C2) = $\sqrt{0}$ = 0
        
    - _Atribuição:_ Cluster 2
        

**Resultados da Partição 1:**

- Grupo (Cluster) 1: {U1, U5}
    
- Grupo (Cluster) 2: {U2, U3, U4, U6}
    

Ajuste e Recálculo dos novos centroides:

- **C1'** = Média de U1 e U5 = $(\frac{5+5}{2}, \frac{3+5}{2}, \frac{1+3}{2}, \frac{1+1}{2})$ = **(5.0, 4.0, 2.0, 1.0)**
    
- **C2'** = Média de U2, U3, U4 e U6 = $(\frac{3+2+4+3}{4}, \frac{1+1+3+1}{4}, \frac{5+5+4+5}{4}, \frac{3+3+2+3}{4})$ = $(\frac{12}{4}, \frac{6}{4}, \frac{19}{4}, \frac{11}{4})$ = **(3.0, 1.5, 4.75, 2.75)**
    

**Iteração 2:**

Recalculando os pontos perante os novos centroides para validar a convergência:

- **Usuário 1 (5, 3, 1, 1)**
    
    - D(U1, C1') = $\sqrt{(5-5)^2 + (3-4)^2 + (1-2)^2 + (1-1)^2}$ = $\sqrt{0 + 1 + 1 + 0}$ = $\sqrt{2}$ $\approx$ 1.41
        
    - D(U1, C2') = $\sqrt{(5-3)^2 + (3-1.5)^2 + (1-4.75)^2 + (1-2.75)^2}$ = $\sqrt{4 + 2.25 + 14.06 + 3.06}$ $\approx$ 4.83
        
    - _Atribuição:_ Cluster 1
        
- **Usuário 2 (3, 1, 5, 3)**
    
    - D(U2, C1') = $\sqrt{(3-5)^2 + (1-4)^2 + (5-2)^2 + (3-1)^2}$ = $\sqrt{4 + 9 + 9 + 4}$ = $\sqrt{26}$ $\approx$ 5.10
        
    - D(U2, C2') = $\sqrt{(3-3)^2 + (1-1.5)^2 + (5-4.75)^2 + (3-2.75)^2}$ = $\sqrt{0 + 0.25 + 0.06 + 0.06}$ $\approx$ 0.61
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 3 (2, 1, 5, 3)**
    
    - D(U3, C1') = $\sqrt{(2-5)^2 + (1-4)^2 + (5-2)^2 + (3-1)^2}$ = $\sqrt{9 + 9 + 9 + 4}$ = $\sqrt{31}$ $\approx$ 5.57
        
    - D(U3, C2') = $\sqrt{(2-3)^2 + (1-1.5)^2 + (5-4.75)^2 + (3-2.75)^2}$ = $\sqrt{1 + 0.25 + 0.06 + 0.06}$ $\approx$ 1.17
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 4 (4, 3, 4, 2)**
    
    - D(U4, C1') = $\sqrt{(4-5)^2 + (3-4)^2 + (4-2)^2 + (2-1)^2}$ = $\sqrt{1 + 1 + 4 + 1}$ = $\sqrt{7}$ $\approx$ 2.65
        
    - D(U4, C2') = $\sqrt{(4-3)^2 + (3-1.5)^2 + (4-4.75)^2 + (2-2.75)^2}$ = $\sqrt{1 + 2.25 + 0.56 + 0.56}$ $\approx$ 2.09
        
    - _Atribuição:_ Cluster 2
        
- **Usuário 5 (5, 5, 3, 1)**
    
    - D(U5, C1') = $\sqrt{(5-5)^2 + (5-4)^2 + (3-2)^2 + (1-1)^2}$ = $\sqrt{0 + 1 + 1 + 0}$ = $\sqrt{2}$ $\approx$ 1.41
        
    - D(U5, C2') = $\sqrt{(5-3)^2 + (5-1.5)^2 + (3-4.75)^2 + (1-2.75)^2}$ = $\sqrt{4 + 12.25 + 3.06 + 3.06}$ $\approx$ 4.73
        
    - _Atribuição:_ Cluster 1
        
- **Usuário 6 (3, 1, 5, 3)**
    
    - D(U6, C1') = $\sqrt{26}$ $\approx$ 5.10
        
    - D(U6, C2') = $\sqrt{0.375}$ $\approx$ 0.61
        
    - _Atribuição:_ Cluster 2
        

**Resultados da Partição 2:**

Todos os usuários permaneceram alocados de forma rigorosa em seus respectivos Clusters da Iteração 1. Como as associações não sofreram nenhuma alteração, os centroides atuais estão estáveis e matematicamente o algoritmo encontrou a sua resposta final.