#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#define MAX_TEXTO 100001


int main()
{
    char linha[MAX_TEXTO];
    while(scanf("%s", linha) != EOF)
    {
        char home_buffer[MAX_TEXTO]; // para armazenar o que foi digitado antes da ultima ]
        char normal_buffer[MAX_TEXTO]; // para armazenar o que foi digitado depois de ]
        int home_index = 0, normal_index = 0;
        bool escrevendo_no_home = false;

        for(int i = 0; linha[i] != '\0'; i++)
        {
            if(linha[i] == '[')
                escrevendo_no_home = true; // Comeca a escrever no home buffer
            else if(linha[i] == ']')
                escrevendo_no_home = false; // Volta a escrever no normal buffer
            else{
                if(escrevendo_no_home)
                    home_buffer[home_index++] = linha[i];
                else
                    normal_buffer[normal_index++] = linha[i];
            }

        }

        home_buffer[home_index] = '\0';
        normal_buffer[normal_index] = '\0';

        // Imprimir ambas strings
        printf("%s %s", home_buffer, normal_buffer);
    }


    return 0;
}
