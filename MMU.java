package com.trabalho_gb;



public class MMU {



    static Frame[] memoriaFisica;
    
    static Pagina[] memoriaVirtual;

    public static Integer[][] tabelaDePaginas;
    public final static int QUANTIDADE_COLUNAS_TABELA = 3;

    /** índices **/
    public final static int POSICAO_TABELA_PAGINAS = 0;
    public final static int PRESENTE = 1;
    public final static int MODIFICADA = 2;

    /** Algoritmo aging **/
    static int[][] bitsReferencia;

    private final static int QTD_BITS_REFERENCIA = 8;

    private static int posicaoSolicitada;

 

    public MMU(int tamanhoMemoriaVirtual, int tamanhoMemoriaFisica) {

        memoriaVirtual = new Pagina[tamanhoMemoriaVirtual];
        tabelaDePaginas = new Integer[tamanhoMemoriaVirtual][QUANTIDADE_COLUNAS_TABELA];

        memoriaFisica = new Frame[tamanhoMemoriaFisica];
        bitsReferencia = new int[tamanhoMemoriaFisica][QTD_BITS_REFERENCIA];



        for (int i = 0; i < tamanhoMemoriaVirtual; i++) {
            tabelaDePaginas[i][PRESENTE] = 0;
            tabelaDePaginas[i][MODIFICADA] = 0;
            tabelaDePaginas[i][POSICAO_TABELA_PAGINAS] = -1;

        }
    }

    public static void mapeamento(int posicao) {

        realocaBitsReferencia();
        posicaoSolicitada = posicao;

        System.out.println("Solicitação de acesso do processador à POSIÇÃO VIRTUAL: " + posicaoSolicitada);

        Pagina pagina = memoriaVirtual[posicaoSolicitada];

        if (tabelaDePaginas[posicaoSolicitada][PRESENTE] == 1) {

            System.out.println("Página presente na memória física na posição "
                    + tabelaDePaginas[posicaoSolicitada][POSICAO_TABELA_PAGINAS]);
            bitsReferencia[tabelaDePaginas[posicaoSolicitada][POSICAO_TABELA_PAGINAS]][0] = 1;

        } 
        else {
            // A página não está carregada na memória física e precisa ser
            System.out.println("\nPágina ausente na memória física.");
            carregaPaginaNaMemoriaFisica(pagina);

        }


        int posicaoMemoriaFisica = tabelaDePaginas[posicaoSolicitada][POSICAO_TABELA_PAGINAS];


        memoriaFisica[posicaoMemoriaFisica].setConteudoFrame(String.valueOf(posicaoSolicitada));
        tabelaDePaginas[posicaoSolicitada][MODIFICADA] = 1;

        System.out.println("\nProcessador acessado na POSIÇÃO FISICA: " + posicaoMemoriaFisica);

    }

    /**
     * Busca um frame disponível na memória física e salva a página no frame.
     **/
    private static void carregaPaginaNaMemoriaFisica(Pagina pagina) {

        int frameDisponivel = buscaFrameDisponivel();

        // Passa o conteúdo da página para o frame

        memoriaFisica[frameDisponivel] = new Frame();
        memoriaFisica[frameDisponivel].setConteudoFrame(pagina.getConteudoPagina());


        // Salva a informação da posição na tabela de páginas
        tabelaDePaginas[posicaoSolicitada][POSICAO_TABELA_PAGINAS] = frameDisponivel;
        // Coloca na tabela que a pagina está presente na memória física
        tabelaDePaginas[posicaoSolicitada][PRESENTE] = 1;

        System.out.println("Página carregada na posição " + frameDisponivel);

        // Atualiza a matriz de bits de referência, setando 1 no bit inicial
        bitsReferencia[frameDisponivel][0] = 1;

        // Imprime a matriz de bits após a realocação
        System.out.println("\nBits de referência:              CONTEÚDO");
        for (int i = 0; i < bitsReferencia.length; i++) {
            System.out.print("[Posição " + i + "] ");
            for (int j = 0; j < QTD_BITS_REFERENCIA; j++)
                System.out.print(bitsReferencia[i][j] + " ");
            if (memoriaFisica[i] != null && memoriaFisica[i].getConteudoFrame() == null){
                memoriaFisica[i].setConteudoFrame(String.valueOf(posicaoSolicitada));
                System.out.print("     ["+memoriaFisica[i].getConteudoFrame()+"]");
            } else if (memoriaFisica[i] != null && memoriaFisica[i].getConteudoFrame() != null)
                System.out.print("     ["+memoriaFisica[i].getConteudoFrame()+"]");

            System.out.println();
        }

    }

