package jantarfilosofos;

import java.util.concurrent.RunnableScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Larissa e Elenara
 */
class RunnableThread implements Runnable {

    public static boolean semaforo = true;
    public static int quantidade = 5;
    public static int hashis[] = new int[5];

    @Override
    public void run() {
        while (true) {
            while (RunnableThread.semaforo == false) {
                try {
                    Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
                } catch (InterruptedException ex) {
                    Logger.getLogger(RunnableThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            RunnableThread.semaforo = false;
            int filosofo = Integer.parseInt((Thread.currentThread().getName())) - 1;
            System.out.println("");
            System.out.println("Filosofo " + (filosofo + 1) + " iniciou");
            int index = JantarFilosofos.filaCircular((filosofo));
            JantarFilosofos.acao(filosofo, index);
            RunnableThread.semaforo = true;

            try {
                Thread.sleep(1 + (int) (Math.random() * (1000 - 1)));
            } catch (InterruptedException ex) {
                Logger.getLogger(RunnableThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}

public class JantarFilosofos {

    public static void acao(int hashi, int index) {
        try {
            if (podeComer(hashi) == true) {
                System.out.println("");
                System.out.println("Comendo " + Thread.currentThread().getName());
                Thread.sleep(1 + (int) (Math.random() * (10000 - 1)));
                RunnableThread.hashis[hashi] = 0;
                RunnableThread.hashis[index] = 0;

            } else {
                System.out.println("");
                System.out.println("Esperando " + Thread.currentThread().getName());
                Thread.sleep(1 + (int) (Math.random() * (10000 - 1)));
            }

        } catch (InterruptedException iex) {
            System.out.println("Exception: " + iex.getMessage());
        }
    }

    public static void esperando() {

    }

    public static int filaCircular(int hashi) {
        int index;
        if (hashi == 0) {
            index = 4;

        } else {
            index = hashi - 1;
        }

        return index;
    }

    public static boolean podeComer(int hashi) throws InterruptedException {

        int index = filaCircular(hashi);

        if (RunnableThread.hashis[hashi] == 0 && RunnableThread.hashis[index] == 0) {
            RunnableThread.hashis[hashi] = 1;
            RunnableThread.hashis[index] = 1;
            System.out.println("");
            System.out.println("Hashis usados " + hashi + " e " + index);
            return true;

        }
        return false;
    }

    public static void main(String[] args) {

        for (int i = 0; i < RunnableThread.hashis.length; i++) {
            RunnableThread.hashis[i] = 0;
        }

        RunnableThread rt = new RunnableThread();

        Thread f1 = new Thread(rt);
        f1.setName("1");
        f1.start();

        Thread f2 = new Thread(rt);
        f2.setName("2");
        f2.start();

        Thread f3 = new Thread(rt);
        f3.setName("3");
        f3.start();

        Thread f4 = new Thread(rt);
        f4.setName("4");
        f4.start();

        Thread f5 = new Thread(rt);
        f5.setName("5");
        f5.start();
    }

}
