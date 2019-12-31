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

                System.out.println("Client Handler "+index+" iteration "+(k++));

                if(skt == null || skt.isConnected() == false ){

                    System.out.println("Client Handler "+index+" going to sleep");

                    Thread.sleep(100000);
                }

                else{

                    try{
  
                        System.out.println("Client Handler "+index+" waiting for client");

                        imgSize = din.readInt();

                        System.out.println("File Size :: "+imgSize);

                        // fout = new FileOutputStream("assets/tempDump/"+index+"/img.png");

                        // while(imgSize != 0){

                        //     n = din.read(buff);

                        //     imgSize -= n;

                        //     fout.write(buff,0,n);

                        // }

                        // fout.close();

                        // img = new File("assets/tempDump/"+index+"/img.png");

                        // resManager.process(this);

                        // try{

                        //     Thread.sleep(1000000);
                        
                        // }

                        // catch(Exception ex){

                        //     System.out.println("Exception@CLientHanlder.run "+index+" :: "+ex.getMessage());
                        // }

                        // fin = new FileInputStream(img);

                        // while((n = fin.read(buff)) != -1){

                        //     dout.write(buff,0,n);
                        // }      
                        
                        System.out.println("Done");

                    }

                    
                    catch(Exception ex){

                        System.out.println("Exception@ClientHandler.run :: "+ex.getMessage());
                        ex.printStackTrace();

                    }

                    finally{

                        System.out.println("@ClientHandler.run.finally");

                        if(skt !=  null)skt.close();
                        if(fin !=  null)fin.close();
                        if(din !=  null)din.close();
                        if(dout !=  null)dout.close();

                        skt = null;

                        poolManager.free(index);

                    }

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