/*
    Programa em C para imprimir todas as permutações de um vetor de caracteres usando ponteiros.
    Autor - João Comini
    Data - 12/11/24
*/

#include <stdio.h>

// Função para trocar dois elementos usando ponteiros
void trocar(char* a, char* b) {
    char temp = *a;
    *a = *b;
    *b = temp;
}

// Função recursiva para gerar as permutações
void permutar(char* vetor, int inicio, int fim) {
    if (inicio == fim) {
        // Imprime a permutação atual
        for (int i = 0; i <= fim; i++) {
            printf("%c", *(vetor + i));
        }
        printf("\n");
    } else {
        for (int i = inicio; i <= fim; i++) {
            trocar((vetor + inicio), (vetor + i));    // Troca
            permutar(vetor, inicio + 1, fim);        // Recursão
            trocar((vetor + inicio), (vetor + i));    // Desfazer a troca (backtracking)
        }
    }
}

int main() {
    int n;

    // Entrada do tamanho do vetor
    printf("Digite o tamanho do vetor de caracteres: ");
    scanf("%d", &n);

    // Alocar espaço para o vetor de caracteres
    char* vetor = (char*)malloc(n * sizeof(char));
    if (vetor == NULL) {
        printf("Erro ao alocar memória.\n");
        return 1;
    }

    // Entrada dos caracteres
    printf("Digite os caracteres (sem espaços):\n");
    for (int i = 0; i < n; i++) {
        scanf(" %c", (vetor + i));
    }

    // Exibição das permutações
    printf("Permutacoes:\n");
    permutar(vetor, 0, n - 1);

    // Liberação da memória alocada
    free(vetor);

    return 0;
}
