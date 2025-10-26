"""
Funções utilitárias para leitura de dados e cálculos estatísticos.
"""
import csv
from pathlib import Path
from typing import List, Tuple
import statistics


def ler_vetor_csv(caminho_arquivo: Path) -> List[int]:
    """
    Lê um arquivo CSV contendo uma única linha de valores inteiros separados por vírgula.
    
    Args:
        caminho_arquivo: Caminho para o arquivo CSV
        
    Returns:
        Lista de inteiros lidos do arquivo
        
    Raises:
        FileNotFoundError: Se o arquivo não existir
        ValueError: Se o arquivo não puder ser lido corretamente
    """
    if not caminho_arquivo.exists():
        raise FileNotFoundError(f"Arquivo não encontrado: {caminho_arquivo}")
    
    try:
        with open(caminho_arquivo, 'r', encoding='utf-8') as arquivo:
            leitor = csv.reader(arquivo)
            linha = next(leitor)
            vetor = [int(valor.strip()) for valor in linha]
            return vetor
    except Exception as e:
        raise ValueError(f"Erro ao ler arquivo {caminho_arquivo}: {str(e)}")


def construir_caminho_dados(tamanho: int, id_execucao: int, diretorio_base: Path) -> Path:
    """
    Constrói o caminho completo para um arquivo de dados específico.
    
    Args:
        tamanho: Tamanho do vetor (ex: 50000)
        id_execucao: ID da execução (1 a 50)
        diretorio_base: Diretório base onde estão os dados
        
    Returns:
        Path completo para o arquivo
        
    Example:
        >>> construir_caminho_dados(50000, 23, Path("dados"))
        Path("dados/n050000/run_023.csv")
    """
    dir_tamanho = f"n{tamanho:06d}"
    arquivo_run = f"run_{id_execucao:03d}.csv"
    return diretorio_base / dir_tamanho / arquivo_run


def calcular_estatisticas(tempos: List[float]) -> Tuple[float, float]:
    """
    Calcula a média e o desvio padrão de uma lista de tempos.
    
    Args:
        tempos: Lista de tempos de execução em milissegundos
        
    Returns:
        Tupla contendo (média, desvio_padrão)
    """
    if not tempos:
        return 0.0, 0.0
    
    media = statistics.mean(tempos)
    desvio = statistics.stdev(tempos) if len(tempos) > 1 else 0.0
    
    return media, desvio


def salvar_resultados_csv(caminho_arquivo: Path, resultados: List[Tuple[int, float, float]]) -> None:
    """
    Salva os resultados em um arquivo CSV.
    
    Args:
        caminho_arquivo: Caminho para o arquivo de saída
        resultados: Lista de tuplas (n, tempo_ms, desvio)
    """
    # Cria o diretório se não existir
    caminho_arquivo.parent.mkdir(parents=True, exist_ok=True)
    
    with open(caminho_arquivo, 'w', newline='', encoding='utf-8') as arquivo:
        escritor = csv.writer(arquivo)
        # Cabeçalho
        escritor.writerow(['n', 'tempo_ms', 'desvio'])
        # Dados
        for n, tempo_ms, desvio in resultados:
            escritor.writerow([n, f'{tempo_ms:.6f}', f'{desvio:.6f}'])


def listar_arquivos_dados(diretorio_base: Path, tamanho: int) -> List[Path]:
    """
    Lista todos os arquivos de dados para um determinado tamanho de vetor.
    
    Args:
        diretorio_base: Diretório base onde estão os dados
        tamanho: Tamanho do vetor
        
    Returns:
        Lista de caminhos para os arquivos de dados
    """
    dir_tamanho = diretorio_base / f"n{tamanho:06d}"
    
    if not dir_tamanho.exists():
        return []
    
    arquivos = sorted(dir_tamanho.glob("run_*.csv"))
    return arquivos
