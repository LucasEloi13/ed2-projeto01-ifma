#!/bin/bash

echo "Iniciando a geração de dados..."

# Loop pelos tamanhos de N (10k, 20k, ..., 100k) 
for n in $(seq 10000 10000 100000); do
    echo "Gerando dados para N = $n"
    
    # Loop pelas 50 execuções (run_id de 1 a 50) 
    for id in $(seq 1 50); do
        # Chama o programa C compilado
        ./gerador $n $id
    done
done

echo "Geração de dados concluída."