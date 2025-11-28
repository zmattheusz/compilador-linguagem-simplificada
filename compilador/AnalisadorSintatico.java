package compilador;

import java.util.List;

public class AnalisadorSintatico {
    private List<Simbolo> simbolos;
    private int pos;
    private Simbolo atual;
    
    public AnalisadorSintatico(List<Simbolo> simbolos) {
        this.simbolos = simbolos;
        this.pos = 0;
        if (simbolos.size() > 0) {
            this.atual = simbolos.get(0);
        }
    }
    
    public No parse() {
        No programa = new No(No.Tipo.PROGRAMA);
        
        while (!fimDosSimbolos()) {
            No decl = declaracao();
            if (decl != null) {
                programa.adicionarFilho(decl);
            }
        }
        
        return programa;
    }
    
    private No declaracao() {
        if (consumirSe(Simbolo.Tipo.VAR)) {
            Simbolo tipo = atual;
            if (!consumirSe(Simbolo.Tipo.INT) && !consumirSe(Simbolo.Tipo.FLOAT)) {
                throw new RuntimeException("Erro sintático: esperado 'int' ou 'float' na linha " + 
                    atual.getLinha());
            }
            
            Simbolo id = atual;
            if (!consumirSe(Simbolo.Tipo.IDENTIFIER)) {
                throw new RuntimeException("Erro sintático: esperado identificador na linha " + 
                    atual.getLinha());
            }
            
            consumir(Simbolo.Tipo.SEMICOLON, "esperado ';'");
            
            No no = new No(No.Tipo.DECLARACAO_VARIAVEL, tipo);
            no.setValor(id.getValor());
            return no;
        }
        
        return comando();
    }
    
    private No comando() {
        if (atual.getTipo() == Simbolo.Tipo.PRINT) {
            return cmdPrint();
        } else if (atual.getTipo() == Simbolo.Tipo.READ) {
            return cmdRead();
        } else if (atual.getTipo() == Simbolo.Tipo.IF) {
            return cmdIf();
        } else if (atual.getTipo() == Simbolo.Tipo.WHILE) {
            return cmdWhile();
        } else if (atual.getTipo() == Simbolo.Tipo.LEFT_BRACE) {
            return bloco();
        } else if (atual.getTipo() == Simbolo.Tipo.IDENTIFIER) {
            return atribuicao();
        }
        
        throw new RuntimeException("Erro sintático: comando inválido na linha " + 
            atual.getLinha());
    }
    
    private No atribuicao() {
        Simbolo id = atual;
        consumir(Simbolo.Tipo.IDENTIFIER, "esperado identificador");
        consumir(Simbolo.Tipo.ASSIGN, "esperado '='");
        
        No expr = expressao();
        consumir(Simbolo.Tipo.SEMICOLON, "esperado ';'");
        
        No no = new No(No.Tipo.ATRIBUICAO, id);
        no.adicionarFilho(expr);
        return no;
    }
    
    private No cmdPrint() {
        Simbolo print = atual;
        consumir(Simbolo.Tipo.PRINT, "esperado 'print'");
        
        No expr = expressao();
        consumir(Simbolo.Tipo.SEMICOLON, "esperado ';'");
        
        No no = new No(No.Tipo.PRINT, print);
        no.adicionarFilho(expr);
        return no;
    }
    
    private No cmdRead() {
        Simbolo read = atual;
        consumir(Simbolo.Tipo.READ, "esperado 'read'");
        
        Simbolo id = atual;
        consumir(Simbolo.Tipo.IDENTIFIER, "esperado identificador");
        consumir(Simbolo.Tipo.SEMICOLON, "esperado ';'");
        
        No no = new No(No.Tipo.READ, read);
        no.setValor(id.getValor());
        return no;
    }
    
    private No cmdIf() {
        Simbolo ifSimbolo = atual;
        consumir(Simbolo.Tipo.IF, "esperado 'if'");
        consumir(Simbolo.Tipo.LEFT_PAREN, "esperado '('");
        
        No cond = expressao();
        consumir(Simbolo.Tipo.RIGHT_PAREN, "esperado ')'");
        
        No blocoIf = bloco();
        
        No no = new No(No.Tipo.IF, ifSimbolo);
        no.adicionarFilho(cond);
        no.adicionarFilho(blocoIf);
        
        if (consumirSe(Simbolo.Tipo.ELSE)) {
            No blocoElse = bloco();
            no.adicionarFilho(blocoElse);
        }
        
        return no;
    }
    
    private No cmdWhile() {
        Simbolo whileSimbolo = atual;
        consumir(Simbolo.Tipo.WHILE, "esperado 'while'");
        consumir(Simbolo.Tipo.LEFT_PAREN, "esperado '('");
        
        No cond = expressao();
        consumir(Simbolo.Tipo.RIGHT_PAREN, "esperado ')'");
        
        No bloco = bloco();
        
        No no = new No(No.Tipo.WHILE, whileSimbolo);
        no.adicionarFilho(cond);
        no.adicionarFilho(bloco);
        return no;
    }
    
