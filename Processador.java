package com.trabalho_gb;

public class Processador {

    public static void geraInstrucao(int posicaoSolicitada) {

        //mapeamento de páginas e frames
        MMU.mapeamento(posicaoSolicitada);

    }
}