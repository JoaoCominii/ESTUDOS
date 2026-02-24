/*
    Adaptar o programa acima para que ele calcule o percentual dos valores positivos,
    negativos e zeros em relação ao total de valores fornecidos.
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

    printf("%i%% %i%% %i%%", contaPositivos * 100 / N, contaNegativos * 100 / N, contaZeros * 100 / N);

    return 0;
}

