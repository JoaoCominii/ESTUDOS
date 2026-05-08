#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#define MAX_LISTA 300

void limparEspacos(char** array, int n) {
    for (int i = 0; i < n; i++) {
        char* temp = strdup(array[i]);
        char* ptr = temp;
        // Remove espaços do início
        while (*ptr == ' ') {
            ptr++;
        }
        // Atualiza o valor no array
        array[i] = strdup(ptr);
        free(temp);
    }
}

// Função auxiliar para trocar elementos em um array de strings
void trocar(char** a, char** b) {
    char* temp = *a;
    *a = *b;
    *b = temp;
}

// Função de partição para o Quick Sort
int particao(char** array, int low, int high) {
    char* pivot = array[high]; // Pivô
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (strcmp(array[j], pivot) < 0) { // Compara strings em ordem alfabética
            i++;
            trocar(&array[i], &array[j]);
        }
    }
    trocar(&array[i + 1], &array[high]);
    return i + 1;
}

// Função principal do Quick Sort
void quickSort(char** array, int low, int high) {
    if (low < high) {
        int pi = particao(array, low, high); // Obtém o índice de partição

        quickSort(array, low, pi - 1);  // Ordena os elementos antes da partição
        quickSort(array, pi + 1, high); // Ordena os elementos depois da partição
    }
}

// Função auxiliar para remover aspas do início e do final de uma string
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

// Função para dividir uma linha CSV, lidando com aspas e vírgulas manualmente
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

    // Adicionar o último campo ao resultado
    buffer[bufferIndex] = '\0';
    resultado = realloc(resultado, (tamanho + 1) * sizeof(char*));
    resultado[tamanho++] = removerAspas(buffer);

    free(copia);
    *count = tamanho;
    return resultado;
}

// Estrutura para armazenar informações do Show
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

// Função para liberar memória alocada de um Show
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

// Função para imprimir informações do Show
void imprimir(Show* show) {
    printf("=> %s ## %s ## %s ## %s ## [", show->Show_ID, show->Title, show->Type, show->Director);

    for (int i = 0; i < show->Cast_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona vírgula antes dos próximos nomes
        }
        printf("%s", show->Cast[i]);
    }
    if (show->Cast_Count == 0) {
        printf("NaN");
    }

    printf("] ## %s ## %s ## %d ## %s ## %s ## [", show->Country, show->Date_Added, show->Release_Year, show->Rating, show->Duration);

    for (int i = 0; i < show->Listed_In_Count; i++) {
        if (i > 0) {
            printf(", "); // Adiciona vírgula antes dos próximos itens
        }
        printf("%s", show->Listed_In[i]);
    }
    if (show->Listed_In_Count == 0) {
        printf("NaN");
    }

    printf("] ##\n");
}

