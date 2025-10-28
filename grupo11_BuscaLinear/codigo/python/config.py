"""
Configurações do projeto de análise de algoritmos de busca.
"""
import os
from pathlib import Path

# Diretórios base
BASE_DIR = Path(__file__).resolve().parent.parent.parent
DADOS_DIR = BASE_DIR / "dados"
RESULTADOS_DIR = BASE_DIR / "resultados" / "brutos" / "estatisticas"

# Configurações de execução
TAMANHOS_VETOR = [10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000]
NUM_EXECUCOES = 50

# Formato dos arquivos
FORMATO_DIRETORIO_DADOS = "n{:06d}"  # Ex: n050000
FORMATO_ARQUIVO_RUN = "run_{:03d}.csv"  # Ex: run_023.csv

# Arquivo de resultados
ARQUIVO_RESULTADOS_PYTHON = "resultados_Python.csv"
