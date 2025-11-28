# Guia de Execução - Mini Compilador

## Pré-requisitos

Java JDK 8+ INSTALADO ```(OBRIGATÓRIO)```

## Compilação

1. Abra o prompt de comando
2. Navegue até a pasta do projeto:
```cmd
cd "caminho_da_pasta"
```

3. Execute o script de compilação:
```cmd
compile.bat
```

Ou compile manualmente:
```cmd
javac compilador/*.java

## Execução

Após compilar, execute o compilador com um arquivo de código-fonte:

```cmd
java compilador.Compilador testes\teste1_basico.txt
```


## Exemplos de Execução

### Exemplo 1: Programa Básico
```cmd
java compilador.Compilador testes\teste1_basico.txt
```

**Saída esperada**:
```
=== Compilador - Linguagem Simplificada ===

Arquivo: testes\teste1_basico.txt
Código-fonte:
var int x;
var float y;
x = 10;
y = 5.5;
print x;
print y;

--- FASE 1: Análise Léxica ---
Tokens encontrados (13):
  Simbolo(VAR, 'var', linha: 1, coluna: 1)
  Simbolo(INT, 'int', linha: 1, coluna: 5)
  ...

--- FASE 2: Análise Sintática ---
Árvore Sintática Abstrata (AST):
PROGRAMA
  DECLARACAO_VARIAVEL (x)
  ...

--- FASE 3: Análise Semântica ---
Análise semântica concluída com sucesso!

--- FASE 4: Execução do Programa ---
10
5.5

=== Programa executado com sucesso! ===
```

### Exemplo 2: Programa com Laço
```cmd
java compilador.Compilador testes\teste3_repeticao.txt
```

### Exemplo 3: Programa Completo
```cmd
java compilador.Compilador testes\teste6_completo.txt
```

## Estrutura de Saída

O compilador exibe informações sobre cada fase:

1. **Análise Léxica**: Lista todos os tokens encontrados
2. **Análise Sintática**: Mostra a AST construída
3. **Análise Semântica**: Confirmação de sucesso ou mensagem de erro
4. **Execução**: Saída do programa executado







