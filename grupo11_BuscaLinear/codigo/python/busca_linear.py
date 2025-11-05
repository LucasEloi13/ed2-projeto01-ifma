def buscaLinear(lista_para_buscar, valor_procurado):
  for i in range(len(lista_para_buscar)):
    
    elemento_atual = lista_para_buscar[i]
    
    if elemento_atual == valor_procurado:
      return i  
  
  return None