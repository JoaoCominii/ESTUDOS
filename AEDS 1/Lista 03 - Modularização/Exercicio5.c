/*
    Faça um procedimento que recebe a média final de um aluno, identifica e exibe o seu conceito,
    conforme a tabela abaixo. Faça um programa que leia a média de N alunos, acionando o procedimento para cada um deles. (N deve ser lido do teclado)
    Autor - João Comini
    Data - 10/09/24
*/
#include <stdio.h>
#include <stdlib.h>
void exibeConceito(int);

int main()
{
    int N, media;
    printf("N:");
    scanf("%i", &N);
    for(int i = 0; i < N; i++)
    {
        printf("Media:\n");
        scanf("%i", &media);
        exibeConceito(media);
    }


    return 0;
}

void exibeConceito(int media)
{
    if(media <= 39)
        printf("F\n");
    else if(media > 39 && media < 60)
        printf("E\n");
    else if(media > 59 && media < 70)
        printf("D\n");
    else if(media > 69 && media < 80)
        printf("C\n");
    else if(media > 79 && media < 90)
        printf("B\n");
    else if(media >= 90)
        printf("A\n");
}
