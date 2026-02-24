/*
    Crie um programa que leia a quantidade de veículos que uma locadora de veículos possui e o
    valor que ela cobra por cada aluguel, mostrando as informações pedidas a seguir:
    a) Sabendo-se que um terço dos veículos são alugados por mês, exiba o faturamento anual da
    locadora;
    b) Quando o cliente atrasa a entrega, é cobrada uma multa de 20% sobre o valor do aluguel.
    Sabendo que um décimo dos veículos alugados no mês é devolvido com atraso, calcule o
    valor ganho com multas no mês;
    c) Sabe-se ainda que 2% dos veículos precisam de manutenção ao longo do ano. Calcule o valor
    gasto com a manutenção anual, sabendo que o valor gasto em média com a manutenção é
    de R$ 600,00.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>
void calcularValorAlugel(int veiculos, float aluguel)
{
    // a
    float faturamentoMensal = veiculos / 3 * aluguel;
    float faturamentoAnual = faturamentoMensal * 12;
    // b
    float atraso = veiculos / 3 / 10 * (aluguel * 0.2);
    // c
    float manutencao = (veiculos * 0.02) * 600;

    // Exibindo os resultados na tela
    printf("Faturamento anual: R$ %.2f\n", faturamentoAnual);
    printf("Ganho com multas no mês: R$ %.2f\n", atraso);
    printf("Gasto anual com manutenção: R$ %.2f\n", manutencao);

    // Gravando no arquivo
    FILE* arq = fopen("resultados.txt", "w");
    fprintf(arq,"Faturamento anual: R$ %.2f\n", faturamentoAnual);
    fprintf(arq,"Ganho com multas no mês: R$ %.2f\n", atraso);
    fprintf(arq,"Gasto anual com manutenção: R$ %.2f\n", manutencao);
    fclose(arq);

}

int main()
{
    int quantidadeVeiculos;
    float valorAluguel;

    printf("Quantidade de veiculos:");
    scanf("%i", &quantidadeVeiculos);
    printf("Valor do aluguel:");
    scanf("%f", &valorAluguel);
    calcularValorAlugel(quantidadeVeiculos, valorAluguel);


    return 0;
}
