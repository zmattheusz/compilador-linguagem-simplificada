package compilador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Compilador {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java compilador.Compilador <arquivo>");
            System.out.println("Exemplo: java compilador.Compilador testes/teste1_basico.txt");
            return;
        }
        
        String arquivo = args[0];
        
        try {
            String codigo = lerArquivo(arquivo);
            
            System.out.println("=== Compilador - Linguagem Simplificada ===\n");
            System.out.println("Arquivo: " + arquivo);
            System.out.println("Código-fonte:\n" + codigo + "\n");
            
            System.out.println("--- FASE 1: Análise Léxica ---");
            AnalisadorLexico lexer = new AnalisadorLexico(codigo);
            List<Simbolo> simbolos = lexer.analisar();
            
            System.out.println("Tokens encontrados (" + simbolos.size() + "):");
            for (Simbolo simbolo : simbolos) {
                if (simbolo.getTipo() != Simbolo.Tipo.EOF) {
                    System.out.println("  " + simbolo);
                }
            }
            System.out.println();
            
            System.out.println("--- FASE 2: Análise Sintática ---");
            AnalisadorSintatico parser = new AnalisadorSintatico(simbolos);
            No ast = parser.parse();
            
            System.out.println("Árvore Sintática Abstrata (AST):");
            System.out.println(ast);
            
            System.out.println("--- FASE 3: Análise Semântica ---");
            AnalisadorSemantico semantico = new AnalisadorSemantico();
            semantico.analisar(ast);
            System.out.println("Análise semântica concluída com sucesso!\n");
            
            System.out.println("--- FASE 4: Execução do Programa ---");
            GeradorCodigo gerador = new GeradorCodigo();
            gerador.executar(ast);
            
            System.out.println("\n=== Programa executado com sucesso! ===");
            
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String lerArquivo(String arquivo) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivo)));
    }
}