// Função para processar e carregar os dados do CSV
Show** carregarCSV(const char* caminhoArquivo, int* tamanho) {
    FILE* file = fopen(caminhoArquivo, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return NULL;
    }

    Show** shows = NULL;
    char linha[2048];
    int contador = 0;

    fgets(linha, sizeof(linha), file); // Ignorar cabeçalho

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

        // Limpar espaços antes de ordenar
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

// Estrutura da lista sequencial
// Estrutura da lista sequencial (armazena ponteiros para Show)
typedef struct {
    Show* array[MAX_LISTA];
    int n;
} Lista;

// --- Funções da Lista ---

// Inicializa a lista vazia
void criarLista(Lista *lista) {
    lista->n = 0;
}

// Insere um Show na primeira posição
void inserirInicio(Lista *lista, Show* show) {
    if (lista->n >= MAX_LISTA) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > 0; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[0] = show;
    lista->n++;
}

// Insere um Show no fim
void inserirFim(Lista *lista, Show* show) {
    if (lista->n >= MAX_LISTA) {
        printf("Erro ao inserir!\n");
        return;
    }
    lista->array[lista->n++] = show;
}

// Insere um Show em uma posição específica
void inserir(Lista *lista, Show* show, int pos) {
    if (lista->n >= MAX_LISTA || pos < 0 || pos > lista->n) {
        printf("Erro ao inserir!\n");
        return;
    }
    for (int i = lista->n; i > pos; i--) {
        lista->array[i] = lista->array[i - 1];
    }
    lista->array[pos] = show;
    lista->n++;
}

// Remove o primeiro elemento da lista
Show* removerInicio(Lista *lista) {
    if (lista->n == 0) {
        printf("Erro ao remover!\n");
        exit(1);
    }
    Show* removido = lista->array[0];
    for (int i = 0; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return removido;
}

// Remove o último elemento da lista
Show* removerFim(Lista *lista) {
    if (lista->n == 0) {
        printf("Erro ao remover!\n");
        exit(1);
    }
    return lista->array[--lista->n];
}

// Remove um elemento de uma posição específica
Show* remover(Lista *lista, int pos) {
    if (lista->n == 0 || pos < 0 || pos >= lista->n) {
        printf("Erro ao remover!\n");
        exit(1);
    }
    Show* removido = lista->array[pos];
    for (int i = pos; i < lista->n - 1; i++) {
        lista->array[i] = lista->array[i + 1];
    }
    lista->n--;
    return removido;
}

// Exibe todos os elementos da lista chamando a função imprimir(Show*)
void mostrar(Lista *lista) {
    for (int i = 0; i < lista->n; i++) {
        imprimir(lista->array[i]);
    }
}

// --- Função auxiliar: busca um Show pelo seu ID ---
// "todosShows" é um array de ponteiros para Show, com "tamanho" elementos.
Show* buscarShow(Show** todosShows, int tamanho, const char* id) {
    for (int i = 0; i < tamanho; i++) {
        if (strcmp(todosShows[i]->Show_ID, id) == 0) {
            return todosShows[i];
        }
    }
    return NULL;
}

// --- Função principal ---
int main() {
    Lista lista;
    criarLista(&lista);

    int totalShows;
    // Carrega os registros do CSV. O arquivo deve estar no caminho indicado.
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
            inserirFim(&lista, novoShow);
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
        if (strcmp(comando, "II") == 0) {
            // Inserir no início: lê o showID
            char showID[100];
            scanf("%s", showID);
            Show* novoShow = buscarShow(todosShows, totalShows, showID);
            if (novoShow != NULL)
                inserirInicio(&lista, novoShow);
        }
        else if (strcmp(comando, "IF") == 0) {
            // Inserir no fim: lê o showID
            char showID[100];
            scanf("%s", showID);
            Show* novoShow = buscarShow(todosShows, totalShows, showID);
            if (novoShow != NULL)
                inserirFim(&lista, novoShow);
        }
        else if (strcmp(comando, "I*") == 0) {
            // Inserir em posição específica:
            // **Ordem dos parâmetros:** primeiro um inteiro (posição) e depois o showID.
            int pos;
            char showID[100];
            scanf("%d", &pos);
            scanf("%s", showID);
            Show* novoShow = buscarShow(todosShows, totalShows, showID);
            if (novoShow != NULL)
                inserir(&lista, novoShow, pos);
        }
        else if (strcmp(comando, "RI") == 0) {
            // Remover do início
            Show* removido = removerInicio(&lista);
            printf("(R) %s\n", removido->Title);
        }
        else if (strcmp(comando, "RF") == 0) {
            // Remover do fim
            Show* removido = removerFim(&lista);
            printf("(R) %s\n", removido->Title);
        }
        else if (strcmp(comando, "R*") == 0) {
            // Remover de posição específica
            int pos;
            scanf("%d", &pos);
            Show* removido = remover(&lista, pos);
            printf("(R) %s\n", removido->Title);
        }
        else {
            printf("Erro: Comando inválido.\n");
        }
    }

    // Exibe a lista final após as operações
    mostrar(&lista);

    // Libera a memória alocada para os Shows
    for (int i = 0; i < totalShows; i++) {
        freeShow(todosShows[i]);
    }
    free(todosShows);

    return 0;
}
