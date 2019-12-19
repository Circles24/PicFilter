import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;

class ClientHandler
{

    int index;
    ResourceManager resManager;

    DataInputStream din;
    DataOutputStream dout;

    File img;
    byte[] buff;
    BufferedImage buffImg;

    FileOutputStream fout;

    class ClientHandlerThreadder extends Thread
    {

        ClientHandler CHandler;

        public ClientHandlerThreadder(ClientHandler CHandler){

            this.CHandler = CHandler;
            this.start();

        }

        public void run(){
            
            CHandler.run();
            
        }

    }

    public ClientHandler(int index,ResourceManager resManager){

        System.out.println("@ ClientHandler.ClientHandler :: "+index);

        this.index = index;
        this.resManager = resManager;
        
    }

    public void init(Socket skt)throws Exception
    {

        System.out.println("@ ClientHandler.init :: "+index);

        din = new DataInputStream(skt.getInputStream());
        dout = new DataOutputStream(skt.getOutputStream());

        

    }

    public void run(){

        System.out.println("@ CLientHandler.run :: "+index);

        int n;

        while(true){

            try{
  
                int imgSize = din.readInt();

                fout = new FileOutputStream("assets/"+index+"/img.png");

                while(true){

                    n = din.read(buff);
                    
                    if(n == -1)break;

                    fout.write(buff,0,n);

                }

                fout.close();

                img = new file("assets/"+index+"/img.png");

                buffImage = ImageIO.read(img);

                resManager.process(this);

                

            }

            catch(Exception ex){

                System.out.println("Exception@ClientHandler.run "+index+" :: "+ex.getMessage());
                
                ex.printStackTrace();
            }

        }

    }
    
}