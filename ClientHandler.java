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
        int imgSize;

        int k = 0;

        while(true){

            try{

                System.out.println("iterating here "+(k++));

                if(skt == null || skt.isConnected() == false)Thread.sleep(100000);

                else{
  
                    imgSize = din.readInt();

                    System.out.println("File Size :: "+imgSize);

                    fout = new FileOutputStream("assets/tempDump/"+index+"/img.png");

                    while(imgSize != 0){

                        n = din.read(buff);

                        imgSize -= n;

                        fout.write(buff,0,n);

                    }

                    fout.close();

                    img = new File("assets/tempDump/"+index+"/img.png");

                    resManager.process(this);

                    try{

                        Thread.sleep(1000000);
                    
                    }

                    catch(Exception ex){

                        System.out.println("Exception@CLientHanlder.run "+index+" :: "+ex.getMessage());
                    }

                    fin = new FileInputStream(img);

                    while((n = fin.read(buff)) != -1){

                        dout.write(buff,0,n);
                    }      
                    
                    skt.close();
                    fin.close();
                    din.close();
                    dout.close();

                    skt = null;

                    poolManager.free(index);

                }

            }

            catch(Exception ex){

                try{

                    skt.close();
                    fin.close();
                    din.close();
                    dout.close();

                    skt = null;

                }

                catch(Exception e){

                    System.out.println("Exception@ClientHandler :: "+e.getMessage());

                    e.printStackTrace();
                }

                poolManager.free(index);

                System.out.println("Exception@ClientHandler.run "+index+" :: "+ex.getMessage());
                
                ex.printStackTrace();
            }

        }

    }

    public void interrupt(){

        threadder.interrupt();
    }
    
}