#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h> // Para sqrt() (desvio padrão)

/* * Para criar diretórios.
 * Em sistemas baseados em Unix (Linux, macOS), inclua:
 */
#include <sys/stat.h>
#include <sys/types.h>

/* * Se estiver no Windows, comente as duas linhas acima e descomente as duas abaixo:
 * #include <direct.h>
 * #define mkdir(path, mode) _mkdir(path)
 */

#define MAX_LINE_SIZE 2000000 // Tamanho máximo da linha CSV (2MB)
#define NUM_EXECUCOES 50

/**
 * @brief Implementação do pseudocódigo BETTER-LINEAR-SEARCH[cite: 1].
 * Procura por 'x' no arranjo 'A' de 'n' elementos.
 * @param A O arranjo (vetor)[cite: 1].
 * @param n O número de elementos em A[cite: 1].
 * @param x O valor que buscamos[cite: 1].
 * @return O índice i (0 a n-1) onde A[i] == x, ou -1 (NOT-FOUND)[cite: 1].
 */
int linear_search(int A[], int n, int x) {
    // 1. Para i = 1 até n: (em C, de 0 até n-1) [cite: 1, 3]
    for (int i = 0; i < n; i++) {
        // a. Se A[i] = x, então retorne i. [cite: 1, 3]
        if (A[i] == x) {
            return i;
        }
    }
    // 2. Retorne NOT-FOUND (usando -1 como índice inválido) [cite: 1, 3]
    return -1;
}

/**
 * @brief Lê um vetor de inteiros de um arquivo CSV de linha única[cite: 72].
 * @param filepath O caminho para o arquivo .csv.
 * @param n O tamanho do vetor.
 * @return Um ponteiro para o vetor alocado dinamicamente.
 */
int* ler_vetor_de_csv(const char* filepath, int n) {
    FILE* fp = fopen(filepath, "r");
    if (fp == NULL) {
        fprintf(stderr, "Erro: Não foi possível abrir o arquivo %s\n", filepath);
        exit(1);
    }

    // Aloca memória para o vetor e para a linha
    int* vetor = (int*)malloc(n * sizeof(int));
    char* line_buffer = (char*)malloc(MAX_LINE_SIZE * sizeof(char));
    if (vetor == NULL || line_buffer == NULL) {
        fprintf(stderr, "Erro: Falha na alocação de memória.\n");
        exit(1);
    }

    // Lê a linha única [cite: 72]
    fgets(line_buffer, MAX_LINE_SIZE, fp);

    int index = 0;
    char* token = strtok(line_buffer, ","); // Divide a string pela vírgula
    while (token != NULL && index < n) {
        vetor[index++] = atoi(token);
        token = strtok(NULL, ",");
    }

    fclose(fp);
    free(line_buffer);
    return vetor;
}

/**
 * @brief Gera uma chave de busca aleatória.
 * Com ~50% de chance de sucesso (chave está no vetor) [cite: 84]
 * e ~50% de chance de falha (chave não está no vetor).
 * @param A O arranjo.
 * @param n O tamanho do arranjo.
 * @return A chave (int) a ser buscada.
 */
int gerar_chave_busca(int A[], int n) {
    int chave;
    
    // Sorteia se a busca terá sucesso (0) ou falha (1) [cite: 84]
    if (rand() % 2 == 0) {
        // SUCESSO: Pega um elemento aleatório de dentro do vetor
        int index_aleatorio = rand() % n;
        chave = A[index_aleatorio];
    } else {
        // FALHA: Gera um número que (muito provavelmente) não está no vetor.
        // Uma estratégia é encontrar o máximo e gerar um número maior.
        // Este tempo de geração NÃO é medido, conforme[cite: 89].
        int max_val = A[0];
        for (int i = 1; i < n; i++) {
            if (A[i] > max_val) {
                max_val = A[i];
            }
        }
        // Gera um número aleatório maior que o máximo
        chave = max_val + (rand() % 1000) + 1;
    }
    return chave;
}

