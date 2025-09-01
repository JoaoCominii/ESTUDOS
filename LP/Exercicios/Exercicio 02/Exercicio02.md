# Exercício 02 - Linguagens de Programação

## 1. Considerando os princípios de projetos de linguagem, descreva as seguintes categorias, utilizando de exemplo(s) como justificativa: (i) sintaxe; (ii) nomes e tipos; (iii) semântica.

### R:

(i) **Sintaxe:**  
A sintaxe de uma linguagem de programação define as regras formais para a escrita dos comandos, ou seja, como os símbolos, palavras e estruturas devem ser organizados para formar programas válidos. Ela determina, por exemplo, como declarar variáveis, escrever funções ou expressões aritméticas, garantindo que o compilador ou interpretador consiga entender o código e executar as instruções corretamente.  
*Exemplo:* Em Python, a indentação é obrigatória para definir blocos de código, enquanto em C, blocos são delimitados por chaves `{}`. Assim, o código abaixo é válido em Python:
```python
if x > 0:
    print("Positivo")
```
Já em C, seria:
```c
if (x > 0) {
    printf("Positivo");
}
```
A diferença na sintaxe influencia a legibilidade e a facilidade de aprendizado da linguagem.

(ii) **Nomes e Tipos:**  
Nomes referem-se à forma como identificadores (como variáveis, funções, classes) são definidos e utilizados em uma linguagem (É um identificador que você dá a uma variável, classe, função ou outros). Tipos indicam a natureza da informação que uma variável pode conter (inteiros, reais, caracteres, etc.), isso influencia a forma como os dados são armazenados e manipulados, bem como a linguagem trata a associação entre nomes e tipos.  
*Exemplo:* Em Java, é necessário declarar o tipo da variável explicitamente:
```java
int idade = 20;
```
Já em Python, a tipagem é dinâmica e não é preciso declarar o tipo:
```python
idade = 20
```
A escolha entre tipagem estática ou dinâmica, forte ou fraca, influencia a detecção de erros e a flexibilidade do código.

(iii) **Semântica:**  
A semântica trata do significado dos comandos e estruturas da linguagem, ou seja, o que realmente acontece quando um programa é executado. Enquanto a sintaxe define "como escrever", a semântica define "o que significa".  
*Exemplo:* O comando `=` em Python representa atribuição (ex: `x = 5` atribui o valor 5 à variável x), enquanto em matemática o símbolo `=` representa igualdade. Já em C, o operador `==` é usado para comparação de igualdade, e `=` para atribuição.  
Outro exemplo: a expressão `x = x + 1` em linguagens imperativas significa incrementar o valor de x em 1, enquanto em linguagens funcionais, como Haskell, isso não faz sentido, pois variáveis são imutáveis.

Essas três categorias — sintaxe, nomes e tipos, e semântica — são fundamentais no projeto de linguagens, pois afetam diretamente a clareza, segurança e expressividade

## 2. Defina brevemente o que é um paradigma de programação.

### R:

Um paradigma de programação é um modelo ou estilo de desenvolvimento que define como os problemas e soluções são estruturados e expressos em um programa. Ele estabelece princípios, conceitos e técnicas para organizar o código, como, por exemplo, o paradigma imperativo (baseado em comandos sequenciais), o orientado a objetos (baseado em objetos e classes), o funcional (baseado em funções e imutabilidade) e o lógico (baseado em regras e lógica). Cada paradigma influencia a forma de pensar, projetar e implementar soluções em uma linguagem de programação, afetando aspectos como a estrutura do código, o controle de fluxo, a gestão de estado e a reutilização de componentes.
```
É como um modelo para se escrever um código.
```
Diferentes paradigmas podem ser mais adequados para diferentes tipos de problemas, e muitos linguagens de programação suportam múltiplos paradigmas, permitindo que os desenvolvedores escolham o mais apropriado para a tarefa em mãos.

## 3. Diferencie os quatro paradigmas em suas características, utilizando de exemplo(s) como justificativa: (i) imperativo; (ii) orientado à objetos; (iii) funcional; (iv) lógico.

### R:

(i) **Imperativo:**  
O paradigma imperativo é baseado em comandos sequenciais que modificam o estado do programa por meio de instruções explícitas. O foco está em "como" realizar as tarefas, detalhando passo a passo as operações necessárias. Linguagens como C, Pascal e Assembly são exemplos clássicos desse paradigma.  
*Exemplo:* Em C, para somar os números de 1 a 5:
```c
int soma = 0;
for (int i = 1; i <= 5; i++) {
    soma += i;
}
```
Aqui, o programador controla explicitamente o fluxo e o estado das variáveis.

(ii) **Orientado a Objetos:**  
O paradigma orientado a objetos organiza o código em torno de "objetos", que são instâncias de "classes" contendo atributos (dados) e métodos (funções). O foco está em modelar entidades do mundo real, promovendo encapsulamento, herança e polimorfismo. Linguagens como Java, Python e C++ suportam esse paradigma.  
*Exemplo:* Em Java, um exemplo de classe `Carro`:
```java
class Carro {
    String modelo;
    void buzinar() {
        System.out.println("Bip Bip!");
    }
}
Carro meuCarro = new Carro();
meuCarro.buzinar();
```
Aqui, o comportamento e os dados do carro estão encapsulados no objeto.

