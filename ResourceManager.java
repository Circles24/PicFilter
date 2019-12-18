
class ResourceManager
{

    private final static ResourceManager selfRef;

    ClientHandlerPool CHPool;

    ImageProcessorPool IPPool;

    private ResourceManager()
    {
        CHPool = new ClientHandlerPool();

        IPPool = new ImageProcessorPool();
    }

    public static synchronized ResourceManager getInstance()
    {
        if(selfRef == null)selfRef = new  ResourceManager();

        return selfRef;
    }

    public boolean isClientHandlerAvailable()
    {
        return CHPool.isClientHandlerAvailable();
    }

    public synchronized void process(ClientHandler CHandler){

        IPPool.process(CHandler);
    }

}