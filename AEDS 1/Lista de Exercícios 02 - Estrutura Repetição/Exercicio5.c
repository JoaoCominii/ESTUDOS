/*
    A prefeitura de uma cidade fez uma pesquisa entre seus habitantes, coletando dados sobre o
    salário e número de filhos de cada habitante. A prefeitura deseja saber:
    a) média do salário da população;
    b) média do número de filhos;
    c) maior salário;
    d) percentual de pessoas com salário até R$100,00.
    O final da leitura de dados se dará com a entrada de um salário negativo.
    Autor - João Comini
    Data - 03/09/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variaveis
    float salario, salario_media = 0, maior_salario = 0, filhos_media = 0, salarioMenor100 = 0;
    int filhos, N;

    // Leitura quantidade de habitantes
    printf("Habitantes:");
    scanf("%i", &N);

    // Leitura dados
    for(int i = 0; i < N; i++)
    {
        printf("Salario:");
        scanf("%f", &salario);
        printf("Filhos:");
        scanf("%i", &filhos);
        salario_media += salario;
        filhos_media += filhos;
        if(salario > maior_salario) // Descobrir maior salario
        {
            maior_salario = salario;
        }
        if(salario <= 100)
            salarioMenor100++; // Contador percentual pessoas salario <= 100
    }

    // Calcular media salario pop
    salario_media = salario_media / N;

    // Calcular media de filhos
    filhos_media = filhos_media / N;

    // Calcular percentual pessoas salario <= 100
    salarioMenor100 = (salarioMenor100 * 100) / N;

    // Escrever resultados na tela
    printf("%.2f\n", salario_media);
    printf("%.2f\n", filhos_media);
    printf("%.2f\n", maior_salario);
    printf("%.2f\n", salarioMenor100);

    return 0;
}

