/*
  Algoritmo que leia o ano de nascimento de uma pessoa e calcule sua idade, considerando o
  ano atual. Para verificar se já fez aniversário no ano atual pergunte se a pessoa já fez       
  aniversário,sendo que ela pode entrar com a informação "S"(sim) ou "N"(não). 
  Com isto é possível se ter maior precisão sobre a idade. Verifique também se a pessoa já tem       idade para conseguir Carteira de Habilitação (18 anos ou mais) e imprima a mensagem referente a     esta checagem ("Pode dirigir"ou "Não pode dirigir"). Imprima a idade da pessoa.
  Autor - João Comini
  Data - 23/08/24
*/
#include <stdio.h>

int main() {
  // Declaração de variáveis
  int idade, ano_nascimento, ano_atual;
  char niver;

  // Leitura de variáveis
  printf("Digite o ano de nascimento: ");
  scanf("%d", &ano_nascimento);
  printf("Ja fez aniversario esse ano? (S/N): ");
  scanf(" %c", &niver);

  // Descobrir se ja fez aniversario este ano
  if (niver == 'S' || niver == 's')
    ano_atual = 2024;
  else ano_atual = 2023;

  // Cálculo idade
  idade = ano_atual - ano_nascimento;
  printf("%i\n", idade);
  
  // Veriicação de idade para dirigir
  if(idade >= 18)
    printf("Pode dirigir");
  else printf("Não pode dirigir");
  
  return 0;
}