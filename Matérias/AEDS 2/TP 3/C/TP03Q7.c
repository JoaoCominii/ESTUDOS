#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#define MAX_LISTA 300
#define TAM_FILA 5

void limparEspacos(char** array, int n) {
    for (int i = 0; i < n; i++) {
        char* temp = strdup(array[i]);
        char* ptr = temp;
        // Remove espa�os do in�cio
        while (*ptr == ' ') {
            ptr++;
        }
        // Atualiza o valor no array
        array[i] = strdup(ptr);
        free(temp);
    }
}

// Fun��o auxiliar para trocar elementos em um array de strings
void trocar(char** a, char** b) {
    char* temp = *a;
    *a = *b;
    *b = temp;
}

// Fun��o de parti��o para o Quick Sort
int particao(char** array, int low, int high) {
    char* pivot = array[high]; // Piv�
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (strcmp(array[j], pivot) < 0) { // Compara strings em ordem alfab�tica
            i++;
            trocar(&array[i], &array[j]);
        }
    }
    trocar(&array[i + 1], &array[high]);
    return i + 1;
}

// Fun��o principal do Quick Sort
void quickSort(char** array, int low, int high) {
    if (low < high) {
        int pi = particao(array, low, high); // Obt�m o �ndice de parti��o

        quickSort(array, low, pi - 1);  // Ordena os elementos antes da parti��o
        quickSort(array, pi + 1, high); // Ordena os elementos depois da parti��o
    }
}

// Fun��o auxiliar para remover aspas do in�cio e do final de uma string
char* removerAspas(const char* src) {
    if (src == NULL || strlen(src) == 0) return strdup("NaN");
    char* result = strdup(src);
    size_t len = strlen(result);

    if (len > 0 && result[0] == '"' && result[len - 1] == '"') {
        result[len - 1] = '\0';
        memmove(result, result + 1, len - 1);
    }
    return result;
}

// Fun��o para dividir uma linha CSV, lidando com aspas e v�rgulas manualmente
char** dividirCSV(const char* linha, int* count) {
    if (linha == NULL || strlen(linha) == 0) {
        *count = 0;
        return NULL;
    }

    char** resultado = NULL;
    int tamanho = 0;
    char* copia = strdup(linha);
    char* ptr = copia;
    bool dentroAspas = false;
    char buffer[2048];
    int bufferIndex = 0;

    while (*ptr != '\0') {
        if (*ptr == '"' && (ptr == copia || *(ptr - 1) != '\\')) {
            dentroAspas = !dentroAspas; // Alterna dentro ou fora das aspas
        } else if (*ptr == ',' && !dentroAspas) {
            buffer[bufferIndex] = '\0';
            resultado = realloc(resultado, (tamanho + 1) * sizeof(char*));
            resultado[tamanho++] = removerAspas(buffer);
            bufferIndex = 0;
        } else {
            buffer[bufferIndex++] = *ptr;
        }
        ptr++;
    }

    // Adicionar o �ltimo campo ao resultado
    buffer[bufferIndex] = '\0';
    resultado = realloc(resultado, (tamanho + 1) * sizeof(char*));
    resultado[tamanho++] = removerAspas(buffer);

    free(copia);
    *count = tamanho;
    return resultado;
}

// Estrutura para armazenar informa��es do Show
typedef struct {
    char* Show_ID;
    char* Type;
    char* Title;
    char* Director;
    char** Cast;
    int Cast_Count;
    char* Country;
    char* Date_Added;
    int Release_Year;
    char* Rating;
    char* Duration;
    char** Listed_In;
    int Listed_In_Count;
} Show;

// Fun��o para liberar mem�ria alocada de um Show
void freeShow(Show* show) {
    free(show->Show_ID);
    free(show->Type);
    free(show->Title);
    free(show->Director);

    for (int i = 0; i < show->Cast_Count; i++) {
        free(show->Cast[i]);
    }
    free(show->Cast);

    free(show->Country);
    free(show->Date_Added);
    free(show->Rating);
    free(show->Duration);

    for (int i = 0; i < show->Listed_In_Count; i++) {
        free(show->Listed_In[i]);
    }
    free(show->Listed_In);
}

// Fun��o para imprimir informa��es do Show
void imprimir(Show* show) {
    printf("=> %s ## %s ## %s ## %s ## [", show->Show_ID, show->Title, show->Type, show->Director);

    for (int i = 0; i < show->Cast_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona v�rgula antes dos pr�ximos nomes
        }
        printf("%s", show->Cast[i]);
    }
    if (show->Cast_Count == 0) {
        printf("NaN");
    }

    printf("] ## %s ## %s ## %d ## %s ## %s ## [", show->Country, show->Date_Added, show->Release_Year, show->Rating, show->Duration);

    for (int i = 0; i < show->Listed_In_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona v�rgula antes dos pr�ximos itens
        }
        printf("%s", show->Listed_In[i]);
    }
    if (show->Listed_In_Count == 0) {
        printf("NaN");
    }

    printf("] ##\n");
}

