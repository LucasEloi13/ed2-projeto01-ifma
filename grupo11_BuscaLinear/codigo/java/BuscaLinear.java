// Implementação do algoritmo de Busca Linear.
public class BuscaLinear {
    public static int buscar(int[] vetor, int valorProcurado) {
        for (int i = 0; i < vetor.length; i++) {
            if (vetor[i] == valorProcurado) {
                return i;
            }
        }
        return -1;
    }
}
