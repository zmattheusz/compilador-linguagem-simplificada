@echo off
chcp 65001 >nul
echo Compilando o mini compilador...
javac -encoding UTF-8 compilador\Simbolo.java compilador\No.java compilador\AnalisadorLexico.java compilador\AnalisadorSintatico.java compilador\TabelaSimbolos.java compilador\AnalisadorSemantico.java compilador\GeradorCodigo.java compilador\Compilador.java
if %errorlevel% == 0 (
    echo Compilacao concluida com sucesso!
    echo.
    echo Para executar um programa:
    echo   java compilador.Compilador testes\teste1_basico.txt
) else (
    echo Erro na compilacao!
    pause
)

