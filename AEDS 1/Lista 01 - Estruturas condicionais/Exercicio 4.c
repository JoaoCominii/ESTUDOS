/*
  Programa que receba a idade de um nadador e mostre sua categoria
  Autor - João Comini
  Data - 23/08/24
*/
#include <stdio.h>

int main() {
  // Declração de variáveis
  int idade;
  
  // Leitura da idade
  printf("Digite a idade do nadador: ");
  scanf("%d", &idade);
  
  // Cauculo da categoria
  if (idade >= 5 && idade <= 7)
    printf("Infantil");
  else if (idade >= 8 && idade <= 10)
    printf("Juvenil");
  else if (idade >= 11 && idade <= 15)
    printf("Adolescente");
  else if (idade >= 16 && idade <= 30)
    printf("Adulto");
  else printf("Senior");


  
  return 0;
}