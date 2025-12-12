/*
  Programa que recebe quatro notas de um aluno, calcule e mostre a média aritmética
  das notas e a mensagem de aprovado ou reprovado, considerando para aprovação média 7.
  Autor - João Comini
  Data - 23/08/24
*/
#include <stdio.h>

int main() {
  // Declaração de variáveis
  float nota1, nota2, nota3, nota4, media;

  // Leitura das notas
  printf("Digite as 4 notas:");
  scanf("%f %f %f %f", &nota1, &nota2, &nota3, &nota4);

  // Calculo da média
  media = (nota1 + nota2 + nota3 + nota4) / 4;

  // Mostar média e mensagem de aprovado ou reprovado
  printf("%.2f\n", media);
  if(media >= 7)
    printf("Aprovado\n");
  else
    printf("Reprovado\n");

  
  return 0;
}