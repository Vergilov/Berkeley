package berkeley;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
   
    public static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");  //formatka czasu
    public static long Timer; 
    public static Long IntToLong(int G,int M,int S) throws ParseException     // zamienia wpisywane liczby na liczbe long(liczba milisekund)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String inputString = G-1+":"+M+":"+S; //poprawka na strefę czasu g-1

        Date date = sdf.parse("1970-01-01 " + inputString); //Czas uniksowy
     
     
    return date.getTime();
    }
    public static class Updater extends Thread
    {
        Socket client;
        public Updater(String host, int port) throws IOException
        {
            client = new Socket(host, port);
        }
        public void run()
        {
            System.out.println("Updater is working");
        InputStream inFromServer =null;
            try {
                inFromServer = client.getInputStream();
                DataInputStream in = new DataInputStream(inFromServer);
                while(true)
                {
                    System.out.println("Test1");
                   Timer=in.readLong();  //jak pojawia sie nowe dane w strumieniu to poprawia godzine
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inFromServer.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public static class InternalInaccuratClock extends Thread  //niedokladny serwer
    {
       // public long Timer; 
        private long Drift;   //dryft zegara
        public InternalInaccuratClock(long how_inaacurate)  //konstruktor zegara
        {
            Drift=how_inaacurate;
            Timer = System.currentTimeMillis();
        }
        public void clockReset(Long newTime)
        {
            Timer = newTime;
        }
        public void run()
        {
            
            while(true)
            {
                try 
                    {
                        Thread.sleep(1000+Drift);     //1 sekunda + dryft (dodatni lub ujemny[zle brzmi dryft ujemny ale chodzi o za szybki zegar] ) 
                        Timer+=1000;  // dodanie jednej sekundy do licznika czasu
                        System.out.println(SDF.format(Timer)); //wypisanie niedokładnego czasu
                
                    } 
                catch (InterruptedException ex) 
                    {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        }
    }
    public static void main(String[] args) throws IOException, ParseException
    {
    
    
    Scanner sc = new Scanner(System.in);
    System.out.println("Podaj godzine format HH");
    Integer G = sc.nextInt();
    System.out.println("Podaj minute format MM");
    Integer M = sc.nextInt();
    System.out.println("Podaj sekunde SS");
    Integer S = sc.nextInt();
    System.out.println("Podaj dryft zegarka w minisekundach");
    Long dryft = sc.nextLong();
    InternalInaccuratClock IIC = new InternalInaccuratClock(dryft);
    IIC.clockReset(IntToLong(G,M,S));
    IIC.start();
    Updater U = new Updater("localhost", 1200);
    U.start();
    } 
}