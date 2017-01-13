package berkeley;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {  //klasa łącząca klienta i strumień danych w parę
    protected Socket client;
   
    protected DataOutputStream out;

    public ClientHandler(Socket client) {
        this.client = client;
        try {
        
            this.out = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}