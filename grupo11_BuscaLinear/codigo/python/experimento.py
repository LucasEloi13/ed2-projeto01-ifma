"""
Módulo para execução e análise de experimentos de busca linear.
"""
import time
from typing import List, Callable, Tuple, Optional
from pathlib import Path
from utils import (
    ler_vetor_csv, 
    construir_caminho_dados, 
    calcular_estatisticas,
    listar_arquivos_dados
)


class ExperimentoBusca:
    """
    Classe responsável por executar e gerenciar experimentos de busca.
    """
    
    def __init__(self, funcao_busca: Callable, diretorio_dados: Path):
        """
        Inicializa o experimento.
        
        Args:
            funcao_busca: Função de busca a ser testada
            diretorio_dados: Diretório onde estão os dados de entrada
        """
        self.funcao_busca = funcao_busca
        self.diretorio_dados = diretorio_dados
    
    def medir_tempo_execucao(self, vetor: List[int], valor_busca: Optional[int] = None) -> float:
        """
        Mede o tempo de execução da busca em milissegundos.
        
        Args:
            vetor: Vetor onde será feita a busca
            valor_busca: Valor a ser buscado. Se None, busca o último elemento.
            
        Returns:
            Tempo de execução em milissegundos
        """
        if valor_busca is None:
            # Pior caso: buscar o último elemento
            valor_busca = vetor[-1] if vetor else -1
        
        inicio = time.perf_counter()
        self.funcao_busca(vetor, valor_busca)
        fim = time.perf_counter()
        
        # Retorna tempo em milissegundos
        tempo_ms = (fim - inicio) * 1000
        return tempo_ms
    
    def executar_para_tamanho(self, tamanho: int, num_execucoes: int = 50) -> List[float]:
        """
        Executa o experimento para um tamanho de vetor específico.
        
        Args:
            tamanho: Tamanho do vetor
            num_execucoes: Número de execuções independentes
            
        Returns:
            Lista com os tempos de cada execução em milissegundos
        """
        tempos = []
        
        for id_execucao in range(1, num_execucoes + 1):
            caminho_arquivo = construir_caminho_dados(
                tamanho, 
                id_execucao, 
                self.diretorio_dados
            )
            
            try:
                # Lê o vetor do arquivo
                vetor = ler_vetor_csv(caminho_arquivo)
                
                # Mede o tempo de execução
                tempo = self.medir_tempo_execucao(vetor)
                tempos.append(tempo)
                
            except FileNotFoundError:
                print(f"Aviso: Arquivo não encontrado - {caminho_arquivo}")
                continue
            except Exception as e:
                print(f"Erro ao processar {caminho_arquivo}: {str(e)}")
                continue
        
        return tempos
    
    def executar_experimento_completo(self, tamanhos: List[int], num_execucoes: int = 50) -> List[Tuple[int, float, float]]:
        """
        Executa o experimento completo para todos os tamanhos especificados.
        
        Args:
            tamanhos: Lista de tamanhos de vetor a serem testados
            num_execucoes: Número de execuções por tamanho
            
        Returns:
            Lista de tuplas (tamanho, tempo_médio_ms, desvio_padrão)
        """
        resultados = []
        
        for tamanho in tamanhos:
            print(f"Processando tamanho: n={tamanho}")
            
            tempos = self.executar_para_tamanho(tamanho, num_execucoes)
            
            if tempos:
                media, desvio = calcular_estatisticas(tempos)
                resultados.append((tamanho, media, desvio))
                print(f"  Média: {media:.6f} ms, Desvio: {desvio:.6f} ms")
            else:
                print(f"  Nenhum dado encontrado para n={tamanho}")
        
        return resultados
    
    def verificar_disponibilidade_dados(self, tamanhos: List[int]) -> dict:
        """
        Verifica quais arquivos de dados estão disponíveis.
        
        Args:
            tamanhos: Lista de tamanhos a verificar
            
        Returns:
            Dicionário com informações sobre disponibilidade
        """
        info = {}
        
        for tamanho in tamanhos:
            arquivos = listar_arquivos_dados(self.diretorio_dados, tamanho)
            info[tamanho] = {
                'total_arquivos': len(arquivos),
                'arquivos_encontrados': [arq.name for arq in arquivos[:5]]  # Primeiros 5
            }
        
        return info
