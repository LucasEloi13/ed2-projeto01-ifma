/**
 * Implementação do algoritmo de Busca Linear.
 */
public class BuscaLinear {
    
    /**
     * Realiza busca linear em um vetor.
     * 
     * @param vetor Vetor onde será feita a busca
     * @param valorProcurado Valor a ser encontrado
     * @return Índice do elemento encontrado, ou -1 se não encontrado
     */
    public static int buscar(int[] vetor, int valorProcurado) {
        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i] == valorProcurado) {
                return i;
            }
        }
        return -1;
    }
}