// Fun��o para processar e carregar os dados do CSV
Show** carregarCSV(const char* caminhoArquivo, int* tamanho) {
    FILE* file = fopen(caminhoArquivo, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return NULL;
    }

    Show** shows = NULL;
    char linha[2048];
    int contador = 0;

    fgets(linha, sizeof(linha), file); // Ignorar cabe�alho

    while (fgets(linha, sizeof(linha), file)) {
        int numCampos;
        char** valores = dividirCSV(linha, &numCampos);

        // Tratar valores ausentes como "NaN"
        for (int i = 0; i < numCampos; i++) {
            if (valores[i] == NULL || strlen(valores[i]) == 0) {
                valores[i] = strdup("NaN");
            }
        }

        // Criar objeto Show
        Show* show = malloc(sizeof(Show));
        show->Show_ID = valores[0];
        show->Type = valores[1];
        show->Title = valores[2];
        show->Director = valores[3];
        show->Cast = dividirCSV(valores[4], &show->Cast_Count);
        show->Country = valores[5];
        show->Date_Added = valores[6];
        show->Release_Year = atoi(valores[7]);
        show->Rating = valores[8];
        show->Duration = valores[9];
        show->Listed_In = dividirCSV(valores[10], &show->Listed_In_Count);

        // Limpar espa�os antes de ordenar
        limparEspacos(show->Cast, show->Cast_Count);
        limparEspacos(show->Listed_In, show->Listed_In_Count);

        // Ordenar `Cast` e `Listed_In` usando Quick Sort
        if (show->Cast_Count > 1) {
            quickSort(show->Cast, 0, show->Cast_Count - 1);
        }
        if (show->Listed_In_Count > 1) {
            quickSort(show->Listed_In, 0, show->Listed_In_Count - 1);
        }

        shows = realloc(shows, (contador + 1) * sizeof(Show*));
        shows[contador++] = show;
    }

    fclose(file);
    *tamanho = contador;
    return shows;
}

// Fila com alocação flexível (tamanho máximo 5)
typedef struct {
    Show** array;
    int primeiro;
    int ultimo;
    int tamanho;
    int capacidade;
} FilaFlex;

// Inicializa a fila flexível
void criarFila(FilaFlex *fila, int capacidade) {
    fila->array = (Show**) malloc(capacidade * sizeof(Show*));
    fila->primeiro = 0;
    fila->ultimo = 0;
    fila->tamanho = 0;
    fila->capacidade = capacidade;
}

// Insere um Show na fila flexível
void inserirFila(FilaFlex *fila, Show* show) {
    if (fila->tamanho == fila->capacidade) {
        // Remove o mais antigo (avanço circular)
        fila->primeiro = (fila->primeiro + 1) % fila->capacidade;
        fila->tamanho--;
    }
    fila->array[fila->ultimo] = show;
    fila->ultimo = (fila->ultimo + 1) % fila->capacidade;
    fila->tamanho++;
}

// Remove um Show da fila flexível
Show* removerFila(FilaFlex *fila) {
    if (fila->tamanho == 0) {
        printf("Erro ao remover da fila!\n");
        exit(1);
    }
    Show* removido = fila->array[fila->primeiro];
    fila->primeiro = (fila->primeiro + 1) % fila->capacidade;
    fila->tamanho--;
    return removido;
}

// Calcula a média truncada dos Release_Year dos elementos da fila
int mediaReleaseYear(FilaFlex *fila) {
    if (fila->tamanho == 0) return 0;
    int soma = 0;
    for (int i = 0, idx = fila->primeiro; i < fila->tamanho; i++, idx = (idx + 1) % fila->capacidade) {
        soma += fila->array[idx]->Release_Year;
    }
    double media = (double)soma / fila->tamanho;
    return (int)(media); // truncada
}

// Exibe todos os elementos da fila flexível no padrão pedido
void mostrarFila(FilaFlex *fila) {
    int idx = fila->primeiro;
    for (int i = 0; i < fila->tamanho; i++, idx = (idx + 1) % fila->capacidade) {
        printf("[%d] ", i);
        imprimir(fila->array[idx]);
    }
}

// Libera a memória da fila flexível
void liberarFila(FilaFlex *fila) {
    free(fila->array);
}

// --- Função auxiliar: busca um Show pelo seu ID ---
// "todosShows" � um array de ponteiros para Show, com "tamanho" elementos.
Show* buscarShow(Show** todosShows, int tamanho, const char* id) {
    for (int i = 0; i < tamanho; i++) {
        if (strcmp(todosShows[i]->Show_ID, id) == 0) {
            return todosShows[i];
        }
    }
    return NULL;
}

// --- Fun��o principal ---
int main() {
    FilaFlex fila;
    criarFila(&fila, 5); // tamanho máximo 5

    int totalShows;
    Show** todosShows = carregarCSV("/tmp/disneyplus.csv", &totalShows);
    if (!todosShows) {
        printf("Erro ao carregar os dados do CSV.\n");
        return 1;
    }

    // Leitura dos IDs iniciais até encontrar "FIM"
    char id[100];
    while (scanf("%s", id) != EOF) {
        if (strcmp(id, "FIM") == 0)
            break;
        Show* novoShow = buscarShow(todosShows, totalShows, id);
        if (novoShow != NULL) {
            inserirFila(&fila, novoShow);
            printf("[Media] %d\n", mediaReleaseYear(&fila));
        } else {
            printf("Erro: Show ID %s não encontrado!\n", id);
        }
    }

    // Leitura do número de operações
    int nOps;
    scanf("%d", &nOps);
    for (int i = 0; i < nOps; i++) {
        char comando[10];
        scanf("%s", comando);
        if (strcmp(comando, "I") == 0) {
            char showID[100];
            scanf("%s", showID);
            Show* novoShow = buscarShow(todosShows, totalShows, showID);
            if (novoShow != NULL) {
                inserirFila(&fila, novoShow);
                printf("[Media] %d\n", mediaReleaseYear(&fila));
            }
        }
        else if (strcmp(comando, "R") == 0) {
            Show* removido = removerFila(&fila);
            printf("(R) %s\n", removido->Title);
        }
        else {
            printf("Erro: Comando inválido.\n");
        }
    }

    // Exibe a fila final após as operações
    mostrarFila(&fila);

    // Libera a memória alocada para os Shows
    for (int i = 0; i < totalShows; i++) {
        freeShow(todosShows[i]);
    }
    free(todosShows);
    liberarFila(&fila);

    return 0;
}
