/*
  Uma empresa decidiu dar uma gratificação de Natal a seus funcionários, baseada no número de
  horas extras e no número de horas que o funcionário faltou ao trabalho.
  Autor - João Comini
  Data - 24/08/24
*/
#include <stdio.h>

int main() {
  // Definição de variáveis
  float hora_extra, hora_falta, gratificacao, H;

  // Leitura da quantidade de horas extras e faltas
  printf("Digite a quantidade de horas extras: ");
  scanf("%f", &hora_extra);
  printf("Digite a quantidade de horas faltas: ");
  scanf("%f", &hora_falta);

  // Calcular a gratificação
  H = hora_extra - (2.0/3.0 * (hora_falta));
  H = H * 60;
    if(H >= 2400)
      gratificacao = 500;
    else if(H >= 1800 && H < 2400)
      gratificacao = 400;
    else if(H >= 1200 && H < 1800)
      gratificacao = 300;
    else if(H >= 600 && H < 1200)
      gratificacao = 200;
    else if(H < 600)
      gratificacao = 100;

  // Exibir a gratificação
  printf("%.2f", gratificacao);
  
  return 0;
}