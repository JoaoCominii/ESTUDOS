/*
    Em uma eleição presidencial existem quatro candidatos. Os votos são informados através
    de códigos. Os dados utilizados para a contagem dos votos obedecem à seguinte
    codificação:
    1,2,3,4 = voto para os respectivos candidatos;
    5 = voto nulo;
    6 = voto em branco;
    Elabore um algoritmo que leia o código do candidado em um voto. Calcule e escreva:
    - total de votos para cada candidato;
    - total de votos nulos;
    - total de votos em branco.

    Como finalizador do conjunto de votos, tem-se o valor 0.
    Autor - João Comini
    Data - 06/09/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variaveis
    int voto, candidato1 = 0, candidato2 = 0, candidato3 = 0, candidato4 = 0, nulo = 0, branco = 0;

    // Leitura dados
    printf("Digite seu voto: (1, 2, 3, 4, 5, 6 e 0 para finalizar)");
    scanf("%i", &voto);
    while(voto != 0)
    {
        switch(voto)
        {
        case 1:
            candidato1++;
            break;
        case 2:
            candidato2++;
            break;
        case 3:
            candidato3++;
            break;
        case 4:
            candidato4++;
            break;
        case 5:
            nulo++;
            break;
        case 6:
            branco++;
            break;
        }
    printf("Digite seu voto: (1, 2, 3, 4, 5, 6 e 0 para finalizar)");
    scanf("%i", &voto);
    }
    printf("%i\n", candidato1);
    printf("%i\n", candidato2);
    printf("%i\n", candidato3);
    printf("%i\n", candidato4);
    printf("%i\n", nulo);
    printf("%i\n", branco);
    return 0;
}

