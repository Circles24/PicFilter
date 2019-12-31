import java.util.HashSet;

public class ClientHandlerPoolManager{

    ResourceManager resManager;

    ClientHandler[] ClientHandlerRef;

    HashSet<Integer> idleClientHandlers;
    HashSet<Integer> busyClientHandlers;

    public ClientHandlerPoolManager(ResourceManager resManager,int CLIENT_HANDLER_COUNT){

        System.out.println("@ClientHandlerPoolManager.ClientHandlerPoolManager");

        this.resManager = resManager;

        ClientHandlerRef = new ClientHandler[CLIENT_HANDLER_COUNT];

        idleClientHandlers = new HashSet<Integer>();
        busyClientHandlers = new HashSet<Integer>();

        for(int i=0;i<CLIENT_HANDLER_COUNT;i++){

            System.out.println("@ intializing client handlers "+i+" of "+CLIENT_HANDLER_COUNT);

            ClientHandlerRef[i] = new ClientHandler(this,resManager,i);

            idleClientHandlers.add(i);
        }
    }


	public void free(int index){

        System.out.println("@ClientHandlerPoolManager.free :: "+index);

        if(busyClientHandlers.remove(index))
            idleClientHandlers.add(index);
        
    }

    public boolean isClientHandlerAvailable(){

        return (idleClientHandlers.size() > 0);
    }

    public ClientHandler getClientHandler()throws Exception
    {   
        System.out.println("@ClientHandlerPoolManager.getClientHandler");

        if(idleClientHandlers.size() == 0)throw new Exception("No Client Handlers Available");

        int clHandlerIndex = idleClientHandlers.iterator().next();

        idleClientHandlers.remove(clHandlerIndex);
        busyClientHandlers.add(clHandlerIndex);

        return ClientHandlerRef[clHandlerIndex];

    }
}