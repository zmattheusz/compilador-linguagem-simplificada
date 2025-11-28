package compilador;

public class AnalisadorSemantico {
    private TabelaSimbolos tabela;
    
    public AnalisadorSemantico() {
        this.tabela = new TabelaSimbolos();
    }
    
    public void analisar(No no) {
        analisarRec(no);
    }
    
    private void analisarRec(No no) {
        switch (no.getTipo()) {
            case PROGRAMA:
                for (No filho : no.getFilhos()) {
                    analisarRec(filho);
                }
                break;
                
            case DECLARACAO_VARIAVEL:
                String nomeVar = (String) no.getValor();
                TabelaSimbolos.Tipo tipoVar = no.getSimbolo().getTipo() == Simbolo.Tipo.INT ? 
                    TabelaSimbolos.Tipo.INT : TabelaSimbolos.Tipo.FLOAT;
                tabela.declarar(nomeVar, tipoVar);
                break;
                
            case ATRIBUICAO:
                String nomeAtrib = no.getSimbolo().getValor();
                if (!tabela.existe(nomeAtrib)) {
                    throw new RuntimeException("Erro semântico: variável '" + nomeAtrib + 
                        "' não foi declarada na linha " + no.getSimbolo().getLinha());
                }
                TabelaSimbolos.Tipo tipoEsperado = tabela.getTipo(nomeAtrib);
                TabelaSimbolos.Tipo tipoExpr = verificarTipo(no.getFilhos().get(0));
                if (!compativel(tipoEsperado, tipoExpr)) {
                    throw new RuntimeException("Erro semântico: tipo incompatível na atribuição de '" + 
                        nomeAtrib + "' na linha " + no.getSimbolo().getLinha());
                }
                analisarRec(no.getFilhos().get(0));
                break;
                
            case IDENTIFICADOR:
                String nomeId = no.getSimbolo().getValor();
                if (!tabela.existe(nomeId)) {
                    throw new RuntimeException("Erro semântico: variável '" + nomeId + 
                        "' não foi declarada na linha " + no.getSimbolo().getLinha());
                }
                break;
                
            case READ:
                String nomeRead = (String) no.getValor();
                if (!tabela.existe(nomeRead)) {
                    throw new RuntimeException("Erro semântico: variável '" + nomeRead + 
                        "' não foi declarada na linha " + no.getSimbolo().getLinha());
                }
                break;
                
            case PRINT:
            case IF:
            case WHILE:
            case BLOCO:
                for (No filho : no.getFilhos()) {
                    analisarRec(filho);
                }
                break;
                
            case OPERACAO_BINARIA:
            case OPERACAO_UNARIA:
                for (No filho : no.getFilhos()) {
                    analisarRec(filho);
                }
                break;
        }
    }
    
    private TabelaSimbolos.Tipo verificarTipo(No no) {
        switch (no.getTipo()) {
            case LITERAL_INT:
                return TabelaSimbolos.Tipo.INT;
            case LITERAL_FLOAT:
                return TabelaSimbolos.Tipo.FLOAT;
            case LITERAL_STRING:
                return TabelaSimbolos.Tipo.STRING;
            case IDENTIFICADOR:
                return tabela.getTipo(no.getSimbolo().getValor());
            case OPERACAO_BINARIA:
                Simbolo.Tipo op = no.getSimbolo().getTipo();
                TabelaSimbolos.Tipo tipoEsq = verificarTipo(no.getFilhos().get(0));
                TabelaSimbolos.Tipo tipoDir = verificarTipo(no.getFilhos().get(1));
                
                if (op == Simbolo.Tipo.AND || op == Simbolo.Tipo.OR ||
                    op == Simbolo.Tipo.EQUALS || op == Simbolo.Tipo.NOT_EQUALS ||
                    op == Simbolo.Tipo.LESS || op == Simbolo.Tipo.GREATER ||
                    op == Simbolo.Tipo.LESS_EQUAL || op == Simbolo.Tipo.GREATER_EQUAL) {
                    return TabelaSimbolos.Tipo.INT;
                }
                
                if (op == Simbolo.Tipo.PLUS || op == Simbolo.Tipo.MINUS ||
                    op == Simbolo.Tipo.MULTIPLY || op == Simbolo.Tipo.DIVIDE) {
                    if (tipoEsq == TabelaSimbolos.Tipo.FLOAT || tipoDir == TabelaSimbolos.Tipo.FLOAT) {
                        return TabelaSimbolos.Tipo.FLOAT;
                    }
                    return TabelaSimbolos.Tipo.INT;
                }
                
                return tipoEsq;
            case OPERACAO_UNARIA:
                return verificarTipo(no.getFilhos().get(0));
            default:
                return TabelaSimbolos.Tipo.INT;
        }
    }
    
    private boolean compativel(TabelaSimbolos.Tipo tipo1, TabelaSimbolos.Tipo tipo2) {
        if (tipo1 == TabelaSimbolos.Tipo.FLOAT && tipo2 == TabelaSimbolos.Tipo.INT) {
            return true;
        }
        return tipo1 == tipo2;
    }
}

