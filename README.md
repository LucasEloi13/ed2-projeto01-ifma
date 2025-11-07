# Busca Linear - Análise Experimental de Desempenho

## 📋 Sobre o Projeto

Este projeto implementa e analisa o desempenho do algoritmo **BETTER-LINEAR-SEARCH** em C, conforme especificado em trabalhos de Estruturas de Dados II. O objetivo é medir empiricamente o tempo de execução do algoritmo para diferentes tamanhos de entrada e validar a complexidade teórica O(n).

## 🎯 Objetivos

- Implementar o algoritmo de busca linear
- Medir o tempo de execução para vetores de tamanho variável (10.000 a 100.000 elementos)
- Executar múltiplas iterações (50 execuções) para obter médias estatisticamente significativas
- Gerar resultados em formato CSV para análise e visualização
- Calcular média e desvio padrão dos tempos de execução

## 🏗️ Arquitetura do Projeto

```
grupo11_BuscaLinear/
├── codigo/
│   └── c/
│       └── busca_linear.c          # Implementação principal
├── dados/
│   ├── vetor_10000.csv             # Vetor com 10.000 elementos
│   ├── vetor_20000.csv             # Vetor com 20.000 elementos
│   ├── ...
│   └── vetor_100000.csv            # Vetor com 100.000 elementos
└── resultados/
    └── resultados_C.csv            # Resultados gerados (tempo médio + desvio)
```

## 🔧 Componentes Principais

### 1. `linear_search(int A[], int n, int x)`
Implementação do algoritmo de busca linear:
- **Entrada**: Vetor A, tamanho n, chave x
- **Saída**: Índice onde x foi encontrado, ou -1 se não existe
- **Complexidade**: O(n)

```c
for (int i = 0; i < n; i++) {
    if (A[i] == x) {
        return i;
    }
}
return -1;
```

### 2. `gerar_chave_busca(int A[], int n)`
Gera chaves de busca com distribuição equilibrada:
- **50% de chance**: Chave existe no vetor (busca com sucesso)
- **50% de chance**: Chave não existe (busca com falha)

**Lógica da geração:**
```c
if (rand() % 2 == 0) {
    // SUCESSO: Seleciona índice aleatório do vetor
    int index_aleatorio = rand() % n;
    chave = A[index_aleatorio];
} else {
    // FALHA: Gera número maior que o máximo do vetor
    chave = max_val + (rand() % 1000) + 1;
}
```

### 3. `calcular_media()` e `calcular_desvio_padrao()`
Funções estatísticas para análise dos resultados:

**Média:**
```
média = (tempo₁ + tempo₂ + ... + tempo₅₀) / 50
```

**Desvio Padrão:**
```
σ = √[Σ(tempo - média)² / (n-1)]
```

## 🚀 Como Executar

### Pré-requisitos

- **GCC** (GNU Compiler Collection)
- Sistema Unix/Linux ou Windows com MinGW
- Arquivos CSV com os vetores de teste na pasta `dados/`

### Compilação

```bash
# Entre no diretório do código
cd grupo11_BuscaLinear/codigo/c/

# Compile o programa
gcc -o busca_linear busca_linear.c -lm

# O flag -lm é necessário para linkar a biblioteca matemática (sqrt)
```

### Execução

```bash
# Execute o programa
./busca_linear

# No Windows:
busca_linear.exe
```

### O que acontece durante a execução:

1. **Criação de diretórios**: O programa cria automaticamente a pasta `resultados/` se não existir

2. **Loop pelos tamanhos**: Para cada tamanho de vetor (10.000, 20.000, ..., 100.000):
   ```
   Processando vetor de tamanho 10000...
   Processando vetor de tamanho 20000...
   ...
   ```

3. **50 execuções por tamanho**: Para cada vetor:
   - Lê o vetor do arquivo CSV
   - Executa 50 buscas com chaves aleatórias
   - Mede o tempo de cada execução
   - Calcula média e desvio padrão

4. **Resultado salvo**: Arquivo `resultados/resultados_C.csv` é gerado

## 📊 Formato dos Resultados

O arquivo `resultados/resultados_C.csv` tem o formato:

