package compilador;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    private String codigo;
    private int pos;
    private int linha;
    private int col;
    private char atual;
    
    public AnalisadorLexico(String codigo) {
        this.codigo = codigo;
        this.pos = 0;
        this.linha = 1;
        this.col = 1;
        if (codigo.length() > 0) {
            this.atual = codigo.charAt(0);
        }
    }
    
    public List<Simbolo> analisar() {
        List<Simbolo> simbolos = new ArrayList<>();
        Simbolo simbolo;
        
        while (pos < codigo.length()) {
            simbolo = proximoSimbolo();
            if (simbolo != null) {
                simbolos.add(simbolo);
            }
        }
        
        simbolos.add(new Simbolo(Simbolo.Tipo.EOF, "", linha, col));
        return simbolos;
    }
    
    private Simbolo proximoSimbolo() {
        while (pos < codigo.length() && Character.isWhitespace(atual)) {
            if (atual == '\n') {
                linha++;
                col = 1;
            } else {
                col++;
            }
            avancar();
        }
        
        if (pos >= codigo.length()) {
            return null;
        }
        
        int linhaIni = linha;
        int colIni = col;
        
        if (Character.isDigit(atual)) {
            return lerNumero(linhaIni, colIni);
        }
        
        if (Character.isLetter(atual) || atual == '_') {
            return lerIdentificador(linhaIni, colIni);
        }
        
        if (atual == '"') {
            return lerString(linhaIni, colIni);
        }
        
        switch (atual) {
            case '+':
                avancar();
                return new Simbolo(Simbolo.Tipo.PLUS, "+", linhaIni, colIni);
            case '-':
                avancar();
                return new Simbolo(Simbolo.Tipo.MINUS, "-", linhaIni, colIni);
            case '*':
                avancar();
                return new Simbolo(Simbolo.Tipo.MULTIPLY, "*", linhaIni, colIni);
            case '/':
                avancar();
                return new Simbolo(Simbolo.Tipo.DIVIDE, "/", linhaIni, colIni);
            case '=':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '=') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.EQUALS, "==", linhaIni, colIni);
                }
                return new Simbolo(Simbolo.Tipo.ASSIGN, "=", linhaIni, colIni);
            case '!':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '=') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.NOT_EQUALS, "!=", linhaIni, colIni);
                }
                return new Simbolo(Simbolo.Tipo.NOT, "!", linhaIni, colIni);
            case '<':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '=') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.LESS_EQUAL, "<=", linhaIni, colIni);
                }
                return new Simbolo(Simbolo.Tipo.LESS, "<", linhaIni, colIni);
            case '>':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '=') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.GREATER_EQUAL, ">=", linhaIni, colIni);
                }
                return new Simbolo(Simbolo.Tipo.GREATER, ">", linhaIni, colIni);
            case '&':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '&') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.AND, "&&", linhaIni, colIni);
                }
                throw new RuntimeException("Erro léxico: '&' esperado '&&' na linha " + linhaIni);
            case '|':
                avancar();
                if (pos < codigo.length() && codigo.charAt(pos) == '|') {
                    avancar();
                    return new Simbolo(Simbolo.Tipo.OR, "||", linhaIni, colIni);
                }
                throw new RuntimeException("Erro léxico: '|' esperado '||' na linha " + linhaIni);
            case ';':
                avancar();
                return new Simbolo(Simbolo.Tipo.SEMICOLON, ";", linhaIni, colIni);
            case ',':
                avancar();
                return new Simbolo(Simbolo.Tipo.COMMA, ",", linhaIni, colIni);
            case '(':
                avancar();
                return new Simbolo(Simbolo.Tipo.LEFT_PAREN, "(", linhaIni, colIni);
            case ')':
                avancar();
                return new Simbolo(Simbolo.Tipo.RIGHT_PAREN, ")", linhaIni, colIni);
            case '{':
                avancar();
                return new Simbolo(Simbolo.Tipo.LEFT_BRACE, "{", linhaIni, colIni);
            case '}':
                avancar();
                return new Simbolo(Simbolo.Tipo.RIGHT_BRACE, "}", linhaIni, colIni);
            default:
                throw new RuntimeException("Erro léxico: caractere inesperado '" + atual + 
                    "' na linha " + linhaIni + ", coluna " + colIni);
        }
    }
    
    private Simbolo lerNumero(int linhaIni, int colIni) {
        StringBuilder sb = new StringBuilder();
        boolean temPonto = false;
        
        while (pos < codigo.length() && 
               (Character.isDigit(atual) || atual == '.')) {
            if (atual == '.') {
                if (temPonto) {
                    break;
                }
                temPonto = true;
            }
            sb.append(atual);
            avancar();
        }
        
        String valor = sb.toString();
        if (temPonto) {
            return new Simbolo(Simbolo.Tipo.FLOAT_NUMBER, valor, linhaIni, colIni);
        } else {
            return new Simbolo(Simbolo.Tipo.INTEGER, valor, linhaIni, colIni);
        }
    }
    
    private Simbolo lerIdentificador(int linhaIni, int colIni) {
        StringBuilder sb = new StringBuilder();
        
        while (pos < codigo.length() && 
               (Character.isLetterOrDigit(atual) || atual == '_')) {
            sb.append(atual);
            avancar();
        }
        
        String valor = sb.toString();
        Simbolo.Tipo tipo = palavraChave(valor);
        
        if (tipo != null) {
            return new Simbolo(tipo, valor, linhaIni, colIni);
        } else {
            return new Simbolo(Simbolo.Tipo.IDENTIFIER, valor, linhaIni, colIni);
        }
    }
    
    private Simbolo lerString(int linhaIni, int colIni) {
        StringBuilder sb = new StringBuilder();
        avancar();
        
        while (pos < codigo.length() && atual != '"') {
            if (atual == '\n') {
                throw new RuntimeException("Erro léxico: string não fechada na linha " + linhaIni);
            }
            sb.append(atual);
            avancar();
        }
        
        if (pos >= codigo.length()) {
            throw new RuntimeException("Erro léxico: string não fechada na linha " + linhaIni);
        }
        
        avancar();
        return new Simbolo(Simbolo.Tipo.STRING, sb.toString(), linhaIni, colIni);
    }
    
    private Simbolo.Tipo palavraChave(String palavra) {
        switch (palavra) {
            case "var": return Simbolo.Tipo.VAR;
            case "int": return Simbolo.Tipo.INT;
            case "float": return Simbolo.Tipo.FLOAT;
            case "if": return Simbolo.Tipo.IF;
            case "else": return Simbolo.Tipo.ELSE;
            case "while": return Simbolo.Tipo.WHILE;
            case "print": return Simbolo.Tipo.PRINT;
            case "read": return Simbolo.Tipo.READ;
            default: return null;
        }
    }
    
    private void avancar() {
        pos++;
        if (pos < codigo.length()) {
            atual = codigo.charAt(pos);
            col++;
        }
    }
}

