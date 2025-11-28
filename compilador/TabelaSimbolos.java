package compilador;

import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolos {
    public enum Tipo {
        INT, FLOAT, STRING
    }
    
    public static class Entrada {
        private String nome;
        private Tipo tipo;
        private boolean declarada;
        
        public Entrada(String nome, Tipo tipo) {
            this.nome = nome;
            this.tipo = tipo;
            this.declarada = true;
        }
        
        public String getNome() {
            return nome;
        }
        
        public Tipo getTipo() {
            return tipo;
        }
        
        public boolean isDeclarada() {
            return declarada;
        }
    }
    
    private Map<String, Entrada> simbolos;
    
    public TabelaSimbolos() {
        this.simbolos = new HashMap<>();
    }
    
    public void declarar(String nome, Tipo tipo) {
        if (simbolos.containsKey(nome)) {
            throw new RuntimeException("Erro semântico: variável '" + nome + "' já foi declarada");
        }
        simbolos.put(nome, new Entrada(nome, tipo));
    }
    
    public Entrada buscar(String nome) {
        return simbolos.get(nome);
    }
    
    public boolean existe(String nome) {
        return simbolos.containsKey(nome);
    }
    
    public Tipo getTipo(String nome) {
        Entrada entrada = simbolos.get(nome);
        if (entrada == null) {
            return null;
        }
        return entrada.getTipo();
    }
}