```csv
n,tempo_ms,desvio
10000,0.152341,0.034512
20000,0.304562,0.068234
30000,0.456123,0.102456
40000,0.608234,0.136678
50000,0.760345,0.170890
60000,0.912456,0.205102
70000,1.064567,0.239314
80000,1.216678,0.273526
90000,1.368789,0.307738
100000,1.520900,0.341950
```

**Colunas:**
- `n`: Tamanho do vetor
- `tempo_ms`: Tempo médio de execução em milissegundos (média de 50 execuções)
- `desvio`: Desvio padrão dos tempos

## 🧪 Metodologia Experimental

### Por que 50 execuções?

Múltiplas execuções são necessárias para:
- **Reduzir ruído**: Sistema operacional, cache, outros processos
- **Capturar variabilidade**: Algumas buscas são rápidas (início do vetor), outras lentas (fim/não encontrado)
- **Obter média confiável**: Lei dos Grandes Números

### Por que 50/50 sucesso/falha?

A estratégia de gerar chaves com probabilidade equilibrada simula o **caso médio** real:
- **Busca com sucesso**: Pode parar no meio (melhor caso: índice 0, pior caso: último índice)
- **Busca com falha**: Sempre percorre todo o vetor (sempre O(n))

### Cronometragem Precisa

```c
// Gera chave ANTES de iniciar o cronômetro
int chave = gerar_chave_busca(A, n);

// INICIA cronômetro
clock_t inicio = clock();

// Executa busca
int resultado = linear_search(A, n, chave);

// PARA cronômetro
clock_t fim = clock();

// Calcula tempo em milissegundos
double tempo_ms = ((double)(fim - inicio) / CLOCKS_PER_SEC) * 1000.0;
```

## 📈 Análise dos Resultados

### Complexidade Esperada

O algoritmo de busca linear tem complexidade **O(n)**:
- Se o gráfico tempo × tamanho for aproximadamente **linear**, valida a teoria
- Desvio padrão indica a variabilidade das execuções

### Visualização (exemplo com Python/Excel)

Você pode plotar os resultados:

```python
import pandas as pd
import matplotlib.pyplot as plt

# Lê resultados
df = pd.read_csv('resultados/resultados_C.csv')

# Plota gráfico
plt.errorbar(df['n'], df['tempo_ms'], yerr=df['desvio'], 
             fmt='o-', capsize=5)
plt.xlabel('Tamanho do Vetor (n)')
plt.ylabel('Tempo Médio (ms)')
plt.title('Desempenho: Busca Linear')
plt.grid(True)
plt.show()
```

## 🔍 Detalhes Técnicos

### Geração de Índice Aleatório

```c
int index_aleatorio = rand() % n;
```

**Como funciona:**
- `rand()` gera número aleatório entre 0 e RAND_MAX
- `% n` pega o resto da divisão por n
- Resultado: número entre 0 e n-1 (índices válidos do vetor)

**Exemplo:**
```
Se n = 10000 e rand() retorna 47523:
index_aleatorio = 47523 % 10000 = 7523
chave = A[7523]  // Elemento no índice 7523
```

### Distribuição Uniforme

O operador `%` garante distribuição uniforme:
- Cada índice tem ~mesma probabilidade de ser escolhido
- Simula buscas realistas (não viesadas para início ou fim)

## 🐛 Troubleshooting

### Erro: "Não foi possível abrir o arquivo"
```
Erro: Não foi possível abrir o arquivo dados/vetor_10000.csv
```
**Solução**: Verifique se os arquivos CSV existem na pasta `dados/`

### Erro de compilação: "undefined reference to sqrt"
```
undefined reference to `sqrt'
```
**Solução**: Adicione o flag `-lm` na compilação:
```bash
gcc -o busca_linear busca_linear.c -lm
```

### Resultados inconsistentes
Se os tempos variam muito entre execuções:
- Feche outros programas pesados
- Execute múltiplas vezes e compare
- Verifique se o sistema não está em carga alta

## 👥 Grupo 11

Projeto desenvolvido para a disciplina de Estruturas de Dados II - IFMA

## 📝 Referências

- Cormen, T. H., et al. "Introduction to Algorithms" (Capítulo sobre busca linear)
- Material da disciplina ED2 - IFMA