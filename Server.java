import java.io.*;
import java.net.*;

public class Server implements Runnable
{

    private ResourceManager resManager;

    private ServerSocket sskt;

    public Server(int portNo) throws Exception
    {

        System.out.println("@Server.Server");

        resManager = ResourceManager.getInstance();

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

                System.out.println("New Connection Req :: " + (InetSocketAddress)skte.getRemoteSocketAddress());

                if(resManager.ClientHandlerAvailable()){

                    clHandler = resManager.getClientHandler();

                    clHandler.init(skt);

                    clhandler.interrupt();

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

            if(args.length != 1)throw new Exception("Usage :: java Server <portNo>");

            int portNo = Integer.parseInt(args[0]);

            new Server(portNo);

        }

        catch(Exception ex){

            System.out.println("Exception@Server.main :: "+ex.getMessage());

            ex.printStackTrace();

        }
    }

}
