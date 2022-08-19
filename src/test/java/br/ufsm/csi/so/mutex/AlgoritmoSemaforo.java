package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class AlgoritmoSemaforo {

    private boolean CA= false;
    private boolean CB= false;
    private char vez;
    private long varGloblal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {

        new AlgoritmoSemaforo();

    }

    public AlgoritmoSemaforo(){
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
                AlgoritmoSemaforo.this.CA= true;
                AlgoritmoSemaforo.this.vez='B';
                while (AlgoritmoSemaforo.this.CB && AlgoritmoSemaforo.this.vez== 'B'){/*Não faz nada*/}

                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                AlgoritmoSemaforo.this.CA= false;
            }
        }
    }


    private class ProcessoB implements Runnable{

        private long varLocal;
        @Override
        public void run() {
            while(true){
                //acesso a região critica
                AlgoritmoSemaforo.this.CB= true;
                AlgoritmoSemaforo.this.vez= 'A';
                while (AlgoritmoSemaforo.this.CA && AlgoritmoSemaforo.this.vez== 'A'){/*Não faz nada*/}

                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                AlgoritmoSemaforo.this.CB= false;
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
