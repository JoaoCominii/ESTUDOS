# Exercício 02 - Linguagens de Programação

## 6. Explique a razão de linguagens pertencentes ao paradigma lógico também serem chamadas de declarativas ou baseadas em regras.

### R:

Linguagens do paradigma lógico são chamadas de **declarativas** ou **baseadas em regras** porque o programador descreve *o que* deve ser resolvido, por meio de fatos e regras, sem detalhar *como* resolver. O sistema de inferência da linguagem utiliza essas declarações para encontrar soluções automaticamente, como ocorre em Prolog.

## 7. Descreva os seguintes tópicos especiais para projetos de linguagens, utilizando de exemplo(s) como justificativa: (i) manipulação de eventos; (ii) concorrência; (iii) correção.

### R: 

(*I*) A **manipulação de eventos** ocorre com programas que respondem a eventos inesperados. Ela está frequentemente relacionada
ao paradigma orientado a objetos como a linguagem Java, embora possa também ser encontrada em linguagens de paradigma imperativo.
Os eventos aos quais o programa responde podem ser tanto gerados pelo usuário como com cliques em botões, teclas pressionadas etc., tanto por outras fontes como leituras de sensores em um robô.

(*II*) A **concorrência** refere-se à capacidade de um programa executar múltiplas tarefas de forma sobreposta, seja por meio de múltiplos processos ou threads. Ela está presente em diversos paradigmas, como imperativo, orientado a objetos, lógico e funcional. A concorrência permite que diferentes partes de um programa compartilhem informações ou se sincronizem, melhorando o desempenho e a responsividade.

Por exemplo, em Java (orientado a objetos), é possível criar múltiplas threads para processar requisições simultâneas em um servidor web. Em Python (imperativo), pode-se usar o módulo `threading` para realizar downloads de arquivos em paralelo. Já em linguagens funcionais como Erlang, a concorrência é utilizada para construir sistemas distribuídos robustos, como servidores de telecomunicações. Em C, é possível implementar concorrência utilizando a biblioteca POSIX Threads (`pthread`), permitindo que diferentes threads executem cálculos simultaneamente, como no processamento paralelo de matrizes.

Esses exemplos mostram como a concorrência é fundamental para aplicações modernas que exigem execução paralela, como jogos, sistemas embarcados e servidores de alta performance.

(*III*) A **Correção**, ou sejá um programa é dito correto quando, satisfaz sua especificação formal para todas as suas entradas possíveis. Alguns exemplos são o sistema Spark/Ada e a Java Modeling Language.

