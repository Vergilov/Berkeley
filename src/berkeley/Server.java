package berkeley;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread {
    private ServerSocket server;
    protected List<ClientHandler> clients; //lista klientów którzy otrzymają dane
    SendMessage Send;  // klasa odpowiedzialna za wysyłanie danych
    public Server(int port) {  //konstruktor serwera, 
        try {
            this.server = new ServerSocket(port);
            System.out.println("New server initialized!");
            clients = Collections
                    .synchronizedList(new ArrayList<ClientHandler>()); //Metoda służy do zwrocenia zsynchronizowanej listy (thread -safe ) poparte podanej listy .
            Scanner sc = new Scanner(System.in);
             System.out.println("Podaj jak czesto serwer ma wysylac poprawna godzine");
            Long freq = sc.nextLong();
            Send=new SendMessage(clients,freq);
            this.start();  //start serwera
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
            try {
                Socket client = server.accept();  //jak się nowy klient podłączy 
                System.out.println(client.getInetAddress().getHostName()
                        + " connected");
                ClientHandler newClient = new ClientHandler(client);
                clients.add(newClient);  //to zapisuje go do listy klientów
     

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException
    {
    new Server(1200);  //nowy serwer
    }
}