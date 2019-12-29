import java.io.*;
import java.net.*;

class ClientHandler
{

    int index;
    ClientHandlerPoolManager poolManager;
    ResourceManager resManager;

    ClientHandlerThreadder threadder;

    Socket skt;
    DataInputStream din;
    DataOutputStream dout;

    File img;
    byte[] buff;

    FileOutputStream fout;
    FileInputStream fin;

    class ClientHandlerThreadder extends Thread
    {

        ClientHandler ClHandler;

        public ClientHandlerThreadder(ClientHandler ClHandler){

            this.ClHandler = ClHandler;
            this.start();

        }

        public void run(){
            
            ClHandler.run();
            
        }

    }

    public ClientHandler(ClientHandlerPoolManager poolManager,ResourceManager resManager,int index){

        System.out.println("@ ClientHandler.ClientHandler :: "+index);

        this.poolManager = poolManager;
        this.resManager = resManager;
        this.index = index;

        this.threadder = new ClientHandlerThreadder(this);
        
    }

    public void init(Socket skt)throws Exception
    {

        System.out.println("@ ClientHandler.init :: "+index);

        this.skt = skt;

        din = new DataInputStream(skt.getInputStream());
        dout = new DataOutputStream(skt.getOutputStream());

        threadder.interrupt();

    }

    public void run(){

        System.out.println("@ ClientHandler.run :: "+index);

        int n;

        while(true){

            try{

                if(din == null)Thread.sleep(1000);

                else{
  
                    int imgSize = din.readInt();

                    fout = new FileOutputStream("assets/"+index+"/img.png");

                    while(imgSize != 0){

                        n = din.read(buff);

                        imgSize -= n;

                        fout.write(buff,0,n);

                    }

                    fout.close();

                    img = new File("assets/"+index+"/img.png");

                    resManager.process(this);

                    try{

                        Thread.sleep(100000);
                    
                    }

                    catch(Exception ex){

                        System.out.println("Exception@CLientHanlder.run "+index+" :: "+ex.getMessage());
                    }

                    fin = new FileInputStream(img);

                    while((n = fin.read(buff)) != -1){

                        dout.write(buff,0,n);
                    }      
                    
                    fin.close();
                    din.close();
                    dout.close();

                    poolManager.free(index);

                }

            }

            catch(Exception ex){

                System.out.println("Exception@ClientHandler.run "+index+" :: "+ex.getMessage());
                
                ex.printStackTrace();
            }

        }

    }

    public void interrupt(){

        threadder.interrupt();
    }
    
}