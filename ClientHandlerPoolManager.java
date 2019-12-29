import java.util.HashSet;

public class ClientHandlerPoolManager{

    ResourceManager resManager;

    ClientHandler[] ClientHandlerRef;

    HashSet<ClientHandler> idleClientHandlers;
    HashSet<ClientHandler> busyClientHandlers;

    public ClientHandlerPoolManager(ResourceManager resManager,int CLIENT_HANDLER_COUNT){

        System.out.println("@ClientHandlerPoolManager.ClientHandlerPoolManager");

        this.resManager = resManager;

        ClientHandlerRef = new ClientHandler[CLIENT_HANDLER_COUNT];

        idleClientHandlers = new HashSet<ClientHandler>();
        busyClientHandlers = new HashSet<ClientHandler>();

        for(int i=0;i<CLIENT_HANDLER_COUNT;i++){

            System.out.println("@ intializing client handlers "+i+" of "+CLIENT_HANDLER_COUNT);

            ClientHandlerRef[i] = new ClientHandler(this,resManager,i);

            idleClientHandlers.add(ClientHandlerRef[i]);
        }
    }


	public void free(int index){

        System.out.println("@ClientHandlerPoolManager.free");

        ClientHandler currentRef = ClientHandlerRef[index];

        if(busyClientHandlers.remove(currentRef))
            idleClientHandlers.add(currentRef);
        
    }

    public boolean isClientHandlerAvailable(){

        return (idleClientHandlers.size() > 0);
    }

    public ClientHandler getClientHandler()throws Exception
    {   
        System.out.println("@ClientHandlerPoolManager.getClientHandler");

        if(idleClientHandlers.size() == 0)throw new Exception("No Client Handlers Available");

        ClientHandler clHandler = idleClientHandlers.iterator().next();

        idleClientHandlers.remove(clHandler);
        busyClientHandlers.add(clHandler);

        return clHandler;

    }
}