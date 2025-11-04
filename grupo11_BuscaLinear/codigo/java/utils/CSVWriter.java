package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utilitário para escrita de arquivos CSV.
 */
public class CSVWriter {
    
    /**
     * Representa um resultado individual do experimento.
     */
    public static class Resultado {
        private final int tamanho;
        private final double tempoMs;
        private final double desvio;
        
        public Resultado(int tamanho, double tempoMs, double desvio) {
            this.tamanho = tamanho;
            this.tempoMs = tempoMs;
            this.desvio = desvio;
        }
        
        public int getTamanho() {
            return tamanho;
        }
        
        public double getTempoMs() {
            return tempoMs;
        }
        
        public double getDesvio() {
            return desvio;
        }
    }
    
    /**
     * Salva os resultados em um arquivo CSV.
     * 
     * @param caminhoArquivo Caminho para o arquivo de saída
     * @param resultados Lista de resultados
     * @throws IOException Se houver erro na escrita do arquivo
     */
    public static void salvarResultadosCSV(Path caminhoArquivo, List<Resultado> resultados) 
            throws IOException {
        // Cria o diretório se não existir
        Files.createDirectories(caminhoArquivo.getParent());
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo.toFile()))) {
            // Escreve o cabeçalho
            writer.write("n,tempo_ms,desvio");
            writer.newLine();
            
            // Escreve os dados
            for (Resultado resultado : resultados) {
                writer.write(String.format("%d,%.6f,%.6f",
                    resultado.getTamanho(),
                    resultado.getTempoMs(),
                    resultado.getDesvio()));
                writer.newLine();
            }
        }
    }
}
