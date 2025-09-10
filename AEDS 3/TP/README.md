
## Introdução
Este é um trabalho prático da disciplina de Algoritmos e Estruturas de Dados III (AEDS III) do curso de Ciência da Computação da Pontifícia Universidade Católica de Minas Gerais.
- Trabalho feito por: João Comini César de Andrade

## Tema Do Sistema
### Sistema de Gerenciamento de Aluguel de Jogos
O sistema permitirá o cadastro e gerenciamento de clientes, itens (jogos) e aluguéis, com funcionalidades para registrar transações, controlar disponibilidade e gerar estatísticas.

## Descrição do Problema

Atualmente, muitas locadoras ou colecionadores utilizam planilhas ou anotações manuais para gerenciar o aluguel de jogos. Além disso, muitos jogadores não têm acesso aos jogos mais caros devido ao alto custo de aquisição, o que limita a experiência e o acesso à diversão e cultura digital. Pensando nisso, decidimos criar um sistema de aluguel de jogos para democratizar o acesso a títulos variados e de alto valor.

Esses métodos manuais geram problemas como:
- Dificuldade para encontrar rapidamente informações sobre clientes e itens alugados.
- Falta de padronização no registro de aluguéis e devoluções.
- Risco de perda de dados e inconsistências.
- Ausência de um histórico centralizado para estatísticas e controle de estoque.

O sistema proposto visa resolver esses problemas oferecendo uma plataforma digital para:
  1. Cadastrar clientes, itens (jogos) e aluguéis.
  2. Registrar aluguéis e devoluções, controlando disponibilidade dos itens.
  3. Pesquisar clientes por nome e jogos por título (casamento de padrões).
  4. Garantir segurança das informações com criptografia de senhas.
  5. Otimizar buscas com índices B+ e hash extensível.
  6. Armazenar dados de forma compactada para economia de espaço.

## Objetivo do Trabalho
Desenvolver um sistema que:
- Permita CRUD de Clientes, Itens (Jogos) e Aluguéis.
- Implemente pelo menos um relacionamento 1:N (Cliente → Aluguéis; um cliente pode ter vários aluguéis).
- Implemente pelo menos um relacionamento N:N (Itens podem estar em vários aluguéis e um aluguel pode conter vários itens, modelado por tabela intermediária).
- Utilize persistência em arquivos binários com cabeçalho e exclusão lógica (lápide).
- Ofereça busca rápida e ordenada por índices B+ e hash extensível.
- Implemente criptografia XOR para senhas de usuários.
- Disponibilize compactação/descompactação dos arquivos de dados.
- Permita pesquisa por nome de cliente e por título de jogo (BM ou KMP).

## Requisitos Funcionais (RF)
- **RF01:** Cadastrar cliente (nome, data de nascimento, e-mail, lista de telefones (multivalorado), endereço).
- **RF02:** Cadastrar item (jogo) com: título, gênero, plataforma, ano de lançamento, preço de aluguel (real), lista de tags (multivalorado).
- **RF03:** Registrar aluguel com: cliente, data de início, data de devolução prevista, valor total (real), itens alugados (N:N).
- **RF04:** Registrar devolução e atualizar disponibilidade dos itens.
- **RF05:** Listar registros ativos e permitir exclusão lógica.
- **RF06:** Pesquisar cliente por nome e jogo por título usando BM ou KMP.

## Requisitos Não Funcionais (RNF)
- **RNF01:** Interface gráfica (HTML/CSS)
- **RNF02:** Persistência em arquivos binários com cabeçalho (com informações de controle, como número de registros e último ID).
- **RNF03:** Uso de índices B+ e hash extensível para buscas.
- **RNF04:** Criptografia XOR para senhas.
- **RNF05:** Compactação Huffman ou LZW para arquivos de dados.

## Atores
- **Administrador:** Gerencia cadastros, aluguéis e itens.
- **Usuário/Cliente:** Consulta informações sobre jogos disponíveis, seus próprios aluguéis e histórico.

## Diagrama de Caso de Uso
![Diagrama de Caso de Uso]()

## Diagrama Entidade-Relacionamento
![DER]()

## Arquitetura Proposta

O sistema seguirá o padrão **MVC + DAO**, onde:
- **Model:** Classes de domínio (Cliente, Item/Jogo, Aluguel, Tabela intermediária de Itens do Aluguel).
- **DAO (Data Access Object):** Responsável pela persistência dos dados em arquivos binários e seus acessos, utilizando cabeçalhos e exclusão lógica (lápide).
- **Controller:** Regras de negócio e controle de fluxo da aplicação.
- **View:** Interface gráfica para interação com o usuário (HTML/CSS).

## Diagrama de Arquitetura em Camadas
![Diagrama de Arquitetura em Camadas]()