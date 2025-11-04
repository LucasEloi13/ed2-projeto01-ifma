#!/bin/bash

# Script para compilar e executar o experimento de Busca Linear em Java

echo "Compilando código Java..."

# Cria diretório para classes compiladas
mkdir -p bin

# Compila todos os arquivos Java
javac -d bin \
    BuscaLinear.java \
    Config.java \
    ExperimentoBusca.java \
    Main.java \
    utils/FileUtils.java \
    utils/StatUtils.java \
    utils/CSVWriter.java

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
    echo ""
    echo "Executando experimento..."
    echo ""
    
    # Executa o programa
    java -cp bin Main
else
    echo "Erro na compilação!"
    exit 1
fi
