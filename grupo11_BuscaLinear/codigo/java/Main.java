import utils.CSVWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Programa principal para execução dos experimentos de busca linear.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EXPERIMENTO: Análise de Performance da Busca Linear (Java)");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Inicializa o experimento
        ExperimentoBusca experimento = new ExperimentoBusca(Config.DADOS_DIR);
        
        // Verifica disponibilidade dos dados
        System.out.println("Verificando disponibilidade dos dados...");
        Map<Integer, Integer> infoDados = experimento.verificarDisponibilidadeDados(
            Config.TAMANHOS_VETOR
        );
        
        System.out.println("\nResumo dos dados disponíveis:");
        int totalArquivos = 0;
        for (Map.Entry<Integer, Integer> entry : infoDados.entrySet()) {
            System.out.printf("  n=%6d: %d arquivos encontrados%n", 
                entry.getKey(), entry.getValue());
            totalArquivos += entry.getValue();
        }
        System.out.println();
        
        // Verifica se há dados suficientes
        if (totalArquivos == 0) {
            System.err.println("ERRO: Nenhum arquivo de dados encontrado!");
            System.err.println("Certifique-se de que os dados estão em: " + Config.DADOS_DIR);
            return;
        }
        
        // Executa os experimentos
        System.out.printf("Iniciando experimentos com %d execuções por tamanho...%n", 
            Config.NUM_EXECUCOES);
        System.out.println("-".repeat(70));
        
        List<CSVWriter.Resultado> resultados = experimento.executarExperimentoCompleto(
            Config.TAMANHOS_VETOR,
            Config.NUM_EXECUCOES
        );
        
        // Salva os resultados
        if (!resultados.isEmpty()) {
            try {
                CSVWriter.salvarResultadosCSV(
                    Config.RESULTADOS_DIR.resolve(Config.ARQUIVO_RESULTADOS_JAVA),
                    resultados
                );
                
                System.out.println();
                System.out.println("-".repeat(70));
                System.out.println("Resultados salvos em: " + 
                    Config.RESULTADOS_DIR.resolve(Config.ARQUIVO_RESULTADOS_JAVA));
                System.out.println();
                
                // Exibe resumo dos resultados
                System.out.println("RESUMO DOS RESULTADOS:");
                System.out.printf("%12s | %18s | %20s%n", 
                    "Tamanho (n)", "Tempo Médio (ms)", "Desvio Padrão (ms)");
                System.out.println("-".repeat(70));
                
                for (CSVWriter.Resultado resultado : resultados) {
                    System.out.printf("%,12d | %18.6f | %20.6f%n",
                        resultado.getTamanho(),
                        resultado.getTempoMs(),
                        resultado.getDesvio());
                }
            } catch (IOException e) {
                System.err.println("Erro ao salvar resultados: " + e.getMessage());
            }
        } else {
            System.out.println();
            System.out.println("AVISO: Nenhum resultado foi gerado.");
            System.out.println("Verifique se os arquivos de dados estão no formato correto.");
        }
        
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("Experimento concluído!");
        System.out.println("=".repeat(70));
    }
}
