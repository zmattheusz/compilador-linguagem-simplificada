package compilador;

import java.util.HashMap;
import java.util.Map;

public class GeradorCodigo {
    private Map<String, Object> vars;
    
    public GeradorCodigo() {
        this.vars = new HashMap<>();
    }
    
    public void executar(No no) {
        executarRec(no);
    }
    
    private void executarRec(No no) {
        switch (no.getTipo()) {
            case PROGRAMA:
                for (No filho : no.getFilhos()) {
                    executarRec(filho);
                }
                break;
                
            case DECLARACAO_VARIAVEL:
                String nomeVar = (String) no.getValor();
                TabelaSimbolos.Tipo tipoVar = no.getSimbolo().getTipo() == Simbolo.Tipo.INT ? 
                    TabelaSimbolos.Tipo.INT : TabelaSimbolos.Tipo.FLOAT;
                
                if (tipoVar == TabelaSimbolos.Tipo.INT) {
                    vars.put(nomeVar, 0);
                } else {
                    vars.put(nomeVar, 0.0f);
                }
                break;
                
            case ATRIBUICAO:
                String nomeAtrib = no.getSimbolo().getValor();
                Object valor = avaliar(no.getFilhos().get(0));
                vars.put(nomeAtrib, valor);
                break;
                
            case PRINT:
                Object valorPrint = avaliar(no.getFilhos().get(0));
                System.out.println(valorPrint);
                break;
                
            case READ:
                String nomeRead = (String) no.getValor();
                try {
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    if (vars.get(nomeRead) instanceof Float) {
                        float valorLido = scanner.nextFloat();
                        vars.put(nomeRead, valorLido);
                    } else {
                        int valorLido = scanner.nextInt();
                        vars.put(nomeRead, valorLido);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Erro na leitura: " + e.getMessage());
                }
                break;
                
            case IF:
                Object cond = avaliar(no.getFilhos().get(0));
                boolean condBool = paraBoolean(cond);
                
                if (condBool) {
                    executarRec(no.getFilhos().get(1));
                } else if (no.getFilhos().size() > 2) {
                    executarRec(no.getFilhos().get(2));
                }
                break;
                
            case WHILE:
                while (true) {
                    Object condWhile = avaliar(no.getFilhos().get(0));
                    boolean condBoolWhile = paraBoolean(condWhile);
                    
                    if (!condBoolWhile) {
                        break;
                    }
                    
                    executarRec(no.getFilhos().get(1));
                }
                break;
                
            case BLOCO:
                for (No filho : no.getFilhos()) {
                    executarRec(filho);
                }
                break;
        }
    }
    
    private Object avaliar(No no) {
        switch (no.getTipo()) {
            case LITERAL_INT:
                return no.getValor();
            case LITERAL_FLOAT:
                return no.getValor();
            case LITERAL_STRING:
                return no.getValor();
            case IDENTIFICADOR:
                String nome = no.getSimbolo().getValor();
                if (!vars.containsKey(nome)) {
                    throw new RuntimeException("Erro de execução: variável '" + nome + 
                        "' não inicializada");
                }
                return vars.get(nome);
                
            case OPERACAO_BINARIA:
                Simbolo.Tipo op = no.getSimbolo().getTipo();
                Object esq = avaliar(no.getFilhos().get(0));
                Object dir = avaliar(no.getFilhos().get(1));
                
                return executarOpBinaria(op, esq, dir);
                
            case OPERACAO_UNARIA:
                Simbolo.Tipo opUnario = no.getSimbolo().getTipo();
                Object operando = avaliar(no.getFilhos().get(0));
                
                return executarOpUnaria(opUnario, operando);
                
            default:
                throw new RuntimeException("Erro de execução: tipo de expressão inválido");
        }
    }
    
    private Object executarOpBinaria(Simbolo.Tipo op, Object esq, Object dir) {
        if (op == Simbolo.Tipo.PLUS) {
            if (esq instanceof Float || dir instanceof Float) {
                return paraFloat(esq) + paraFloat(dir);
            }
            return paraInt(esq) + paraInt(dir);
        }
        if (op == Simbolo.Tipo.MINUS) {
            if (esq instanceof Float || dir instanceof Float) {
                return paraFloat(esq) - paraFloat(dir);
            }
            return paraInt(esq) - paraInt(dir);
        }
        if (op == Simbolo.Tipo.MULTIPLY) {
            if (esq instanceof Float || dir instanceof Float) {
                return paraFloat(esq) * paraFloat(dir);
            }
            return paraInt(esq) * paraInt(dir);
        }
        if (op == Simbolo.Tipo.DIVIDE) {
            float resultado = paraFloat(esq) / paraFloat(dir);
            if (esq instanceof Integer && dir instanceof Integer) {
                return (int) resultado;
            }
            return resultado;
        }
        
        if (op == Simbolo.Tipo.EQUALS) {
            return paraFloat(esq) == paraFloat(dir) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.NOT_EQUALS) {
            return paraFloat(esq) != paraFloat(dir) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.LESS) {
            return paraFloat(esq) < paraFloat(dir) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.GREATER) {
            return paraFloat(esq) > paraFloat(dir) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.LESS_EQUAL) {
            return paraFloat(esq) <= paraFloat(dir) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.GREATER_EQUAL) {
            return paraFloat(esq) >= paraFloat(dir) ? 1 : 0;
        }
        
        if (op == Simbolo.Tipo.AND) {
            return (paraBoolean(esq) && paraBoolean(dir)) ? 1 : 0;
        }
        if (op == Simbolo.Tipo.OR) {
            return (paraBoolean(esq) || paraBoolean(dir)) ? 1 : 0;
        }
        
        throw new RuntimeException("Erro de execução: operador binário inválido");
    }
    
    private Object executarOpUnaria(Simbolo.Tipo op, Object operando) {
        if (op == Simbolo.Tipo.NOT) {
            return paraBoolean(operando) ? 0 : 1;
        }
        if (op == Simbolo.Tipo.MINUS) {
            if (operando instanceof Float) {
                return -paraFloat(operando);
            }
            return -paraInt(operando);
        }
        
        throw new RuntimeException("Erro de execução: operador unário inválido");
    }
    
    private boolean paraBoolean(Object valor) {
        if (valor instanceof Integer) {
            return (Integer) valor != 0;
        }
        if (valor instanceof Float) {
            return (Float) valor != 0.0f;
        }
        return valor != null;
    }
    
    private int paraInt(Object valor) {
        if (valor instanceof Integer) {
            return (Integer) valor;
        }
        if (valor instanceof Float) {
            return ((Float) valor).intValue();
        }
        throw new RuntimeException("Erro de execução: não é possível converter para int");
    }
    
    private float paraFloat(Object valor) {
        if (valor instanceof Float) {
            return (Float) valor;
        }
        if (valor instanceof Integer) {
            return ((Integer) valor).floatValue();
        }
        throw new RuntimeException("Erro de execução: não é possível converter para float");
    }
}

