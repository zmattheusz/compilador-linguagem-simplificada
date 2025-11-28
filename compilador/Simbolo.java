package compilador;

public class Simbolo {
    public enum Tipo {
        VAR, INT, FLOAT, IF, ELSE, WHILE, PRINT, READ,
        PLUS, MINUS, MULTIPLY, DIVIDE,
        EQUALS, NOT_EQUALS, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        AND, OR, NOT,
        ASSIGN,
        SEMICOLON, COMMA, LEFT_PAREN, RIGHT_PAREN,
        LEFT_BRACE, RIGHT_BRACE,
        INTEGER, FLOAT_NUMBER, IDENTIFIER, STRING,
        EOF, NEWLINE
    }
    
    private Tipo tipo;
    private String valor;
    private int linha;
    private int coluna;
    
    public Simbolo(Tipo tipo, String valor, int linha, int coluna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public Tipo getTipo() {
        return tipo;
    }
    
    public String getValor() {
        return valor;
    }
    
    public int getLinha() {
        return linha;
    }
    
    public int getColuna() {
        return coluna;
    }
    
    @Override
    public String toString() {
        return String.format("Simbolo(%s, '%s', linha: %d, coluna: %d)", 
            tipo, valor, linha, coluna);
    }
}

