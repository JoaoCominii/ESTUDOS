/*
    Programa leia uma seqüência de valores inteiros fornecida pelo usuário em uma
    linha de entrada e conte o número de valores positivos, negativos e zeros.
    Autor - João Comini
    Data - 28/08/24
*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
    // Declaração de variáveis
    int N, valor, contaPositivos = 0, contaZeros = 0, contaNegativos = 0;

    // Leitura e testes
    printf("Quantos numeros?");
    scanf("%i", &N);
    for(int i = 0; i < N; i++)
    {
        printf("Digite o %i.o valor", i+1);
        scanf("%i", &valor);
        if(valor > 0)
            contaPositivos++;
        else if(valor == 0)
        {
            contaZeros++;
        }
        else contaNegativos++;
    }

    printf("%i %i %i", contaPositivos, contaNegativos, contaZeros);s

    return 0;
}
