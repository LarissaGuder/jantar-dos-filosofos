package jantarfilosofos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 * @author Larissa e Elenara
 */
class RunnableThread implements Runnable {

    // Quantidade de filosofos (Só vai funcionar com 5, pois existem 5 threads no momento)
    public static int quantidade = 5;
    public static int[] talheres = new int[quantidade];
    // Semaforo para controlar o acesso aos talheres
    public static Semaphore semaphore = new Semaphore(1);
    private static StatusFilosofo statusFilosofo;

    public static void setStatusFilosofo(StatusFilosofo aStatusFilosofo) {
        statusFilosofo = aStatusFilosofo;
    }

    public static StatusFilosofo getStatusFilosofo() {
        return statusFilosofo;
    }

    @Override
    public void run() {
        while (true) {

            // Converte o nome do filosofo num inteiro, será o talher a direita do filosofo
            int filosofo = Integer.parseInt((Thread.currentThread().getName()));
            // Seta o status do filosofo como iniciado 
            // Para evitar problemas com a primeira execução.
            RunnableThread.setStatusFilosofo(StatusFilosofo.Iniciou);

//System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
            // O index será usado para indicar o talher a esquerda do filosofo
            // O método fila circular
            int index = JantarFilosofos.filaCircular((filosofo));
            // realiza toda a ação
            JantarFilosofos.acao(filosofo, index);
        }
    }

}

public class JantarFilosofos {

    static boolean[] jantaram = {false, false, false, false, false};

    public static void janta() {
        int cont = 0;
        for (int i = 0; i < jantaram.length; i++) {
            if (jantaram[i] == true) {
                cont = cont + 1;
            }
            if (cont == 5) {
                System.out.println("Todos jantaram"
                        + "");
                System.exit(0);
            }

        }
    }

    public static void acao(int talher, int index) {
        // O método janta verifica se todos já jantaram.
        janta();
        try {
            // O método podeComer vai verificar se os talheres estão sendo utilizados.
            if (podeComer(talher) == true) {

                // se retornou verdadeiro, o semaforo bloqueia o acesso
                RunnableThread.semaphore.acquire();
                int filosofo = Integer.parseInt((Thread.currentThread().getName()));
                // avisa no método janta, que o filosofo está jantando
                jantaram[filosofo] = true;
                // seta o status como Comendo
                RunnableThread.setStatusFilosofo(StatusFilosofo.Comendo);
                System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
                // Libera os talheres
                RunnableThread.talheres[talher] = 0;
                RunnableThread.talheres[index] = 0;
                //Pausa por 2 segundos
                Thread.sleep(2000);
                // O filosofo para de comer, e é alterado o status dele
                finalizou();
                // semaforo libera o acesso
                RunnableThread.semaphore.release();
                //System.out.println("Parou de comer " + Thread.currentThread().getName());
            } else {
                // Se o método podecomer retorna falso, o filosofo passa a pensar
                RunnableThread.setStatusFilosofo(StatusFilosofo.Pensando);
                System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
                Thread.sleep(1500);
            }

        } catch (InterruptedException iex) {
            System.out.println("Exception: " + iex.getMessage());
        }
    }

    public static void finalizou() throws InterruptedException {
        // Para evitar problemas com o status do filosofo
        // da uma pausa, e depois o filosofo volta a tentar comer
        RunnableThread.setStatusFilosofo(StatusFilosofo.Terminou);
        System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
        Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
    }

    // Evita estourar o array, visto que eles estão sentados em circulo, quando o
    // array chegar nos extremos, o valor do index é alterado, para o menor ou o maior valor.
    public static int filaCircular(int talher) {
        int index;
        if (talher == 0) {
            index = 4;

        } else {
            index = talher - 1;
        }

        return index;
    }

    public static boolean podeComer(int talher) throws InterruptedException {

        int index = filaCircular(talher);
        // 0 equivale a um talher que não está sendo usado
        if (RunnableThread.talheres[talher] == 0 && RunnableThread.talheres[index] == 0) {
            // 1 quando é utilizado
            RunnableThread.talheres[talher] = 1;
            RunnableThread.talheres[index] = 1;
            //System.out.println("");
            //System.out.println("talheres usados " + talher + " e " + index);
            // retorna verdadeiro, e o filosofo pode usar os talheres
            return true;

        }
        // retorna falso, e o filosofo fica finalizou
        return false;
    }

    public static void main(String[] args) {
// Inicia as threads
        for (int i = 0; i < RunnableThread.talheres.length; i++) {
            RunnableThread.talheres[i] = 0;
        }

        RunnableThread rt = new RunnableThread();

        Thread f1 = new Thread(rt);
        f1.setName("0");
        f1.start();

        Thread f2 = new Thread(rt);
        f2.setName("1");
        f2.start();

        Thread f3 = new Thread(rt);
        f3.setName("2");
        f3.start();

        Thread f4 = new Thread(rt);
        f4.setName("3");
        f4.start();

        Thread f5 = new Thread(rt);
        f5.setName("4");
        f5.start();
    }

}
// status possíveis para os filosofos

enum StatusFilosofo {

    Comendo,
    Pensando,
    Terminou,
    Iniciou,
}
