package br.ufsm.csi.so.mutex;

import lombok.SneakyThrows;

public class AlgoritmoPeterson {

    private boolean CA= false;
    private boolean CB= false;
    private char vez;
    private long varGloblal;
    private ProcessoA processoA;
    private ProcessoB processoB;

    public static void main(String[] args) {

        new AlgoritmoPeterson();

    }

    public AlgoritmoPeterson(){
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
                AlgoritmoPeterson.this.CA= true;
                AlgoritmoPeterson.this.vez='B';
                while (AlgoritmoPeterson.this.CB && AlgoritmoPeterson.this.vez== 'B'){/*Não faz nada*/}

                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                AlgoritmoPeterson.this.CA= false;
            }
        }
    }


    private class ProcessoB implements Runnable{

        private long varLocal;
        @Override
        public void run() {
            while(true){
                //acesso a região critica
                AlgoritmoPeterson.this.CB= true;
                AlgoritmoPeterson.this.vez= 'A';
                while (AlgoritmoPeterson.this.CA && AlgoritmoPeterson.this.vez== 'A'){/*Não faz nada*/}

                //regiao critica
                varGloblal++;
                varLocal++;
                //saida regiao critica
                AlgoritmoPeterson.this.CB= false;
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
