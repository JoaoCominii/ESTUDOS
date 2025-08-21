#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

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


// Fun��o para implementar o Radix Sort
/*
* O Radix Sort � um algoritmo de ordena��o baseado no conceito de classifica��o por d�gitos.
* Ele ordena os elementos processando um d�gito significativo por vez,
* come�ando pelos menos significativos  ou pelos mais significativos.
* O algoritmo utiliza m�todos de ordena��o est�vel, como Counting Sort, em cada etapa.
* Usando como chave realese_year:
* Considera os anos como n�meros inteiros.
* Processa cada d�gito (unidade, dezena, centena, etc.) separadamente.
* Analise de complexidade:
* Tempo: O Radix Sort � (O(n vezes k)), onde (n) � o n�mero de elementos e (k) � o n�mero de d�gitos m�ximos no maior n�mero.
* Mem�ria: (O(n)) de espa�o adicional.
*/

void radixsort(Show** shows, int n, int* comparacoes, int* movimentacoes) {
    // Encontra o maior n�mero em release_year para determinar o n�mero de d�gitos
    int max = shows[0]->Release_Year;
    for (int i = 1; i < n; i++) {
        if (shows[i]->Release_Year > max) {
            max = shows[i]->Release_Year;
        }
    }

    // Aplica Counting Sort para cada d�gito (unidade, dezena, centena, etc.)
    for (int exp = 1; max / exp > 0; exp *= 10) {
        countingSort(shows, n, exp, comparacoes, movimentacoes);
    }
}

// Fun��o de ordena��o est�vel (Counting Sort) para cada d�gito
void countingSort(Show** shows, int n, int exp, int* comparacoes, int* movimentacoes) {
    Show* output[n]; // Array auxiliar para armazenar os resultados
    int count[10] = {0};


    for (int i = 0; i < n; i++) {
        int digito = (shows[i]->Release_Year / exp) % 10;
        count[digito]++;
    }

    // Calcula os �ndices acumulativos
    for (int i = 1; i < 10; i++) {
        count[i] += count[i - 1];
    }

    // Constr�i o array ordenado
    for (int i = n - 1; i >= 0; i--) {
        int digito = (shows[i]->Release_Year / exp) % 10;
        output[--count[digito]] = shows[i];
        (*movimentacoes)++;
    }

    // Copia os elementos ordenados de volta para o array original
    for (int i = 0; i < n; i++) {
        shows[i] = output[i];
        (*movimentacoes)++;
    }

    // Ordena os blocos com o mesmo release_year por t�tulo
    int inicioBloco = 0;
    for (int i = 1; i < n; i++) {
        (*comparacoes)++;
        if (shows[i]->Release_Year != shows[i - 1]->Release_Year || i == n - 1) {
            ordenarBlocoPorTitulo(shows, inicioBloco, i, comparacoes, movimentacoes);
            inicioBloco = i;
        }
    }
}

// Fun��o para ordenar blocos pelo t�tulo
void ordenarBlocoPorTitulo(Show** shows, int inicio, int fim, int* comparacoes, int* movimentacoes) {
    for (int i = inicio; i < fim; i++) {
        for (int j = inicio; j < fim - 1; j++) {
            (*comparacoes)++;
            if (strcmp(shows[j]->Title, shows[j + 1]->Title) > 0) {
                Show* temp = shows[j];
                shows[j] = shows[j + 1];
                shows[j + 1] = temp;
                (*movimentacoes)++;
            }
        }
    }
}


// Fun��o principal
int main() {
    const char* caminhoArquivo = "/tmp/disneyplus.csv";
    int tamanho;

    Show** todosShows = carregarCSV(caminhoArquivo, &tamanho);
    if (!todosShows) return 1;

    Show* showsSelecionados[300];
    int contador = 0;

    char id[256];
    while (scanf("%s", id) != EOF) {
        if (strcmp(id, "FIM") == 0) break;

        for (int i = 0; i < tamanho; i++) {
            if (todosShows[i] && strcmp(todosShows[i]->Show_ID, id) == 0) {
                showsSelecionados[contador++] = todosShows[i];
                break;
            }
        }
    }

        // Inicializa os contadores de compara��es e movimenta��es
    int comparacoes = 0;
    int movimentacoes = 0;

    clock_t inicio = clock(); // Marca o in�cio do tempo de execu��o

    // Ordena os shows selecionados usando Shellsort
    radixsort(showsSelecionados, contador, &comparacoes, &movimentacoes);

    clock_t fim = clock(); // Marca o fim do tempo de execu��o
    double tempoExecucao = (double)(fim - inicio) / CLOCKS_PER_SEC;


    for(int i = 0; i < contador; i++) {
        imprimir(showsSelecionados[i]);
    }

    // Cria o arquivo de log
    FILE* logFile = fopen("matricula_radixsort.txt", "w");
    if (!logFile) {
        fprintf(stderr, "Erro ao criar o arquivo de log.\n");
        return 1;
    }
    fprintf(logFile, "00846713\t%d\t%d\t%.6f\n", comparacoes, movimentacoes, tempoExecucao);
    fclose(logFile);


    for (int i = 0; i < tamanho; i++) {
        freeShow(todosShows[i]);
    }
    free(todosShows);

    return 0;
}
