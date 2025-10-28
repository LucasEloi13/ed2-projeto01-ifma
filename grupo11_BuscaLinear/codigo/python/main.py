"""
Programa principal para execução dos experimentos de busca linear.
"""
from pathlib import Path
from busca_linear import buscaLinear
from experimento import ExperimentoBusca
from utils import salvar_resultados_csv
from config import (
    DADOS_DIR,
    RESULTADOS_DIR,
    TAMANHOS_VETOR,
    NUM_EXECUCOES,
    ARQUIVO_RESULTADOS_PYTHON
)


def main():
    """
    Função principal que coordena a execução dos experimentos.
    """
    print("=" * 70)
    print("EXPERIMENTO: Análise de Performance da Busca Linear")
    print("=" * 70)
    print()
    
    # Inicializa o experimento
    experimento = ExperimentoBusca(
        funcao_busca=buscaLinear,
        diretorio_dados=DADOS_DIR
    )
    
    # Verifica disponibilidade dos dados
    print("Verificando disponibilidade dos dados...")
    info_dados = experimento.verificar_disponibilidade_dados(TAMANHOS_VETOR)
    
    print("\nResumo dos dados disponíveis:")
    for tamanho, info in info_dados.items():
        print(f"  n={tamanho:6d}: {info['total_arquivos']} arquivos encontrados")
    print()
    
    # Verifica se há dados suficientes
    total_arquivos = sum(info['total_arquivos'] for info in info_dados.values())
    if total_arquivos == 0:
        print("ERRO: Nenhum arquivo de dados encontrado!")
        print(f"Certifique-se de que os dados estão em: {DADOS_DIR}")
        return
    
    # Executa os experimentos
    print(f"Iniciando experimentos com {NUM_EXECUCOES} execuções por tamanho...")
    print("-" * 70)
    
    resultados = experimento.executar_experimento_completo(
        tamanhos=TAMANHOS_VETOR,
        num_execucoes=NUM_EXECUCOES
    )
    
    # Salva os resultados
    if resultados:
        caminho_resultados = RESULTADOS_DIR / ARQUIVO_RESULTADOS_PYTHON
        salvar_resultados_csv(caminho_resultados, resultados)
        
        print()
        print("-" * 70)
        print(f"Resultados salvos em: {caminho_resultados}")
        print()
        
        # Exibe resumo dos resultados
        print("RESUMO DOS RESULTADOS:")
        print(f"{'Tamanho (n)':>12} | {'Tempo Médio (ms)':>18} | {'Desvio Padrão (ms)':>20}")
        print("-" * 70)
        for n, tempo, desvio in resultados:
            print(f"{n:>12,} | {tempo:>18.6f} | {desvio:>20.6f}")
    else:
        print()
        print("AVISO: Nenhum resultado foi gerado.")
        print("Verifique se os arquivos de dados estão no formato correto.")
    
    print()
    print("=" * 70)
    print("Experimento concluído!")
    print("=" * 70)


if __name__ == "__main__":
    main()
