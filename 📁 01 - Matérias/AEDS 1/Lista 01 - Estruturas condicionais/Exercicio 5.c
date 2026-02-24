/*
  Uma empresa decide dar um aumento de 30% aos funcionários com salários inferiores a R$ 500,00.
  Faça um programa que receba o salário do funcionário e mostre o valor do salário reajustado (ou
  a mensagem: "Sem reajuste", caso ele não tenha direito ao aumento).
  Autor - João Comini
  Data - 23/08/24
*/
#include <stdio.h>

int main() {
  // Declaração das variáveis
  float salario;
  
  // Leitura das variáveis
  printf("Digite o salário do funcionário: ");
  scanf("%f", &salario);

  // Verificar se salario é menor que 500
  if (salario <= 500)
  {
    salario = salario * 1.3;
    printf("%.2f", salario);
  }
  else printf("Sem reajuste");

  return 0;
}