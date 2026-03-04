/*
  Programa para ler os coeficientes de uma equação do primeiro grau (ax + b = 0),
  calcular e escrever a raiz da equação.
  Autor - João Comini
  Data - 24/08/24
*/
#include <stdio.h>

int main() {
  // Declaração de variáveis
  float a, b, x;
  
  // Leitura das variáveis
  printf("Digite o valor de a: ");
  scanf("%f", &a);
  printf("Digite o valor de b: ");
  scanf("%f", &b);
  
  // Calculo da raiz
  x = -b / a; // (ax + b = 0) consegue ser reescrito como x = -b / a

  // Escrita do resultado
  printf("%.2f", x);



  
  return 0;
}