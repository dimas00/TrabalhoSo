FROM openjdk:8 
RUN apt install git
RUN git clone https://github.com/dimas00/TrabalhoSo.git
WORKDIR TrabalhoSo/src/main/java
RUN javac br/ufsm/csi/so/webserver/Servidor.java
ENTRYPOINT ["java", "br.ufsm.csi.so.webserver.Servidor"]
