package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class Algoritmo2 {

    private boolean CA= false;
    private boolean CB= false;
    private long varGloblal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {

        new Algoritmo2();

    }

    public Algoritmo2(){
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

                while (CB){/*Não faz nada*/}
                CA= true;
                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                CA= false;
            }
        }
    }


    private class ProcessoB implements Runnable{

        private long varLocal;
        @Override
        public void run() {
            while(true){
                //acesso a região critica

                while (CA){/*Não faz nada*/}
                CB= true;
                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                CB= false;
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