    private No bloco() {
        consumir(Simbolo.Tipo.LEFT_BRACE, "esperado '{'");
        
        No bloco = new No(No.Tipo.BLOCO);
        
        while (atual.getTipo() != Simbolo.Tipo.RIGHT_BRACE && 
               atual.getTipo() != Simbolo.Tipo.EOF) {
            No cmd = comando();
            if (cmd != null) {
                bloco.adicionarFilho(cmd);
            }
        }
        
        consumir(Simbolo.Tipo.RIGHT_BRACE, "esperado '}'");
        return bloco;
    }
    
    private No expressao() {
        No esq = termo();
        
        while (atual.getTipo() == Simbolo.Tipo.AND || 
               atual.getTipo() == Simbolo.Tipo.OR) {
            Simbolo op = atual;
            avancar();
            No dir = termo();
            
            No no = new No(No.Tipo.OPERACAO_BINARIA, op);
            no.adicionarFilho(esq);
            no.adicionarFilho(dir);
            esq = no;
        }
        
        return esq;
    }
    
    private No termo() {
        No esq = fator();
        
        while (atual.getTipo() == Simbolo.Tipo.EQUALS ||
               atual.getTipo() == Simbolo.Tipo.NOT_EQUALS ||
               atual.getTipo() == Simbolo.Tipo.LESS ||
               atual.getTipo() == Simbolo.Tipo.GREATER ||
               atual.getTipo() == Simbolo.Tipo.LESS_EQUAL ||
               atual.getTipo() == Simbolo.Tipo.GREATER_EQUAL) {
            Simbolo op = atual;
            avancar();
            No dir = fator();
            
            No no = new No(No.Tipo.OPERACAO_BINARIA, op);
            no.adicionarFilho(esq);
            no.adicionarFilho(dir);
            esq = no;
        }
        
        return esq;
    }
    
    private No fator() {
        No esq = unario();
        
        while (atual.getTipo() == Simbolo.Tipo.PLUS ||
               atual.getTipo() == Simbolo.Tipo.MINUS ||
               atual.getTipo() == Simbolo.Tipo.MULTIPLY ||
               atual.getTipo() == Simbolo.Tipo.DIVIDE) {
            Simbolo op = atual;
            avancar();
            No dir = unario();
            
            No no = new No(No.Tipo.OPERACAO_BINARIA, op);
            no.adicionarFilho(esq);
            no.adicionarFilho(dir);
            esq = no;
        }
        
        return esq;
    }
    
    private No unario() {
        if (atual.getTipo() == Simbolo.Tipo.NOT) {
            Simbolo op = atual;
            avancar();
            No expr = unario();
            
            No no = new No(No.Tipo.OPERACAO_UNARIA, op);
            no.adicionarFilho(expr);
            return no;
        }
        
        if (atual.getTipo() == Simbolo.Tipo.MINUS) {
            Simbolo op = atual;
            avancar();
            No expr = unario();
            
            No no = new No(No.Tipo.OPERACAO_UNARIA, op);
            no.adicionarFilho(expr);
            return no;
        }
        
        return primario();
    }
    
    private No primario() {
        if (atual.getTipo() == Simbolo.Tipo.LEFT_PAREN) {
            avancar();
            No expr = expressao();
            consumir(Simbolo.Tipo.RIGHT_PAREN, "esperado ')'");
            return expr;
        }
        
        if (atual.getTipo() == Simbolo.Tipo.IDENTIFIER) {
            Simbolo id = atual;
            avancar();
            return new No(No.Tipo.IDENTIFICADOR, id);
        }
        
        if (atual.getTipo() == Simbolo.Tipo.INTEGER) {
            Simbolo simbolo = atual;
            avancar();
            No no = new No(No.Tipo.LITERAL_INT);
            no.setValor(Integer.parseInt(simbolo.getValor()));
            return no;
        }
        
        if (atual.getTipo() == Simbolo.Tipo.FLOAT_NUMBER) {
            Simbolo simbolo = atual;
            avancar();
            No no = new No(No.Tipo.LITERAL_FLOAT);
            no.setValor(Float.parseFloat(simbolo.getValor()));
            return no;
        }
        
        if (atual.getTipo() == Simbolo.Tipo.STRING) {
            Simbolo simbolo = atual;
            avancar();
            No no = new No(No.Tipo.LITERAL_STRING);
            no.setValor(simbolo.getValor());
            return no;
        }
        
        throw new RuntimeException("Erro sintático: esperado expressão na linha " + 
            atual.getLinha());
    }
    
    private boolean consumirSe(Simbolo.Tipo tipo) {
        if (atual.getTipo() == tipo) {
            avancar();
            return true;
        }
        return false;
    }
    
    private void consumir(Simbolo.Tipo tipo, String msg) {
        if (atual.getTipo() != tipo) {
            throw new RuntimeException("Erro sintático: " + msg + " na linha " + 
                atual.getLinha());
        }
        avancar();
    }
    
    private void avancar() {
        pos++;
        if (pos < simbolos.size()) {
            atual = simbolos.get(pos);
        }
    }
    
    private boolean fimDosSimbolos() {
        return pos >= simbolos.size() || atual.getTipo() == Simbolo.Tipo.EOF;
    }
}

