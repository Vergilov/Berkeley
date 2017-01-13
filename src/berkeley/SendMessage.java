package berkeley;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class SendMessage extends Thread {
        protected List<ClientHandler> clients;
        protected String userInput;
        protected BufferedReader console;
        Long frequency;

        public SendMessage(List<ClientHandler> clients, long freq) {
            this.clients = clients; //konstruktor 
            this.userInput = null;
            this.start();
            this.frequency=freq;
        }

        public void run() {
           
            
            try {
                while(clients.size() == 0){}  // tu czeka az podlaczy sie jakis klient
                if (clients.size() > 0) {
                    
                    while (true) {
                      Thread.sleep(frequency);  //jak czesto aktualizowac zegarki
                            for (ClientHandler client : clients) {
                      
                                client.out.writeLong(System.currentTimeMillis());  //wysyla dane
                                client.out.flush();//czysci strumien danych wychodzacych
                                Thread.currentThread(); //Metoda zwraca odwołanie do  aktualnie wykonującego się wątku
                          
                            }
                        
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }