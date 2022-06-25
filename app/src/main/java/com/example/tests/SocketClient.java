package com.example.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    private final int HEADER = 4;  // formato de Header: 0023
    private final MainActivity activity;

    private volatile Socket conn;
    private volatile PrintStream connOutput; // output: recebe a mensagem do servidor
    private volatile BufferedReader connInput;  // input: recebe a mensagem do servidor

    public SocketClient(MainActivity activity){
        this.activity = activity;
        new Thread(this::connect).start();
    }

    private synchronized void connect(){
        // função de conexão. Cria o socket, input e output
        
        // todo: checar se a conexão ainda existe
        activity.print("Conectando...");
        String ip = activity.getIP();
        int port = activity.getPort();

        try {
            conn = new Socket(ip, port);
            connOutput = new PrintStream(conn.getOutputStream());
            connInput = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            activity.print("Conectado a " + ip + " na porta " + port);
            activity.print("Recebido: " + recv());

        } catch (IOException e) {
            activity.print("Falha na conexão.");
            activity.print(String.valueOf(e), "SocketClient.connect");
        }
    }

    private void disconnect(){
        activity.print("Desconectando...");
        try {  // tenta enviar pacote de desconexão e fecha socket
            connOutput.print(headerGen(Msg.DISCONNECT.txt));
            connOutput.print(Msg.DISCONNECT.txt);
            conn.close();
            activity.print("Desconecatdo com sucesso.");
        } catch (IOException | NullPointerException ignored){}
        finally {  // por fim, fecha input e output
            try {
                connOutput.close();
                connInput.close();
            } catch (IOException | NullPointerException ignored) {}
        }
    }

    private String headerGen(String msg){
        String bufferTxt = Integer.toString(msg.length());
        return new String(
                new char[HEADER - bufferTxt.length()]).replace("\0", "0")
                + bufferTxt;
        // isso deve gerar um cabeçalho do tamanho definido
    }

    private void send(String msg){
        // esta função além de mandar o header e o texto,
        // serve para resetar conexão enviando mensagens
        // todo: passar função de reset e retry para o recv
        // todo: implementar horário e latência aqui
        String header = headerGen(msg);

        Runnable sending = () -> {
            connOutput.print(header);  // header com tamanho das mensagens
            connOutput.print(msg);  // envia a mensagem
            activity.print("Recebido: " + recv());
        };  // cria um lambda para ter a chance de tentar executar novamente
        try {
            sending.run();
        } catch (NullPointerException e) {
            activity.print("Resetando conexão.");
            try {
                actionReset();  // tenta resetar conexão e então mandar mensagem
                sending.run();
            } catch (NullPointerException e1){
                activity.print(String.valueOf(e), "SocketClient.send");
            }
        }
    }

    private String recv(){
        // recebe o cabeçalho e a mensagem no socket
        char[] header = new char[HEADER];
        try {
            connInput.read(header);  // recebe o header
            // este método modifica o reader e retorna o número de caracteres lidos
            int buffer = Integer.parseInt(String.valueOf(header));
            // extrai do cabeçalho o valor do buffer
            char[] msg = new char[buffer];  // cria o valor buffer
            connInput.read(msg);
            return new String(msg);
        } catch (IOException | NumberFormatException e){
            actionReset();
            return "";
        }
    }

    public void actionReset(){
        disconnect();
        connect();
    }

    public void actionPing(){
        activity.print("Ping.");
        send(Msg.PING.txt);
    }

    public void testConn(){
        // testa a conexão se uma existir
        try {
            if (!conn.isConnected()) {
                actionReset();
            }
        } catch (NullPointerException ignored) {}
    }

    public void actionLED(){
        activity.print("Mudando estado da led");
        send(Msg.LED.txt);
    }

    public void actionRelay(int n){
        activity.print("Mudando estado do relê");
        send(Msg.RELAY.txt + n);
    }

    public void actionAddNw(String SSID, String password){
        activity.print("Salvando rede " + SSID);
        send(Msg.ADD_NW.txt + "-" + SSID + "-" + password);
    }

    public void actionRmNw(String SSID){
        activity.print("Removendo rede " + SSID);
        send(Msg.DEL_NW.txt + "-" + SSID);
    }
}