(iii) **Funcional:**  
O paradigma funcional enfatiza o uso de funções puras, imutabilidade e ausência de efeitos colaterais. O foco está em "o que" deve ser feito, e não em "como" fazer, utilizando funções como cidadãos de primeira classe e evitando modificar o estado global. Linguagens como Haskell, Lisp e, parcialmente, Python e JavaScript suportam esse paradigma.  
*Exemplo:* Em Haskell, para somar os números de 1 a 5:
```haskell
sum [1..5]
```
Em Python, usando `map` e `lambda`:
```python
lista = [1, 2, 3, 4, 5]
quadrados = list(map(lambda x: x**2, lista))
```
Aqui, a ênfase está em aplicar funções sobre coleções de dados, sem alterar o estado original.

(iv) **Lógico:**  
O paradigma lógico é baseado em regras de lógica e relações, onde o programador define fatos e regras, e o sistema de inferência busca soluções para consultas. O foco está em "o que é verdade" sobre o problema, e não em como resolver passo a passo. Prolog é a linguagem mais conhecida desse paradigma.  
*Exemplo:* Em Prolog, para definir relações familiares:
```prolog
pai(joao, maria).
pai(joao, jose).
irmao(X, Y) :- pai(P, X), pai(P, Y), X \= Y.
```
Uma consulta como `irmao(maria, jose).` retorna verdadeiro se maria e jose forem irmãos, com base nas regras e fatos definidos.

**Resumo:**  
- O paradigma imperativo detalha o passo a passo das operações.
- O orientado a objetos modela entidades como objetos com dados e comportamentos.
- O funcional enfatiza funções puras e imutabilidade.
- O lógico descreve relações e regras, deixando a resolução para o sistema.

Cada paradigma oferece vantagens para diferentes tipos de problemas e estilos de programação, e a escolha do paradigma adequado pode facilitar a solução do problema e tornar o código mais claro e eficiente.

## 4. Explique a razão de C++ ser classificado como uma linguagem multi paradigma. Quais paradigmas estão em questão? Utilize de trechos de código como justificativa.

### R:

C++ é classificado como uma linguagem multi paradigma porque suporta diferentes estilos de programação, permitindo que o programador escolha o paradigma mais adequado para cada situação. Os principais paradigmas suportados por C++ são: imperativo/procedural, orientado a objetos e, em menor escala, o paradigma funcional.

**Paradigma Imperativo/Procedural:**  
C++ permite a programação tradicional baseada em funções e comandos sequenciais, semelhante ao C.
*Exemplo:*
```cpp
#include <iostream>
void saudacao() {
    std::cout << "Olá, mundo!" << std::endl;
}
int main() {
    saudacao();
    return 0;
}
```

**Paradigma Orientado a Objetos:**  
C++ oferece suporte completo a classes, objetos, herança, polimorfismo e encapsulamento.
*Exemplo:*
```cpp
#include <iostream>
class Animal {
public:
    void falar() {
        std::cout << "Som de animal" << std::endl;
    }
};
int main() {
    Animal a;
    a.falar();
    return 0;
}
```

**Paradigma Funcional:**  
C++ também permite o uso de funções como objetos de primeira classe (functors, lambdas) e programação baseada em funções.
*Exemplo:*
```cpp
#include <iostream>
#include <algorithm>
#include <vector>
int main() {
    std::vector<int> v = {1, 2, 3, 4, 5};
    std::for_each(v.begin(), v.end(), [](int x) { std::cout << x * x << " "; });
    return 0;
}
```

**Resumo:**  
A flexibilidade de C++ em suportar múltiplos paradigmas permite que o desenvolvedor combine estilos de programação, aproveitando o melhor de cada abordagem para resolver diferentes tipos de problemas de forma mais eficiente e clara.

## 5. Explique a razão de certos comandos, como o de atribuição, não "fazerem sentido" no paradigma funcional

### R:

No paradigma funcional, certos comandos como o de atribuição (`=`) não fazem sentido porque esse paradigma é baseado em imutabilidade e funções puras. Em linguagens funcionais, uma vez que um valor é atribuído a uma variável, ele não pode ser alterado. Isso contrasta com paradigmas imperativos, onde variáveis podem ter seus valores modificados ao longo do tempo.

Atribuição implica mudança de estado, mas no paradigma funcional, não há estado mutável: cada variável representa um valor fixo durante sua existência. Em vez de modificar valores, criam-se novos valores a partir dos existentes.

*Exemplo:*  
Em uma linguagem **imperativa**, como Python:
```python
x = 5
x = x + 1  # x agora vale 6
```
Aqui, o valor de `x` é alterado.

Em Haskell **(funcional)**, isso não é permitido:
```haskell
x = 5
x = x + 1  -- Erro! Não é possível reatribuir x
```
No paradigma **funcional**, para obter um novo valor, você define uma nova variável:
```haskell
x = 5
y = x + 1  -- y vale 6, x continua valendo 5
```

Portanto, comandos de atribuição não fazem sentido no paradigma funcional porque violam o princípio da imutabilidade, que é fundamental para esse estilo.

