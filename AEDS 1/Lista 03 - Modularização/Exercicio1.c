/*
    Faça um procedimento que recebe as 3 notas de um aluno por parâmetro e uma letra. Se a letra
    for ‘A’, o procedimento calcula e escreve a média aritmética das notas do aluno, se for ‘P’, calcula
    e escreve a sua média ponderada (pesos: 5, 3 e 2). Faça um programa que leia 3 notas de N
    alunos e acione o procedimento para cada aluno. (N deve ser lido do teclado)
    Autor - João Comini
    Data - 09/09/24
*/
#include <stdio.h>
#include <stdlib.h>
void Calcula(float, float, float, char); // Declaração da função Calcula indicando que a mesma recebe 4 parametros.

int main()
{
    // Declaração de variáveis
    int N;
    float nota1, nota2, nota3;
    char opc;
    printf("Valor de N:");
    scanf("%i", &N);
    for(int i = 0; i < N; i++)
    {
        printf("Nota 1, 2, 3:\n");
        scanf("%f %f %f", &nota1, &nota2, &nota3);
        printf("A/P\n");
        scanf(" %c", &opc);
        Calcula(nota1, nota2, nota3, opc);
    }


    return 0;
}

void Calcula(float nota1, float nota2, float nota3, char opc)
{
    if(opc == 'A')
        printf("%.2f\n", (nota1 + nota2 + nota3) / 3);
    else if(opc == 'P')
        printf("%.2f\n", (nota1 * 5 + nota2 * 3 + nota3 * 2) / 3);
}
