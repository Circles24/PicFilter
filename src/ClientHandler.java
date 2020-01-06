import java.util.Scanner;
import java.io.*;
import java.net.*;


class ClientHandler extends PoolResource
{

    int index;
    PoolManager poolManager;
    ResourceManager resManager;

    ClientHandlerThreadder threadder;

    Socket skt;
    DataInputStream din;
    DataOutputStream dout;

    int editChoice;
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

    public ClientHandler(PoolManager poolManager,int index)throws Exception
    {

        System.out.println("@ ClientHandler.ClientHandler :: "+index);

        this.poolManager = poolManager;
        this.resManager = ResourceManager.getInstance();
        this.index = index;

        Scanner scn = new Scanner(new File("datDump/ClientHandler.dat"));

        buff = new byte[scn.nextInt()];

        scn.close();

        this.threadder = new ClientHandlerThreadder(this);
        
    }

    private ClientHandler()
    {
        // just empty resource generator method
    }

    public ClientHandler getObject(PoolManager poolManager,int index)throws Exception
    {
        return new ClientHandler(poolManager,index);

    }

    public static ClientHandler getGenerator()
    {

        return new ClientHandler();
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

        File file;

        while(true){

            try{

                System.out.println("Client Handler "+index+" iteration "+(k++));

                if(skt == null || skt.isConnected() == false ){

                    System.out.println("Client Handler "+index+" going to sleep");

                    Thread.sleep(Long.MAX_VALUE);
                }

                else{

                    try{
  
                        System.out.println("Client Handler "+index+" waiting for client");

                        editChoice = din.readInt();
                        imgSize = din.readInt();

                        System.out.println("File Size :: "+imgSize+" Edit Choice :: "+editChoice);

                        file = new File("assets/tempDump/"+index);

                        if(file.exists() == false){

                            file.mkdirs();
                        }

                        file = new File("assets/tempDump/"+index+"/img.jpg");

                        if(file.exists() == false){

                            file.createNewFile();
                        }

                        fout = new FileOutputStream("assets/tempDump/"+index+"/img.jpg");

                        System.out.println("Started reading file");

                        while(imgSize != 0){

                            n = din.read(buff);

                            imgSize -= n;

                            fout.write(buff,0,n);

                        }

                        System.out.println("read the file");

                        fout.close();

                        img = new File("assets/tempDump/"+index+"/img.jpg");

                        resManager.process(this);

                        try{

                            System.out.println("now client handler is sleeping for img processor run");

                            Thread.sleep(Long.MAX_VALUE);
                        
                        }

                        catch(Exception ex){

                            System.out.println("Exception@ClientHanlder.run "+index+" :: "+ex.getMessage());
                            ex.printStackTrace();
                        }

                        fin = new FileInputStream(img);

                        System.out.println("Sending back the processed image :: "+fin.available());

                        dout.writeInt(fin.available());

                        while((n = fin.read(buff)) != -1){

                            dout.write(buff,0,n);
                        }
                        
                        fin.close();
                        
                        System.out.println("Done");

                    }

                    
                    catch(Exception ex){

                        System.out.println("Exception@ClientHandler.run "+index+" :: "+ex.getMessage());
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

    public String getTempDumpAddress(){

        return "assets/tempDump/"+index+"/img.jpg";
    }

    public int getEditChoice(){

        return editChoice;
    }
    
}