import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable
{

    private ResourceManager resManager;

    private ServerSocket sskt;

    public Server(int portNo,int CLIENT_HANDLER_COUNT,int IMAGE_PROCESSOR_COUNT) throws Exception
    {

        if(CLIENT_HANDLER_COUNT < 1)throw new Exception("invalid client handler count");

        if(IMAGE_PROCESSOR_COUNT < 1)throw new Exception("invalid image processor count");

        System.out.println("@Server.Server");

        resManager = ResourceManager.getInstance(CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

        Thread.sleep(200);

        System.out.println("\nStatic resources allocated");

        sskt = new ServerSocket(portNo);

        new Thread(this).start();
    }

    public void run()
    {   

        System.out.println("@Server.run");

        try{

            Socket skt;
            ClientHandler clHandler;

            while(true){

                skt = sskt.accept();

                System.out.println("New Connection Req :: " + (InetSocketAddress)skt.getRemoteSocketAddress());

                if(resManager.isClientHandlerAvailable()){

                    clHandler = resManager.getClientHandler();

                    clHandler.init(skt);

                    clHandler.interrupt();

                }

                else {

                    new DataOutputStream(skt.getOutputStream()).writeUTF("Sorry Server is busy");
                
                    skt.close();
                }

            }

        }

        catch(Exception ex){

            System.out.println("Exception@Server.run :: "+ex.getMessage());

            ex.printStackTrace();

        }

    }

    public static void main(String args[])
    {
        System.out.println("@Server.main");

        try{

            Scanner scn  = new Scanner(new File("datDump/Server.dat"));

            int portNo = scn.nextInt();
            int CLIENT_HANDLER_COUNT = scn.nextInt();
            int IMAGE_PROCESSOR_COUNT = scn.nextInt();

            System.out.println("Server "+portNo+" "+CLIENT_HANDLER_COUNT+" "+IMAGE_PROCESSOR_COUNT);

            new Server(portNo,CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

            scn.close();
        }

        catch(Exception ex){

            System.out.println("Data resources are corrupted");

            System.out.println("Exception@Server.main :: "+ex.getMessage());

            ex.printStackTrace();

        }
    }

}
