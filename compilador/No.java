package compilador;

import java.util.ArrayList;
import java.util.List;

public class No {
    public enum Tipo {
        PROGRAMA, DECLARACAO_VARIAVEL,
        ATRIBUICAO, PRINT, READ,
        IF, WHILE, BLOCO,
        EXPRESSAO_ARITMETICA, EXPRESSAO_LOGICA, EXPRESSAO_COMPARACAO,
        IDENTIFICADOR, LITERAL_INT, LITERAL_FLOAT, LITERAL_STRING,
        OPERACAO_BINARIA, OPERACAO_UNARIA
    }
    
    private Tipo tipo;
    private Simbolo simbolo;
    private List<No> filhos;
    private Object valor;
    
    public No(Tipo tipo) {
        this.tipo = tipo;
        this.filhos = new ArrayList<>();
    }
    
    public No(Tipo tipo, Simbolo simbolo) {
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.filhos = new ArrayList<>();
    }
    
    public No(Tipo tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.filhos = new ArrayList<>();
    }
    
    public void adicionarFilho(No filho) {
        filhos.add(filho);
    }
    
    public Tipo getTipo() {
        return tipo;
    }
    
    public Simbolo getSimbolo() {
        return simbolo;
    }
    
    public List<No> getFilhos() {
        return filhos;
    }
    
    public Object getValor() {
        return valor;
    }
    
    public void setValor(Object valor) {
        this.valor = valor;
    }
    
    @Override
    public String toString() {
        return toStringRecursivo(0);
    }
    
    private String toStringRecursivo(int nivel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            sb.append("  ");
        }
        sb.append(tipo);
        if (simbolo != null) {
            sb.append(" (").append(simbolo.getValor()).append(")");
        }
        if (valor != null) {
            sb.append(" = ").append(valor);
        }
        sb.append("\n");
        
        for (No filho : filhos) {
            sb.append(filho.toStringRecursivo(nivel + 1));
        }
        
        return sb.toString();
    }
}

