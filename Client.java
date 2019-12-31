import java.util.Scanner;
import java.net.InetAddress;
import java.net.Socket;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Client{

    InetAddress serverIP;
    int serverPortNo;

    Socket skt;

    DataInputStream din;
    DataOutputStream dout;

    byte[] buff;

    File img;
    FileInputStream fin;
    FileOutputStream fout;

    ClientThreadder  threadder;

    class ClientThreadder extends Thread
    {

        Client cl;

        public ClientThreadder(Client cl){

            this.cl = cl;

            this.start();
        }

        public void run(){

            cl.run();
        }
    }

    public Client(InetAddress serverIP,int serverPortNo,int BUFF_SIZE)throws Exception
    {

        this.serverIP = serverIP;
        this.serverPortNo = serverPortNo;
        
        buff = new byte[BUFF_SIZE];

        threadder = new ClientThreadder(this); 
    }

    public void init()throws Exception
    {

        skt = new Socket(serverIP,serverPortNo);

        din = new DataInputStream(skt.getInputStream());
        dout = new DataOutputStream(skt.getOutputStream());

    }

    public void run(){

        System.out.println("@Client.run");

        String imgFileName;
        int imgFileSize;

        int n;
        
        Scanner scn = new Scanner(System.in);

        while(true){

            try{

                System.out.println("Enter 0/1 for exit/continue");

                if(scn.nextInt() == 0)break;
            
                try{

                    init();
                }

                catch(Exception ex){

                    System.out.println("Exception@Client.run :: "+ex.getMessage());
                    ex.printStackTrace();

                    System.out.println("Server not available closing the shell");

                    break;
                }
                
                System.out.println("enter the file address");

                imgFileName = scn.next();

                img = new File(imgFileName);
                fin = new FileInputStream(img);
                imgFileSize = fin.available();

                System.out.println(imgFileName+" :: "+imgFileSize);

                dout.writeInt(imgFileSize);

                while( (n = fin.read(buff)) > 0){

                    dout.write(buff,0,n);

                }
                
                fin.close();

                System.out.println("Transfer of your image sucessful");
                System.out.println("Enter the result img address");

                imgFileName = scn.next();
                img = new File(imgFileName);

                fout = new FileOutputStream(img);

                System.out.println("Server is processing your request");

                while(imgFileSize > 0){

                    n = din.read(buff);

                    imgFileSize -= n;

                    fout.write(buff,0,n);

                }

                System.out.println("your img has been saved as "+imgFileName);
                System.out.println("Done");
            }

            catch(Exception ex){

                System.out.println("Exception@Client.run :: "+ex.getMessage());

                ex.printStackTrace();

            }

        }

        try{

            if(scn != null)scn.close();
            if(din != null)din.close();
            if(dout != null)dout.close();
            if(skt != null)skt.close();
        
        }
    
        catch(Exception ex)
        {

            System.out.println("Exception@Client.run :: "+ex.getMessage());
            ex.printStackTrace();

        }
    }

    public static void main(String args[]){

        try{

            if(args.length != 3)throw new Exception("Usage :: java Client <ServerIP> <ServerPortNo> <BUFF_SIZE> ");

            InetAddress serverIp = InetAddress.getByName(args[0]);

            int serverPortNo = Integer.parseInt(args[1]);

            int BUFF_SIZE = Integer.parseInt(args[2]);

            new Client(serverIp,serverPortNo,BUFF_SIZE);

        }

        catch(Exception ex){

            System.out.println("Exception@Client.main :: "+ex.getMessage());

            ex.printStackTrace();
        }


    }
}