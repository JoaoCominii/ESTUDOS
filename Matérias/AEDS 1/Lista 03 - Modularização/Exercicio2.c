/*
    A prefeitura de uma cidade fez uma pesquisa entre os seus habitantes, coletando dados sobre
    o salário e número de filhos. Faça um procedimento que leia esses dados para um número não
    determinado de pessoas, calcule e exiba a média de salário da população (a condição de parada
    deve ser um flag com salário negativo). Faça um programa que acione o procedimento.
    Autor - João Comini
    Data - 09/09/24
*/
#include <stdio.h>
#include <stdlib.h>
void mediaSalario();

int main()
{
    // Chamar a função mediaSalario
    mediaSalario();



    return 0;
}

void mediaSalario()
{
    float salario, media = 0;
    int filhos, habitantes = 0;
    printf("Salario/Filhos\n");
    scanf("%f %i", &salario, &filhos);
    while(salario >= 0)
    {
        printf("Salario/Filhos\n");
        scanf("%f %i", &salario, &filhos);
        media += salario;
        habitantes++;
    }
    media = media / habitantes;
    printf("%.2f\n", media);
}