    /**
     * Realoca os bits de Referência de todas as páginas da memória fisica
     */
    private static void realocaBitsReferencia() {
        // Realoca os bits para a direita
        for (int i = 0; i < bitsReferencia.length; i++) {
            for (int j = QTD_BITS_REFERENCIA - 1; j > 0; j--)
                bitsReferencia[i][j] = bitsReferencia[i][j - 1];
            // Primeira posição é zerada
            bitsReferencia[i][0] = 0;
        }
    }

    private static int buscaFrameDisponivel() {
        System.out.println("Buscando frame disponível...");
        for (int i = 0; i < memoriaFisica.length; i++) {
            if (memoriaFisica[i] == null) {
                System.out.println("Frame " + i + " está disponível.");
                return i;
                // Se houver uma página disponível o método retorna o número desse frame,
                // Caso contrário um frame é liberado
            }
        }
        System.out.println("\nNão existem frames disponíveis. Será necessário liberar um frame.\n");
        return liberarFrame();

    }

    private static int liberarFrame() {
        System.out.println("Verificando o melhor frame para ser liberado...");
        // Criando um array com 8 valores de referência
        int[] valoresReferencia = new int[memoriaFisica.length];
        System.out.println("\nMatriz dos bits de referência:   CONTEÚDO");
        for (int i = 0; i < bitsReferencia.length; i++) {
            System.out.print("[Posição " + i + "] ");
            for (int j = 0; j < QTD_BITS_REFERENCIA; j++) {
                // Valor salvo no array valores corresponde a uma sequência de
                // binários, é multplicado o valor por 10^j para obter um número de
                // até 8 dígitos
                valoresReferencia[i] += bitsReferencia[i][j] * Math.pow(10, 7 - j);
                System.out.print(bitsReferencia[i][j] + " ");

            }
            System.out.print("     ["+memoriaFisica[i].getConteudoFrame()+"]");
            System.out.println();
        }

        // Busca a posição com o menor valor dentro no array valores para definir qual deve ser liberada
        int minimo = valoresReferencia[0];
        int posicaoLiberada = 0;
        for (int pos = 1; pos < valoresReferencia.length; pos++) {
            if (valoresReferencia[pos] < minimo) {
                minimo = valoresReferencia[pos];
                posicaoLiberada = pos;
            }
        }
        System.out.println("\nPosição " + posicaoLiberada + " possui o menor valor(bits) e será liberada");

        // Verificando a página virtual que foi removida da memória física
        int posicaoPaginaRemovida = -1;
        for (int i = 0; i < tabelaDePaginas.length; i++) {
            if (tabelaDePaginas[i][POSICAO_TABELA_PAGINAS] == posicaoLiberada) {
                posicaoPaginaRemovida = i;
            }
        }

        System.out.println("\nVerificando se a página foi modificada...");
        System.out.println("Bit de modificação = " + tabelaDePaginas[posicaoPaginaRemovida][MODIFICADA]);

        // Caso o bit sujo seja 1, salva a modificação em disco
        if (tabelaDePaginas[posicaoPaginaRemovida][MODIFICADA] == 1) {
            memoriaVirtual[posicaoPaginaRemovida].setConteudoPagina(String.valueOf(posicaoPaginaRemovida));

            // Volta o bit de modificação na tabela para 0
            tabelaDePaginas[posicaoPaginaRemovida][MODIFICADA] = 0;
        }

        memoriaFisica[posicaoLiberada] = null;

        // Zera o bit de presença na tabela
        tabelaDePaginas[posicaoPaginaRemovida][PRESENTE] = 0;
        tabelaDePaginas[posicaoPaginaRemovida][POSICAO_TABELA_PAGINAS] = -1;

        // Zera os bits de referência do frame que foi retirado
        for (int i = 0; i < QTD_BITS_REFERENCIA; i++)
            bitsReferencia[posicaoLiberada][i] = 0;

        System.out.println("O frame " + posicaoLiberada + " foi liberado");
        return posicaoLiberada;

    }



}