import java.io.*;
import java.net.*;

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

            if(args.length != 3)throw new Exception("Usage :: java Server <portNo> <client_handler_count> <image_processor_count> ");

            int portNo = Integer.parseInt(args[0]);
            int CLIENT_HANDLER_COUNT = Integer.parseInt(args[1]);
            int IMAGE_PROCESSOR_COUNT = Integer.parseInt(args[2]);

            new Server(portNo,CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

        }

        catch(Exception ex){

            System.out.println("Exception@Server.main :: "+ex.getMessage());

            ex.printStackTrace();

        }
    }

}
