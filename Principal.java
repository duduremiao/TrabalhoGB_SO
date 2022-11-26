package com.trabalho_gb;


import javafx.application.Application;
import javafx.stage.Stage;




public class Principal extends Application {



    @Override
    public void start(Stage stage) {

        int tamanhoPagina;
        int tamanhoMemoriaFisica;
        int tamanhoMemoriaVirtual;

        System.out.println("--------------------------------------------------");
        tamanhoPagina = 8;
        tamanhoMemoriaFisica = 64;
        tamanhoMemoriaVirtual = 1024;

        int numeroPosicoesFisica = tamanhoMemoriaFisica / tamanhoPagina;
        System.out.println("A memória física possui " + numeroPosicoesFisica + " posições");
        int numeroPosicoesVirtual = tamanhoMemoriaVirtual / tamanhoPagina;
        System.out.println("A memória lógica possui " + numeroPosicoesVirtual + " posições");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SistemaOperacional so = new SistemaOperacional(numeroPosicoesVirtual, numeroPosicoesFisica);
        Thread threadSO = new Thread(so);

        threadSO.start();

    }

}