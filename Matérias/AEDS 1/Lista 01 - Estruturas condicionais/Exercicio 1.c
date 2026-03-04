/*
  Programa que recebe três números e mostre o maior
  Autor - João Comini
  Data - 23/08/24
*/
#include <stdio.h>

int main() {
  // Declação de variáveis
  int A, B, C;
  
  // Ler três números
  printf("Digite três números: ");
  scanf("%d %d %d", &A, &B, &C);
  
  // Criar condição para mostrar o maior número
  if(A > B && A > C) {
    printf("O maior número é %d\n", A);
  }
  else if (B > A && B > C){
    printf("O maior número é %d\n", B);
  }
  else printf("O maior número é %d\n", C);
  
  return 0;
}