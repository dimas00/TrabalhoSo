package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AlgoritmoCondicionalSemafaro {

    private AlgoritmoCondicionalSemafaro.Produtor produtor;
    private AlgoritmoCondicionalSemafaro.Consumidor consumidor;
    private final static int TAM_BUFFER= 100;
    private final List<Integer> buffer= new ArrayList<>(TAM_BUFFER);
    private Semaphore vazio= new Semaphore(TAM_BUFFER);
    private Semaphore cheio= new Semaphore(0);
    private Semaphore mutex= new Semaphore(1);

    public static void main(String[] args) {
        new AlgoritmoCondicionalSemafaro();
    }

    public AlgoritmoCondicionalSemafaro() {
        this.produtor = new Produtor();
        this.consumidor = new Consumidor();
        new Thread(produtor).start();
        new Thread(consumidor).start();
    }

    private class Produtor implements Runnable {

        private Random rnd= new Random();
        @SneakyThrows
        @Override
        public void run() {
            while (true) {

                //Produz
                int num= Math.abs(rnd.nextInt());
                vazio.acquire();
                mutex.acquire();

                //grava_buffer
                System.out.println("Produtor: produziu "+ num);
                buffer.add(num);

                mutex.release();
                cheio.release();
            }
        }
    }

    private class Consumidor implements Runnable {

        private long varLocal;
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                cheio.acquire();
                mutex.acquire();

                //le_buffer
                int num= buffer.remove(0);
                System.out.println("Consumidor: consumiu "+ num);

                mutex.release();
                vazio.release();

                //consome_dado
            }
        }
    }

}
