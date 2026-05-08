#include <stdio.h>
#include <stdlib.h>

#define TAM_NOME 50

typedef struct{

    char nome[TAM_NOME];
    int pesoMaximo;

} Atleta;

// Funcao para construir atleta
Atleta criarAtleta(const char* nome, int pesoMaximo)
{
    Atleta atleta;
    strncpy(atleta.nome, nome, TAM_NOME -1);
    atleta.nome[TAM_NOME -1] = '\0';
    atleta.pesoMaximo = pesoMaximo;
    return atleta;
}

int main()
{
    int pesoMaximo;
    Atleta atletas[100];
    int N = 0;
    char nome[TAM_NOME];
    while(scanf("%s %i", atletas[N].nome, &atletas[N].pesoMaximo) != EOF)
    {
     N++;
    }

    // Ordenar atletas (insercao)
    for(int i = 0; i < N; i++)
    {
        Atleta chave = atletas[i];
        int j = i -1;
        while(j >= 0 && atletas[j].pesoMaximo < chave.pesoMaximo || atletas[j].pesoMaximo == chave.pesoMaximo && strcmp(atletas[j].nome, chave.nome) > 0)
        {
            atletas[j + 1] = atletas[j];
            j--;
        }
        atletas[j + 1] = chave;
    }

    // printar atletas
    for(int i = 0; i < N; i++)
    {
        printf("%s %i\n", atletas[i].nome, atletas[i].pesoMaximo);
    }



    return 0;
}
