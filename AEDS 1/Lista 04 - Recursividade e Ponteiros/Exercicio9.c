/*
    Desenvolva um programa que leia a quantidade total de segundos e converta para Horas, Minutos
    e Segundos. Para isso, utilize um PROCEDIMENTO com protótipo: void converteHora(int total
    segundos, int *hora, int *min, int *seg). Na main, imprima o resultado da conversão no formato
    HH:MM:SS
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

void converteHora(int totalsegundos, int *hora, int *min, int *seg)
{
    int temp;

    *hora = totalsegundos / 3600;

    temp = totalsegundos % 3600;

    *min = temp / 60;
    *seg = temp % 60;
}

int main()
{
    int totalSegundos, hora, min, seg;
    scanf("%i", &totalSegundos);
    converteHora(totalSegundos, &hora, &min, &seg);
    printf("%2i:%2i:%2i", hora, min, seg);


    return 0;
}
