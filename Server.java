import java.io.*;
import java.net.*;

public class Server implements Runnable
{

    private ResourceManager resManager;

    private ServerSocket sskt;

    public Server(int portNo) throws Exception
    {

        resManager = ResourceManager.getInstance();

        sskt = new ServerSocket(portNo);

        new Thread(this).start();
    }

    public void run()
    {


    }

    public static void main(String args[])
    {
        try{


        }

        catch(ArrayIndex)
    }

}
