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

    Scanner scn;
    int editChoice;

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

        scn = new Scanner(System.in);
        editChoice = -1;

        threadder = new ClientThreadder(this); 

    }

    public void init()throws Exception
    {

        skt = new Socket(serverIP,serverPortNo);

        din = new DataInputStream(skt.getInputStream());
        dout = new DataOutputStream(skt.getOutputStream());

    }

    protected void main_menu(){

        System.out.println("***************** MENU ***************\n");
        System.out.println("  Black & White          ::       0");
        System.out.println("  _GB                    ::       1");
        System.out.println("  R_B                    ::       2");
        System.out.println("  RG_                    ::       3");
        System.out.println("  R__                    ::       4");
        System.out.println("  _G_                    ::       5");
        System.out.println("  __B                    ::       6");
        System.out.println("  Hrand                  ::       7");
        System.out.println("  Srand                  ::       8");
        System.out.println("  Xrand                  ::       9");
        System.out.println("  Sdull                  ::       10");
        System.out.println("  Hdull                  ::       11");
        System.out.println("  EXrand                 ::       12");

        editChoice = scn.nextInt();
    }

    protected void menu(){

        while(true){

            main_menu();

            if(editChoice >= 0 && editChoice <= 12)break;
        }

    }

    public void run(){

        System.out.println("@Client.run");

        String imgFileName;
        int imgFileSize;

        int n;

        while(true){

            try{

                System.out.println("press 0 to exit");

                if(scn.nextInt() == 0)break;

                menu();
                
                System.out.println("enter the file address");

                imgFileName = scn.next();

                img = new File(imgFileName);
                fin = new FileInputStream(img);
                imgFileSize = fin.available();

                System.out.println(imgFileName+" :: "+imgFileSize);

                try{

                    init();
                }

                catch(Exception ex){

                    System.out.println("Exception@Client.run :: "+ex.getMessage());
                    ex.printStackTrace();

                    System.out.println("Server not available closing the shell");

                    break;
                }

                dout.writeInt(editChoice);
                dout.writeInt(imgFileSize);

                while( (n = fin.read(buff)) > 0){

                    dout.write(buff,0,n);

                }
                
                fin.close();

                System.out.println("Transfer of your image sucessful");

                imgFileName = "assets/response/res.jpg";

                img = new File(imgFileName);
                fout = new FileOutputStream(img);

                System.out.println("Server is processing your request");

                imgFileSize = din.readInt();

                System.out.println("Reading the processed file :: "+imgFileSize);

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

            Scanner  scn = new Scanner(new File("datDump/Client.dat"));

            InetAddress serverIp = InetAddress.getByName(scn.next());
            int serverPortNo = scn.nextInt();
            int BUFF_SIZE = scn.nextInt();

            scn.close();

            System.out.println("Client "+serverIp+" "+serverPortNo+" "+BUFF_SIZE);

            new Client(serverIp,serverPortNo,BUFF_SIZE);

        }

        catch(Exception ex){

            System.out.println("corrupted reource files");

            System.out.println("Exception@Client.main :: "+ex.getMessage());

            ex.printStackTrace();
        }


    }
}