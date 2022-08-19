package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class Algoritmo1 {

    private char vez = 'A';
    private long varGloblal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {

        new Algoritmo1();

    }

    public Algoritmo1(){
        this.processoA = new ProcessoA();
        this.processoB = new ProcessoB();
        new Thread(processoA).start();
        new Thread(processoB).start();
        new Thread(new ConfereConsistencia()).start();
    }
    private class ProcessoA implements Runnable{

        private long varLocal;
        @Override
        public void run() {
            while(true){
                //acesso a região critica

                while (vez == 'B'){/*Não faz nada*/}
                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                vez= 'B';
            }
        }
    }


    private class ProcessoB implements Runnable{

        private long varLocal;
        @Override
        public void run() {
            while(true){
                //acesso a região critica

                while (vez == 'A'){/*Não faz nada*/}
                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                vez= 'A';
            }
        }
    }

    private class ConfereConsistencia implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(1000);
                if (varGloblal != (processoA.varLocal + processoB.varLocal)) {
                    System.out.println("Inconssistente: " + Math.abs(varGloblal));
                }
                else{
                    System.out.println("Consistente");
                }
            }
        }
    }

}
