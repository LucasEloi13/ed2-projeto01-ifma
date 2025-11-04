package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Utilitários para manipulação de arquivos.
 */
public class FileUtils {
    
    /**
     * Lê um arquivo CSV contendo uma única linha de valores inteiros separados por vírgula.
     * 
     * @param caminhoArquivo Caminho para o arquivo CSV
     * @return Array de inteiros lidos do arquivo
     * @throws IOException Se houver erro na leitura do arquivo
     */
    public static int[] lerVetorCSV(Path caminhoArquivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo.toFile()))) {
            String linha = reader.readLine();
            if (linha == null || linha.trim().isEmpty()) {
                throw new IOException("Arquivo vazio: " + caminhoArquivo);
            }
            
            String[] valores = linha.split(",");
            int[] vetor = new int[valores.length];
            
            for (int i = 0; i < valores.length; i++) {
                vetor[i] = Integer.parseInt(valores[i].trim());
            }
            
            return vetor;
        }
    }
    
    /**
     * Constrói o caminho completo para um arquivo de dados específico.
     * 
     * @param tamanho Tamanho do vetor
     * @param idExecucao ID da execução (1-50)
     * @param diretorioBase Diretório base onde estão os dados
     * @return Path completo para o arquivo
     */
    public static Path construirCaminhoDados(int tamanho, int idExecucao, Path diretorioBase) {
        String dirTamanho = String.format("n%06d", tamanho);
        String arquivoRun = String.format("run_%03d.csv", idExecucao);
        return diretorioBase.resolve(dirTamanho).resolve(arquivoRun);
    }
    
    /**
     * Lista todos os arquivos de dados para um determinado tamanho de vetor.
     * 
     * @param diretorioBase Diretório base onde estão os dados
     * @param tamanho Tamanho do vetor
     * @return Lista de caminhos para os arquivos de dados
     * @throws IOException Se houver erro ao listar os arquivos
     */
    public static List<Path> listarArquivosDados(Path diretorioBase, int tamanho) throws IOException {
        Path dirTamanho = diretorioBase.resolve(String.format("n%06d", tamanho));
        List<Path> arquivos = new ArrayList<>();
        
        if (!Files.exists(dirTamanho)) {
            return arquivos;
        }
        
        try (Stream<Path> paths = Files.list(dirTamanho)) {
            paths.filter(path -> path.getFileName().toString().startsWith("run_") 
                              && path.getFileName().toString().endsWith(".csv"))
                 .sorted()
                 .forEach(arquivos::add);
        }
        
        return arquivos;
    }
}
