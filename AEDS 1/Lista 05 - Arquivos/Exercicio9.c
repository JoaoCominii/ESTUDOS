/*
    Crie um programa que receba dados de vários alunos (Matricula e Telefone) e armazene em um
    arquivo texto chamado saida.txt. Deve-se ler a opção da entrada de dados (1-pelo teclado, 2-por
    arquivo). Se escolhida a leitura por arquivo, considere o nome do arquivo como entrada.txt.
    Autor - João Comini
    Data - 09/10/24
*/
#include <stdio.h>
#include <stdlib.h>

void lerDadosTeclado(FILE *arquivo) {
    int matricula;
    char telefone[20];
    char continuar;
    do {
        printf("Digite a matricula: ");
        scanf("%d", &matricula);
        printf("Digite o telefone: ");
        scanf("%s", telefone);
        fprintf(arquivo, "Matricula: %d, Telefone: %s\n", matricula, telefone);
        printf("Deseja continuar? (s/n): ");
        scanf(" %c", &continuar);
    } while (continuar == 's' || continuar == 'S');
}

void lerDadosArquivo(FILE *entrada, FILE *saida) {
    int matricula;
    char telefone[20];
    while (fscanf(entrada, "%d %s", &matricula, telefone) != EOF) {
        fprintf(saida, "Matricula: %d, Telefone: %s\n", matricula, telefone);
    }
}

int main() {
    int opcao;
    printf("Escolha a opcao de entrada de dados:\n");
    printf("1 - Pelo teclado\n");
    printf("2 - Por arquivo\n");
    printf("Opcao: ");
    scanf("%d", &opcao);

    FILE *saida = fopen("saida.txt", "w");
    if (saida == NULL) {
        perror("Erro ao abrir saida.txt");
        return 1;
    }

    if (opcao == 1) {
        lerDadosTeclado(saida);
    } else if (opcao == 2) {
        FILE *entrada = fopen("entrada.txt", "r");
        if (entrada == NULL) {
            perror("Erro ao abrir entrada.txt");
            fclose(saida);
            return 1;
        }
        lerDadosArquivo(entrada, saida);
        fclose(entrada);
    } else {
        printf("Opcao invalida!\n");
    }

    fclose(saida);
    printf("Dados armazenados no arquivo 'saida.txt'.\n");

    return 0;
}