/**
 * @brief Calcula a média de um vetor de tempos.
 */
double calcular_media(double tempos[], int num_execucoes) {
    double soma = 0.0;
    for (int i = 0; i < num_execucoes; i++) {
        soma += tempos[i];
    }
    return soma / num_execucoes;
}

/**
 * @brief Calcula o desvio padrão de um vetor de tempos.
 */
double calcular_desvio_padrao(double tempos[], int num_execucoes, double media) {
    double soma_diff_quad = 0.0;
    for (int i = 0; i < num_execucoes; i++) {
        soma_diff_quad += (tempos[i] - media) * (tempos[i] - media);
    }
    // Usa n-1 (Bessel's correction) para amostra
    return sqrt(soma_diff_quad / (num_execucoes - 1));
}

/**
 * @brief Prepara os diretórios de saída conforme[cite: 93].
 */
void preparar_diretorios_saida() {
    mkdir("resultados", 0755);
    mkdir("resultados/estatisticas", 0755); // [cite: 93]
}

/**
 * @brief Ponto de entrada principal: O "Harness" Experimental.
 */
int main() {
    // Garante sementes aleatórias diferentes a cada execução do programa
    srand(time(NULL)); 
    
    preparar_diretorios_saida();

    // Abre o arquivo de resultados da linguagem C [cite: 94]
    FILE* fp_resultados = fopen("resultados/estatisticas/resultados_C.csv", "w");
    if (fp_resultados == NULL) {
        fprintf(stderr, "Erro: Não foi possível criar o arquivo de resultados.\n");
        return 1;
    }
    
    // Escreve o cabeçalho do CSV de resultados [cite: 96]
    fprintf(fp_resultados, "n,tempo_ms,desvio\n");

    printf("Iniciando experimentos para Busca Linear em C...\n");

    // Loop pelos tamanhos de N (10k, 20k, ..., 100k) [cite: 79]
    for (int n = 10000; n <= 100000; n += 10000) {
        
        double tempos_execucao[NUM_EXECUCOES];
        printf("Processando para N = %d...\n", n);

        // Loop pelas 50 execuções independentes [cite: 77]
        for (int run_id = 1; run_id <= NUM_EXECUCOES; run_id++) {
            char filepath[256];
            // Monta o nome do arquivo de dados (ex: dados/n010000/run_001.csv) [cite: 74-76]
            sprintf(filepath, "dados/n%06d/run_%03d.csv", n, run_id);

            // --- Etapa de Preparação (Tempo NÃO contado) ---
            int* vetor = ler_vetor_de_csv(filepath, n);
            int chave = gerar_chave_busca(vetor, n); // [cite: 83, 84, 89]
            
            // --- Etapa de Medição (Tempo CONTADO) --- [cite: 86, 89]
            // Usa clock() para medição de tempo de CPU [cite: 92]
            clock_t inicio = clock();
            
            linear_search(vetor, n, chave); // Executa o algoritmo
            
            clock_t fim = clock();
            // --- Fim da Medição ---

            // Calcula o tempo em milissegundos (ms) 
            double tempo_gasto_ms = ((double)(fim - inicio) * 1000.0) / CLOCKS_PER_SEC;
            tempos_execucao[run_id - 1] = tempo_gasto_ms;

            // Libera a memória do vetor lido
            free(vetor);
        }

        // --- Etapa de Consolidação (Após as 50 execuções) ---
        double media = calcular_media(tempos_execucao, NUM_EXECUCOES); // 
        double desvio = calcular_desvio_padrao(tempos_execucao, NUM_EXECUCOES, media); // 

        // Salva a linha de resultado para este 'n' [cite: 96, 97]
        fprintf(fp_resultados, "%d,%.6f,%.6f\n", n, media, desvio);
    }

    fclose(fp_resultados);
    printf("Experimentos em C concluídos. Resultados salvos em:\n");
    printf("resultados/estatisticas/resultados_C.csv\n");

    return 0;
}