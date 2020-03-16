/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author student1
 */
public class ServerThread extends Thread{

    private ServerSocket serverSocket;
    List<ClientHandlerThread> clients;

    public ServerThread() throws IOException {
        serverSocket=new ServerSocket(9000);
        clients=new ArrayList<>();
    }
    
    
    
    @Override
    public void run() {
        while(!serverSocket.isClosed()){
            System.out.println("waiting...");
            try {
                Socket socket=serverSocket.accept();
                ClientHandlerThread client=new ClientHandlerThread(socket);
                client.start();
                clients.add(client);
                System.out.println("Client connected!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
        stopClientHandlers();
        
    }
    
    public  void stopServerThread() throws IOException{
        serverSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    private void stopClientHandlers(){
        for (ClientHandlerThread client : clients) {
            try {
                client.getSocket().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
