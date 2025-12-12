/*
    Faça um procedimento que receba e preencha um vetor com as notas de uma turma de 10 alunos.
    Faça um outro procedimento que receba um vetor preenchido com as notas, calcule a média da
    turma e conte quantos alunos obtiveram nota acima da média. Esse procedimento deve exibir a
    média e o resultado da contagem. Faça um programa que declare as devidas variáveis e acione
    os procedimentos.
    Autor - João Comini
    Data - 09/11/24
*/
#include <stdio.h>
#include <stdlib.h>
#define ln 10
void preencheNotas(float notas[ln])
{
    for(int i = 0; i < ln; i++)
    {
        printf("Nota:\n");
        scanf("%f", &notas[i]);
    }

}
void media(float notas[ln])
{
    float media = 0;
    int conta = 0;
    for(int i = 0; i < ln; i++)
    {
        media += notas[i];
    }
    media /= ln;
    for(int i = 0; i < ln; i++)
    {
       if(notas[i] >= media)
        conta++;
    }
    printf("Média: %.2f\n", media);
    printf("Alunos acima da média: %i\n", conta);
}

int main()
{
    float notas[ln];

    preencheNotas(notas);
    media(notas);
    return 0;
}
