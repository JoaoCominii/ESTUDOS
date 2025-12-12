/*
  Programa que receba a altura e o sexo de uma pessoa e calcule e mostre seu peso ideal
  Autor - João Comini
  Data - 24/08/24
*/
#include <stdio.h>

int main() {
  // Declaração de variáveis
  float altura, peso_ideal;
  char sexo;

  // Leitura das variáveis
  printf("Digite a altura: ");
  scanf("%f", &altura);
  printf("Digite o gênero (H/M): ");
  scanf(" %c", &sexo); 
  
  // Calculo do peso ideal
  if(sexo == 'H' || sexo == 'h')
    peso_ideal = (72.7 * altura) - 58; // Baseado na formula: (72.7 * h) – 58 para homens.
  else if(sexo == 'M' || sexo == 'M')
    peso_ideal = (62.1 * altura) - 44.7; // Baseado na formula: (62.1 * h) – 44.7 para mulheres.
  else printf("Genero invalido"); // Caso o genero seja diferente de H ou M.

  // Exibição do resultado
  printf("%.2f", peso_ideal);
  
  return 0;
}