
# 📘 Aula 02 - Introdução ao Aprendizado de Máquina

## 📄 Slides da aula
![[2 - Introdução ao Aprendizado de Máquina.pdf]]

## ❓O que é o Aprendizado de Maquina (AM) ?
### Algumas definições:
- A capacidade de melhorar o desempenho na realização de alguma tarefa por meio da experiência.” Mitchell (1997)
- Processo que permite aos computadores aprenderem autonomamente, identificando padrões e tomando decisões baseadas em dados.” Enciclopédia Britannica (2026)

![[Pasted image 20260224141927.png]]
### Indução de Hipóteses
#### ❓Como aprender autonomamente?
- Indução de hipótese (regra ou função) a partir de dados (Treinamento por meio de exemplos)
- É desejável que a hipótese seja válida para outras instâncias do problema que não foram vistas ainda (Teste se a máquina consegue induzir respostas corretamente através da novidade depois de treinada)
#### Na prática
Aprovação de empréstimo: Imagine que um banco quer prever se deve **aprovar ou rejeitar** um pedido de empréstimo com base em alguns atributos do cliente.
❓ Quais atributos seriam importantes de serem avaliados? 
R: Valor da dívida, renda, idade, histórico, etc.
##### Primeiro é feito o treinamento com um conjunto de dados já com a resposta de se foi aprovado ou não o emprestimo.
![[Pasted image 20260224142523.png]]

##### Depois do treinamento é feito o teste com um cliente novo para identificar se a IA consegue induzir a resposta corretamente.![[Pasted image 20260224142627.png]]
![[Pasted image 20260224142648.png]]
**Generalização:** A hipótese deve ser válida para outras instâncias do problema que não foram vistas ainda.
- **Overfitting** -> hipótese memorizou/especializou nos dados de treinamento.
- **Underfitting** -> hipótese apresenta baixa taxa de acerto até mesmo no treinamento.
##### 📚 Tarefas de Aprendizado
- **Preditivas** -> Encontrar uma função(hipótese) a partir dos dados de treinamento para prever um rótulo ou valor com base nos atributos de uma nova entrada inédita.
	- Algoritmos seguem o paradigma do **Aprendizado Supervisionado** -> Existe um supervisor externo que conhece a saída (o atributo de saída) desejada para cada um dos exemplos de teste da IA(depois dela já ter sido treinada).
- **Descritivas** -> Explorar ou descrever um conjunto de dados.
	- Algoritmos seguem o paradigma do **Aprendizado Não supervisionado** -> Dessa forma não fazem uso de um atributo de saída
![[Pasted image 20260224144216.png]]

### Aprendizado Supervisionado - Classificação e Regressão
##### [[Classificação]]
Objetivo -> dado um exemplo não rotulado, atribui uma das possíveis **classes**(rótulos)
- Classes são sempre **atributos categóricos (nominais)**
- Essa é uma das tarefas mais comuns no Aprendizado de Máquina.
![[Pasted image 20260224144512.png]]

##### [[Regressão]]
Semelhante a classificação.
Objetivo -> dado um exemplo não rotulado, atribui uma dos possíveis **valores** (rótulos)
- Classes são sempre atributos **numéricos**. 
![[Pasted image 20260224144805.png]]

### Aprendizado Não Supervisionado - Agrupamento e Associação
##### Agrupamento (*clustering*)
Objetivo -> agrupar as instâncias de acordo com a similaridade entre as instâncias (a partir das *features*)
- Não existe rótulo/atributo de saída
- É muito usado para descobrir perfis dentro do conjunto de dados
![[Pasted image 20260224145109.png]]
- Grupo A: baixa renda maior risco - c1,c3,c6
- Grupo B: média/alta renda, bom histórico - c2,c4,c7
- Grupo C: alta renda mas alto endividamento - c5

##### Associação
Obejtivo -> busca de **padrões frequentes** de associações entre atributos de um mesmo conjunto de dados
- Features são sempre **categóricas**. 
**Exemplo:** em sistemas de e-commerce, descobrir regras do tipo “quem compra produto A, também compra produto B”
![[Pasted image 20260224145439.png]]
![[Pasted image 20260224145446.png]]
![[Pasted image 20260224145523.png]]
