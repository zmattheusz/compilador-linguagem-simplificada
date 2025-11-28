# Mini Compilador - Linguagem Simplificada

 Repositório GitHub

**Link do Código-Fonte Completo**: [https://github.com/zmattheusz/compilador-linguagem-simplificada](https://github.com/zmattheusz/compilador-linguagem-simplificada)

---

## Descrição
Este projeto implementa um mini compilador completo para uma linguagem simplificada inspirada em Python, desenvolvido em Java seguindo princípios de Orientação a Objetos.

## Estrutura do Projeto

### Componentes do Compilador

1. **Análise Léxica (Lexer)**: `Lexer.java`
Identifica e classifica tokens (palavras-chave, identificadores, números, operadores, etc.)

2. **Análise Sintática (Parser)**: `Parser.java`
Verifica a estrutura gramatical do código
Constrói uma árvore sintática abstrata (AST)

3. **Análise Semântica (SemanticAnalyzer.java)**
Verifica tipos de variáveis
Valida declarações e uso de variáveis
Detecta erros semânticos

4. **Geração de Código (CodeGenerator.java)**
Gera código executável (pseudo-código ou interpretação)

5. **Estruturas de Dados**
`Token.java`: Representa tokens do código-fonte
`ASTNode.java`: Nós da árvore sintática
`SymbolTable.java`: Tabela de símbolos para variáveis

## Especificação da Linguagem

### Tipos de Dados
`int`: Números inteiros
`float`: Números reais

### Palavras-chave
`var`: Declaração de variável
`int`: Tipo inteiro
`float`: Tipo real
`if`: Estrutura condicional
`else`: Alternativa condicional
`while`: Estrutura de repetição
`print`: Comando de saída
`read`: Comando de entrada

### Operadores Aritméticos
- `+`, `-`, `*`, `/`

### Operadores Lógicos
- `==`, `!=`, `<`, `>`, `<=`, `>=`
- `&&`, `||`, `!`

### Exemplo de Programa

```
var int x;
var float y;
x = 10;
y = 5.5;
print x;
if (x > 5) {
    print y;
}
while (x > 0) {
    print x;
    x = x - 1;
}
```

## Testes

Os arquivos de teste estão na pasta `testes/` com exemplos de programas.




