/*
    Escreva uma função que recebe por parâmetro um valor inteiro e positivo N e retorna o valor de
    S, calculado segundo a fórmula abaixo.
    Autor - João Comini
    Data - 12/09/24
*/
#include <stdio.h>
#include <stdlib.h>
float calculaS(int N);

int main() {
    int N;
    printf("N: ");
    scanf("%d", &N);


    if (N > 0)
    {
        printf("%f\n", calculaS(N));
    }
    return 0;
}

float calculaS(int N) {
    float S = 0.0;
    for (int i = 1; i <= N; i++)
    {
        S += (i * i + 1.0) / (i + 3.0);
    }

    return S;
}
