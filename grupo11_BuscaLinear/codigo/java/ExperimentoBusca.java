import utils.FileUtils;
import utils.StatUtils;
import utils.CSVWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por executar e gerenciar experimentos de busca.
 */
public class ExperimentoBusca {
    
    private final Path diretorioDados;
    

    // Inicializa o experimento.
    public ExperimentoBusca(Path diretorioDados) {
        this.diretorioDados = diretorioDados;
    }
    
    
    // Mede o tempo de execução da busca em milissegundos.
    public double medirTempoExecucao(int[] vetor, int valorBusca) {
        long inicio = System.nanoTime();
        BuscaLinear.buscar(vetor, valorBusca);
        long fim = System.nanoTime();

        return (fim - inicio) / 1_000_000.0;
    }
    

    // Executa o experimento para um tamanho de vetor específico.
    public List<Double> executarParaTamanho(int tamanho, int numExecucoes) {
        List<Double> tempos = new ArrayList<>();
        
        for (int idExecucao = 1; idExecucao <= numExecucoes; idExecucao++) {
            Path caminhoArquivo = FileUtils.construirCaminhoDados(
                tamanho, 
                idExecucao, 
                diretorioDados
            );
            
            try {
                int[] vetor = FileUtils.lerVetorCSV(caminhoArquivo);
                
                // Busca o último elemento (pior caso)
                int valorBusca = vetor[vetor.length - 1];
            
                double tempo = medirTempoExecucao(vetor, valorBusca);
                tempos.add(tempo);
                
            } catch (IOException e) {
                System.err.println("Aviso: Arquivo não encontrado - " + caminhoArquivo);
            } catch (Exception e) {
                System.err.println("Erro ao processar " + caminhoArquivo + ": " + e.getMessage());
            }
        }
        
        return tempos;
    }
    

    // Executa o experimento completo para todos os tamanhos especificados.
    public List<CSVWriter.Resultado> executarExperimentoCompleto(int[] tamanhos, int numExecucoes) {
        List<CSVWriter.Resultado> resultados = new ArrayList<>();
        
        for (int tamanho : tamanhos) {
            System.out.println("Processando tamanho: n=" + tamanho);
            
            List<Double> tempos = executarParaTamanho(tamanho, numExecucoes);
            
            if (!tempos.isEmpty()) {
                StatUtils.Estatisticas stats = StatUtils.calcularEstatisticas(tempos);
                resultados.add(new CSVWriter.Resultado(
                    tamanho, 
                    stats.getMedia(), 
                    stats.getDesvioPadrao()
                ));
                System.out.printf("  Média: %.6f ms, Desvio: %.6f ms%n", 
                    stats.getMedia(), stats.getDesvioPadrao());
            } else {
                System.out.println("  Nenhum dado encontrado para n=" + tamanho);
            }
        }
        
        return resultados;
    }
    
    
    // Verifica quais arquivos de dados estão disponíveis.
    public Map<Integer, Integer> verificarDisponibilidadeDados(int[] tamanhos) {
        Map<Integer, Integer> info = new HashMap<>();
        
        for (int tamanho : tamanhos) {
            try {
                List<Path> arquivos = FileUtils.listarArquivosDados(diretorioDados, tamanho);
                info.put(tamanho, arquivos.size());
            } catch (IOException e) {
                info.put(tamanho, 0);
            }
        }
        
        return info;
    }
}
