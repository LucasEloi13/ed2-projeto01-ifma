package utils;

import java.util.List;

/**
 * Utilitários para cálculos estatísticos.
 */
public class StatUtils {
    
    /**
     * Calcula a média de uma lista de valores.
     * 
     * @param valores Lista de valores
     * @return Média dos valores
     */
    public static double calcularMedia(List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return 0.0;
        }
        
        double soma = 0.0;
        for (double valor : valores) {
            soma += valor;
        }
        
        return soma / valores.size();
    }
    
    /**
     * Calcula o desvio padrão de uma lista de valores.
     * 
     * @param valores Lista de valores
     * @return Desvio padrão dos valores
     */
    public static double calcularDesvioPadrao(List<Double> valores) {
        if (valores == null || valores.size() <= 1) {
            return 0.0;
        }
        
        double media = calcularMedia(valores);
        double somaQuadrados = 0.0;
        
        for (double valor : valores) {
            double diferenca = valor - media;
            somaQuadrados += diferenca * diferenca;
        }
        
        return Math.sqrt(somaQuadrados / (valores.size() - 1));
    }
    
    /**
     * Representa o resultado de cálculos estatísticos.
     */
    public static class Estatisticas {
        private final double media;
        private final double desvioPadrao;
        
        public Estatisticas(double media, double desvioPadrao) {
            this.media = media;
            this.desvioPadrao = desvioPadrao;
        }
        
        public double getMedia() {
            return media;
        }
        
        public double getDesvioPadrao() {
            return desvioPadrao;
        }
    }
    
    /**
     * Calcula média e desvio padrão de uma lista de valores.
     * 
     * @param valores Lista de valores
     * @return Objeto Estatisticas com média e desvio padrão
     */
    public static Estatisticas calcularEstatisticas(List<Double> valores) {
        double media = calcularMedia(valores);
        double desvio = calcularDesvioPadrao(valores);
        return new Estatisticas(media, desvio);
    }
}
