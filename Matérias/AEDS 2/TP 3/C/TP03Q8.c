#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

void limparEspacos(char** array, int n) {
    for (int i = 0; i < n; i++) {
        char* temp = strdup(array[i]);
        char* ptr = temp;
        // Remove espaços do início
        while (*ptr == ' ') {
            ptr++;
        }
        // Libera o valor antigo antes de sobrescrever
        free(array[i]);
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

// Fun��o para ordenar os shows usando Quicksort
void quicksort(Show** shows, int esq, int dir, int* comparacoes, int* movimentacoes) {
    if (esq >= dir) {
        return; // Caso base: parti��o tem 0 ou 1 elemento
    }

    // Escolhe o piv� como o elemento central
    Show* pivo = shows[(esq + dir) / 2];
    int i = esq, j = dir;

    while (i <= j) {
        // Move "i" para frente enquanto o elemento for menor que o piv�
        while (compararShows(shows[i], pivo) < 0) {
            (*comparacoes)++;
            i++;
        }

        // Move "j" para tr�s enquanto o elemento for maior que o piv�
        while (compararShows(shows[j], pivo) > 0) {
            (*comparacoes)++;
            j--;
        }

        if (i <= j) {
            // Troca os elementos nas posi��es "i" e "j"
            Show* temp = shows[i];
            shows[i] = shows[j];
            shows[j] = temp;
            (*movimentacoes)++;
            i++;
            j--;
        }
    }

    // Ordena recursivamente as parti��es
    quicksort(shows, esq, j, comparacoes, movimentacoes);
    quicksort(shows, i, dir, comparacoes, movimentacoes);
}

// Fun��o para comparar dois Shows (por date_added, depois por title)
int compararShows(const Show* show1, const Show* show2) {
    char dataISO1[20], dataISO2[20];

    // Converte as datas para o formato "YYYY-MM-DD"
    converterData(show1->Date_Added, dataISO1);
    converterData(show2->Date_Added, dataISO2);

    // Compara as datas
    int cmp = strcmp(dataISO1, dataISO2);
    if (cmp != 0) {
        return cmp;
    }

    // Desempate por title
    return strcmp(show1->Title, show2->Title);
}

// Fun��o para converter a data no formato "September 24, 2021" para "YYYY-MM-DD"
int converterData(const char* data, char* formatoISO) {
    const char* dataPadrao = "March 1, 1900";
    if (data == NULL || strcmp(data, "NaN") == 0 || strlen(data) == 0) {
        data = dataPadrao;
    }
    char mes[20];
    int dia, ano;
    if (sscanf(data, "%s %d, %d", mes, &dia, &ano) != 3) {
        strcpy(formatoISO, "9999-99-99");
        return 0;
    }
    int numeroMes;
    if (strcmp(mes, "January") == 0) numeroMes = 1;
    else if (strcmp(mes, "February") == 0) numeroMes = 2;
    else if (strcmp(mes, "March") == 0) numeroMes = 3;
    else if (strcmp(mes, "April") == 0) numeroMes = 4;
    else if (strcmp(mes, "May") == 0) numeroMes = 5;
    else if (strcmp(mes, "June") == 0) numeroMes = 6;
    else if (strcmp(mes, "July") == 0) numeroMes = 7;
    else if (strcmp(mes, "August") == 0) numeroMes = 8;
    else if (strcmp(mes, "September") == 0) numeroMes = 9;
    else if (strcmp(mes, "October") == 0) numeroMes = 10;
    else if (strcmp(mes, "November") == 0) numeroMes = 11;
    else if (strcmp(mes, "December") == 0) numeroMes = 12;
    else {
        strcpy(formatoISO, "9999-99-99");
        return 0;
    }
    sprintf(formatoISO, "%04d-%02d-%02d", ano, numeroMes, dia);
    return 1;
}

// Nó da lista duplamente encadeada
typedef struct No {
    Show* show;
    struct No* ant;
    struct No* prox;
} No;

// Lista duplamente encadeada
typedef struct {
    No* inicio;
    No* fim;
    int tamanho;
} ListaDupla;

// Inicializa a lista
void inicializarLista(ListaDupla* lista) {
    lista->inicio = lista->fim = NULL;
    lista->tamanho = 0;
}

// Insere no final
void inserirFim(ListaDupla* lista, Show* show) {
    No* no = malloc(sizeof(No));
    no->show = show;
    no->prox = NULL;
    no->ant = lista->fim;
    if (lista->fim) lista->fim->prox = no;
    else lista->inicio = no;
    lista->fim = no;
    lista->tamanho++;
}

// Libera a lista (não libera os Shows, só os nós)
void liberarLista(ListaDupla* lista) {
    No* atual = lista->inicio;
    while (atual) {
        No* prox = atual->prox;
        free(atual);
        atual = prox;
    }
}

// Troca os Shows de dois nós
void trocarNos(No* a, No* b) {
    Show* tmp = a->show;
    a->show = b->show;
    b->show = tmp;
}

// Particiona e retorna o nó do pivô
No* particaoLista(No* esq, No* dir, int* comp, int* mov) {
    Show* pivo = dir->show;
    No* i = esq->ant;
    for (No* j = esq; j != dir; j = j->prox) {
        (*comp)++;
        if (compararShows(j->show, pivo) < 0) {
            i = (i == NULL) ? esq : i->prox;
            trocarNos(i, j);
            (*mov)++;
        }
    }
    i = (i == NULL) ? esq : i->prox;
    trocarNos(i, dir);
    (*mov)++;
    return i;
}

// Quicksort recursivo para lista duplamente encadeada
void quicksortLista(No* esq, No* dir, int* comp, int* mov) {
    if (dir != NULL && esq != dir && esq != dir->prox) {
        No* p = particaoLista(esq, dir, comp, mov);
        quicksortLista(esq, p->ant, comp, mov);
        quicksortLista(p->prox, dir, comp, mov);
    }
}

// Imprime a lista
void imprimirLista(ListaDupla* lista) {
    for (No* atual = lista->inicio; atual; atual = atual->prox) {
        imprimir(atual->show);
    }
}

// Fun��o principal
int main() {
    const char* caminhoArquivo = "/tmp/disneyplus.csv";
    int tamanho;

    Show** todosShows = carregarCSV(caminhoArquivo, &tamanho);
    if (!todosShows) return 1;

    ListaDupla lista;
    inicializarLista(&lista);

    char id[256];
    while (scanf("%s", id) != EOF) {
        if (strcmp(id, "FIM") == 0) break;
        for (int i = 0; i < tamanho; i++) {
            if (todosShows[i] && strcmp(todosShows[i]->Show_ID, id) == 0) {
                inserirFim(&lista, todosShows[i]);
                break;
            }
        }
    }

    int comparacoes = 0, movimentacoes = 0;
    clock_t inicio = clock();

    if (lista.tamanho > 1)
        quicksortLista(lista.inicio, lista.fim, &comparacoes, &movimentacoes);

    clock_t fim = clock();
    double tempoExecucao = (double)(fim - inicio) / CLOCKS_PER_SEC;

    imprimirLista(&lista);

    FILE* logFile = fopen("matricula_quicksort2.txt", "w");
    if (!logFile) {
        fprintf(stderr, "Erro ao criar o arquivo de log.\n");
        return 1;
    }
    fprintf(logFile, "00846713\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, tempoExecucao);
    fclose(logFile);

    liberarLista(&lista);
    for (int i = 0; i < tamanho; i++) {
        freeShow(todosShows[i]);
    }
    free(todosShows);

    return 0;
}
