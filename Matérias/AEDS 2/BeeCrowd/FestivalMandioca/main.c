#include <stdio.h>
#include <stdlib.h>

int contarPares(int n)
{
    int pares = 0;
    while(n > 0)
    {
        int digito = n % 10;
        if(digito % 2 ==0)
            pares++;
        n/=10;
    }

    return pares;
}

int main()
{
   int N, K; // N = numero de sacas e K = posicao da taina
   scanf("%i %i", &N, &K);
   int *sacas = malloc(N * sizeof(int));
   for(int i = 0; i < N; i++)
   {
       scanf("%i", &sacas[i]);
   }
   int tam = N;
    for(int visitante = 1; visitante <= K; visitante++)
    {
        int maior = 0;
        for(int i = 1; i < tam; i++)
        {
            if(sacas[i] > sacas[maior])
                maior = i;
        }
        int quantidadeColetada = contarPares(sacas[maior]);
        if(visitante == K)
        {
            printf("%i\n", quantidadeColetada);
            break;
        }

        sacas[maior] -= quantidadeColetada;

        if(sacas[maior] == 0)
        {
            sacas[maior] = sacas[tam - 1];
            tam--;
        }
    }


    free(sacas);
    return 0;
}
