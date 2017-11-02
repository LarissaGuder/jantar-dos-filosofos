package jantarfilosofos;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 * @author Larissa e Elenara
 */
class RunnableThread implements Runnable {

    public static boolean semaforo = true;
    public static int quantidade = 5;
    public static int[] talheres = new int[quantidade];
    public static boolean talherD;
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

            int filosofo = Integer.parseInt((Thread.currentThread().getName()));
            //System.out.println("");
            RunnableThread.setStatusFilosofo(StatusFilosofo.Iniciou);
            System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());

            int index = JantarFilosofos.filaCircular((filosofo));
            JantarFilosofos.acao(filosofo, index);

        }
    }

}

public class JantarFilosofos {

    public static void acao(int talher, int index) {
        try {
            if (podeComer(talher) == true) {
                //  System.out.println("");

                RunnableThread.semaphore.acquire();
                RunnableThread.setStatusFilosofo(StatusFilosofo.Comendo);
                System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
                RunnableThread.talheres[talher] = 0;
                RunnableThread.talheres[index] = 0;
                Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
                esperando();
                RunnableThread.semaphore.release();
                //System.out.println("Parou de comer " + Thread.currentThread().getName());
            } else {
                //System.out.println("");
                RunnableThread.setStatusFilosofo(StatusFilosofo.Pensando);
                System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
                Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
            }

        } catch (InterruptedException iex) {
            System.out.println("Exception: " + iex.getMessage());
        }
    }

    public static void esperando() throws InterruptedException {
        RunnableThread.setStatusFilosofo(StatusFilosofo.Terminou);
        System.out.println(RunnableThread.getStatusFilosofo() + " - " + Thread.currentThread().getName());
        Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
    }

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

        if (RunnableThread.talheres[talher] == 0 && RunnableThread.talheres[index] == 0) {
            RunnableThread.talheres[talher] = 1;
            RunnableThread.talheres[index] = 1;
            //System.out.println("");
            //System.out.println("talheres usados " + talher + " e " + index);
            return true;

        }
        return false;
    }

    public static void main(String[] args) {

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

enum StatusFilosofo {

    Comendo,
    Pensando,
    Terminou,
    Iniciou,
}
