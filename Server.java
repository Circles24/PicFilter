import java.io.File;
import java.io.DataOutputStream;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

import java.util.Scanner;

public class Server
{

    private ResourceManager resManager;

    private ServerSocket sSkt;

    public Server(final int portNo,final int CLIENT_HANDLER_COUNT,final int IMAGE_PROCESSOR_COUNT) throws Exception
    {

        System.out.println("@Server.Server");

        sSkt = new ServerSocket(portNo);

        resManager = ResourceManager.getInstance(CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

        System.out.println("\nStatic resources allocated");

        new Thread(){

            public void run(){

                processRequests();

            }

        }.start();
    }

    public void processRequests()
    {   

        System.out.println("@Server.run");

        try{

            Socket skt;
            ClientHandler clHandler;

            while(true){

                skt = sSkt.accept();

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

        Scanner scn = null;

        try{

            scn  = new Scanner(new File("datDump/Server.dat"));

            int portNo = scn.nextInt();
            int CLIENT_HANDLER_COUNT = scn.nextInt();
            int IMAGE_PROCESSOR_COUNT = scn.nextInt();

            if(CLIENT_HANDLER_COUNT < 0 || IMAGE_PROCESSOR_COUNT < 0)throw new Exception("wrong arguments");

            System.out.println("Server "+portNo+" "+CLIENT_HANDLER_COUNT+" "+IMAGE_PROCESSOR_COUNT);

            new Server(portNo,CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

        }

        catch(Exception ex){

            System.out.println("Data resources are corrupted");

            System.out.println("Exception@Server.main :: "+ex.getMessage());

            ex.printStackTrace();

        }

        finally{

            scn.close();
        }
    }

}
