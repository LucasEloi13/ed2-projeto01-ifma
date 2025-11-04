import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configurações do projeto de análise de algoritmos de busca.
 */
public class Config {
    
    // Diretórios base
    public static final Path BASE_DIR = Paths.get("").toAbsolutePath()
            .getParent().getParent();
    public static final Path DADOS_DIR = BASE_DIR.resolve("dados");
    public static final Path RESULTADOS_DIR = BASE_DIR.resolve("resultados")
            .resolve("brutos").resolve("estatisticas");
    
    // Configurações de execução
    public static final int[] TAMANHOS_VETOR = {
        10000, 20000, 30000, 40000, 50000, 
        60000, 70000, 80000, 90000, 100000
    };
    public static final int NUM_EXECUCOES = 50;
    
    // Arquivo de resultados
    public static final String ARQUIVO_RESULTADOS_JAVA = "resultados_Java.csv";
    
    /**
     * Constrói o nome do diretório para um tamanho específico.
     * 
     * @param tamanho Tamanho do vetor
     * @return Nome do diretório (ex: n050000)
     */
    public static String formatarDiretorioTamanho(int tamanho) {
        return String.format("n%06d", tamanho);
    }
    
    /**
     * Constrói o nome do arquivo para uma execução específica.
     * 
     * @param idExecucao ID da execução (1-50)
     * @return Nome do arquivo (ex: run_023.csv)
     */
    public static String formatarArquivoRun(int idExecucao) {
        return String.format("run_%03d.csv", idExecucao);
    }
}
