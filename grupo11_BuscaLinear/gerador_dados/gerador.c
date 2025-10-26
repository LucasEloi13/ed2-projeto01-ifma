#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/* * Para criar diretórios.
 * Em sistemas baseados em Unix (Linux, macOS), inclua:
 */
#include <sys/stat.h>
#include <sys/types.h>

/* * Se estiver no Windows, comente as duas linhas acima e descomente as duas abaixo:
 * #include <direct.h>
 * #define mkdir(path, mode) _mkdir(path)
 */

/**
 * @brief Cria os diretórios necessários para o arquivo de saída.
 * * @param n O tamanho do vetor (para o nome da pasta n<tam>).
 * @param dir_path O buffer para armazenar o caminho do diretório criado.
 */
void preparar_diretorios(int n, char* dir_path) {
    // Cria o diretório 'dados/' principal, se não existir.
    mkdir("dados", 0755);

    // Formata o nome do subdiretório conforme (ex: dados/n050000)
    sprintf(dir_path, "dados/n%06d", n);

    // Cria o subdiretório específico do tamanho 'n'.
    mkdir(dir_path, 0755);
}

/**
 * @brief Gera um arquivo .csv com 'n' números inteiros aleatórios.
 * * @param n O número de inteiros a gerar.
 * @param run_id O ID da execução (para a semente e nome do arquivo).
 * @param dir_path O caminho do diretório onde o arquivo será salvo.
 */
void gerar_arquivo(int n, int run_id, const char* dir_path) {
    char file_path[256];

    // Formata o nome do arquivo conforme (ex: .../run_023.csv)
    sprintf(file_path, "%s/run_%03d.csv", dir_path, run_id);

    FILE *fp = fopen(file_path, "w");
    if (fp == NULL) {
        fprintf(stderr, "Erro: Não foi possível criar o arquivo %s\n", file_path);
        exit(1);
    }

    // Usa o 'run_id' como semente para o gerador aleatório
    // Isso garante que cada uma das 50 execuções use uma "seed distinta".
    srand(run_id);
    // (Nota: para garantir sementes diferentes entre *diferentes* testes,
    //  alguns preferem srand(time(NULL) + run_id), mas usar run_id é
    //  suficiente e mais reprodutível).

    // Gera 'n' valores e escreve no arquivo
    for (int i = 0; i < n; i++) {
        fprintf(fp, "%d", rand());

        // Adiciona vírgula, exceto após o último elemento
        if (i < n - 1) {
            fprintf(fp, ",");
        }
    }
    // O PDF pede "uma única linha" .
    // Adicionar uma quebra de linha no final (\n) é uma boa prática
    // para arquivos de texto e não deve afetar a leitura.
    // fprintf(fp, "\n");

    fclose(fp);
    printf("Arquivo gerado: %s\n", file_path);
}

/**
 * @brief Ponto de entrada principal.
 * * Recebe o tamanho do vetor (n) e o ID da execução (run_id)
 * como argumentos de linha de comando.
 */
int main(int argc, char *argv[]) {
    // Verifica se os argumentos corretos foram passados
    if (argc != 3) {
        fprintf(stderr, "Uso: %s <tamanho_n> <run_id>\n", argv[0]);
        fprintf(stderr, "Exemplo: %s 50000 23\n", argv[0]);
        return 1;
    }

    // Converte os argumentos da linha de comando para inteiros
    int n = atoi(argv[1]);
    int run_id = atoi(argv[2]);

    if (n <= 0 || run_id <= 0) {
        fprintf(stderr, "Erro: Tamanho 'n' e 'run_id' devem ser maiores que 0.\n");
        return 1;
    }

    char dir_path[128];
    preparar_diretorios(n, dir_path);
    gerar_arquivo(n, run_id, dir_path);

    return 0;
}
