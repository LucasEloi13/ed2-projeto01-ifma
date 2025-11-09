"""
Funções utilitárias para leitura de dados e cálculos estatísticos.
"""
import csv
from pathlib import Path
from typing import List, Tuple
import statistics


def ler_vetor_csv(caminho_arquivo: Path) -> List[int]:
    try:
        with open(caminho_arquivo, 'r', encoding='utf-8') as arquivo:
            leitor = csv.reader(arquivo)
            linha = next(leitor)
            vetor = [int(valor.strip()) for valor in linha]
            return vetor
    except FileNotFoundError as e:
        raise FileNotFoundError(f"Arquivo não encontrado: {caminho_arquivo}") from e
    except StopIteration as e:
        raise ValueError(f"O arquivo {caminho_arquivo} está vazio ou mal formatado.") from e
    except ValueError as e:
        raise ValueError(f"Erro ao converter valores para inteiro no arquivo {caminho_arquivo}: {e}") from e


def construir_caminho_dados(tamanho: int, id_execucao: int, diretorio_base: Path) -> Path:
    dir_tamanho = f"n{tamanho:06d}"
    arquivo_run = f"run_{id_execucao:03d}.csv"
    return diretorio_base / dir_tamanho / arquivo_run


def calcular_estatisticas(tempos: List[float]) -> Tuple[float, float]:
    if not tempos:
        return 0.0, 0.0
    media = statistics.mean(tempos)
    desvio = statistics.stdev(tempos) if len(tempos) > 1 else 0.0
    return media, desvio


def salvar_resultados_csv(caminho_arquivo: Path, resultados: List[Tuple[int, float, float]]) -> None:
    caminho_arquivo.parent.mkdir(parents=True, exist_ok=True)
    
    with open(caminho_arquivo, 'w', newline='', encoding='utf-8') as arquivo:
        escritor = csv.writer(arquivo)
        escritor.writerow(['n', 'tempo_ms', 'desvio'])
        for n, tempo_ms, desvio in resultados:
            escritor.writerow([n, f'{tempo_ms:.6f}', f'{desvio:.6f}'])


def listar_arquivos_dados(diretorio_base: Path, tamanho: int) -> List[Path]:
    dir_tamanho = diretorio_base / f"n{tamanho:06d}"
        
    arquivos = sorted(dir_tamanho.glob("run_*.csv"))
    return arquivos
