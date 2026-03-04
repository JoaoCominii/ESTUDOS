
# Lista de Exercícios #1 – Introdução à IA e ao Aprendizado de Máquina - João Comini Cesar de Andrade
### Questão 01. Pesquise e responda às perguntas abaixo sobre dois momentos históricos importantes para a Inteligência Artificial:
- Origem (1956 – Conferência de Dartmouth):
	1. Onde e quando ocorreu a conferência que é considerada o “nascimento” da IA?
		R: Ela ocorreu em 1956 na universidade Dartmouth College. 
	2.  Quem foram os principais participantes e qual foi a proposta central apresentada no evento?
		R: A proposta principal do evento foi apresentada por John McCarthy e era de que todo aspecto da inteligência pode ser descrito com tanta precisão que uma maquina pode ser construída para simulá-lo. Os principais participantes do evento foram: John McCarthy, Marvin Minsky, Claude Shannon, Nathaniel Rochester, Ray Solomonoff e Oliver Selfridge.
- 50 anos depois (2006 – Reunião comemorativa):
	1. Onde ocorreu o reencontro dos pesquisadores em 2006?
		R: O reencontro ocorreu novamente em Dartmouth College no ano de 2006 e foi chamado de Dartmouth Artificial Intelligence Conference: The Next Fifty Years (AI@50).
	2. Quais reflexões ou previsões foram feitas sobre o futuro da IA nesse encontro?
		R: No AI@50, os pesquisadores refletiram que a IA havia avançado muito mas não como esperado devido a sistemas especialistas que tiveram sucesso limitado, o  sonho de ser uma "inteligência geral" ainda estava distante e muitas das previsões feitas nos anos 50 eram otimistas demais. Eles também previam que no aspecto do crescimento do aprendizado de máquina já se percebia que a IA estava mais integrada ao cotidiano, se viam avanços na área da robótica, sistemas mais autônomos, questões éticas começando a ganhar importância mas ainda não havia a explosão da IA Generativa, que só viria anos depois com modelos como o GPT.
- Em sua opinião, qual é a maior diferença entre a visão dos pioneiros da IA em 1956 e a realidade tecnológica observada em 2006?
	R: As maiores diferenças entre a visão que se tinha em 1956 e a realidade observada em 2006 são que em 1956 a visão era fortemente simbólica e eles acreditavam que regras lógicas resolveria inteligência dessa forma existia um grande otimismo de que a IA avançaria muito rapidamente. Em 2006 a realidade foi que o problema era muito mais complexo e se partiu para o Aprendizado de Máquina e a ênfase em dados e estatística, não apenas em lógica formal.

### Questão 02. Qual é a definição dos conceitos de Inteligência Artificial, Aprendizado de Máquina, Aprendizado Profundo e IA Generativa? Qual é a principal diferença entre eles?
Inteligência Artificial é o campo amplo que busca desenvolver sistemas capazes de simular comportamentos inteligentes. O Aprendizado de Máquina é uma subárea da IA que permite que sistemas aprendam a partir de dados. O Aprendizado Profundo é um tipo de Aprendizado de Máquina baseado em redes neurais profundas. Já a IA Generativa é uma aplicação moderna, geralmente baseada em Aprendizado Profundo, capaz de gerar novos conteúdos como textos e imagens. A principal diferença entre eles está na abrangência e no foco de cada abordagem.

### Questão 03. Para cada problema listado a seguir, responda:
- a) Qual a tarefa de aprendizado? (classificação, regressão, agrupamento ou associação)
- b) Quais são os possíveis atributos de entrada?
- c) Existe atributo de saída (alvo/target)? Se sim, qual?
1. Uma imobiliária deseja prever o preço de venda de casas com base em suas características.
	R: 
	 a) A tarefa de aprendizado para esse problema seria a Regressão, pois ela é usada para atribuir valores numéricos dado um exemplo não rotulado depois de um treinamento com exemplos rotulados.
	 b) Possíveis atributos de entrada seriam: tamanho da casa, número de quartos, número de banheiros, localização e etc.
	 c) Sim existe atributo de saída que é o preço de venda da casa.
2. Um hospital possui dados clínicos e exames laboratoriais de pacientes e deseja prever se um tumor é benigno ou maligno.
	R: 
	a) A tarefa de aprendizado para esse problema seria a Classificação, pois ela é usada para atribuir rótulos dado um exemplo não rotulado depois de um treinamento com exemplos rotulados.
	b) Possíveis atributos de entrada seriam: tamanho do tumor, idade do paciente, resultado de biópsia, histórico médico e etc.
	c) Sim existe atributo de saída que é benigno ou maligno.
3. Uma instituição financeira quer segmentar clientes com base em seu perfil.
	 R: 
	 a) A tarefa de aprendizado para esse problema seria o Agrupamento ou *clustering*, pois ele é usado para agrupar as instâncias de acordo com a similaridade entre elas a partir de *features*. 
	 b) Possíveis atributos de entrada seriam: renda, idade, histórico de credito, dividas e etc.
	 c) Não existe rotulo de saída para casos de *clustering* já que o objetivo aqui é agrupar as instâncias.
4. Uma concessionária quer estimar o consumo mensal de energia de uma residência com base em características do imóvel e histórico de uso
	R: 
	a) A tarefa de aprendizado para esse problema seria a Regressão.
	b) Possíveis atributos de entrada seriam: quantidade de moradores, horários de maior uso, presença de ar-condicionado ou aquecedor elétrico, consumo médio dos últimos meses etc.
	c) Sim existe atributo de saída que é o consumo mensal de energia.
5. Um sistema de e-mail deseja identificar se uma mensagem é spam ou não.
	 R: 
	 a) A tarefa de aprendizado para esse problema seria a Classificação
	 b) Possíveis atributos de entrada seriam: número de caracteres, presença de anexos, presença de links, histórico de envios, horário de envio e etc.
	 c) Sim existe atributo de saída que é spam ou não spam.
6. Um supermercado deseja descobrir quais produtos costumam ser comprados em conjunto nas mesmas transações.
	R: 
	a) A tarefa de aprendizado para esse problema seria a Associação, pois ela é usada para a busca de padrões frequentes de associação dentro de um mesmo conjunto de dados.
	b) lista de produtos comprados, quantidade de cada produto, horário da compra, tipo do produto, marca, preço, em promoção ou não e etc.
	c) Não existe atributo de saída, já que o objetivo é encontrar padrões dentro do conjunto de dados.