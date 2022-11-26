package com.trabalho_gb;


import java.util.Random;


public class SistemaOperacional implements Runnable {

    private static int tamanhoVirtual;

    public SistemaOperacional(int tamanhoDaMemoriaVirtual, int tamanhoDaMemoriaFisica) {
        System.out.println("------------------------------------------------------------------");
        tamanhoVirtual = tamanhoDaMemoriaVirtual;
        new MMU(tamanhoDaMemoriaVirtual, tamanhoDaMemoriaFisica);
        criaPaginas(tamanhoDaMemoriaVirtual);

    }

    public static void inicializa() {

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sortei de um inteiro de que representa a posição da memória virtual que será solicitado o acesso
        Random random = new Random();
        int posicaoSolicitada = random.nextInt(tamanhoVirtual);
        Processador.geraInstrucao(posicaoSolicitada);

        System.out.println("------------------------------------------------------------------");


    }


    private static void criaPaginas(double tamanhoDaMemoriaVirtual) {

        for (int i = 0; i < tamanhoDaMemoriaVirtual; i++) {

            Pagina pagina = new Pagina();
            MMU.memoriaVirtual[i] = pagina;
        }
    }

    @Override
    public synchronized void run() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            inicializa();
        }
    }

}