/*
  Programa que receba o tipo de investimento e seu valor, calcule e mostre o valor corrigido após
  um mês de investimento, de acordo com o tipo de investimento.
  Autor - João Comini
  Data - 24/08/24
*/
#include <stdio.h>

int main() {
  // Declaração de variáveis
  float valor;
  char tipo_investimento;

  // Leitura das variáveis
  printf("Digite o valor do investimento: ");
  scanf("%f", &valor);
  printf("Digite o tipo de investimento (P para poupança ou F para fundos de renda)");
  scanf(" %c", &tipo_investimento);
  
  // Calculo do rendimento
  if(tipo_investimento == 'P' || tipo_investimento == 'p') 
    valor = valor * 1.03;
  else if(tipo_investimento == 'F' || tipo_investimento == 'f')
    valor = valor * 1.04;
  else printf("Tipo de investimento invalido");
  
  // Exibição do resultado
  printf("%.2f", valor);

  
  return 0;
}