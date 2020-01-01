import java.util.Queue;
import java.util.LinkedList;

class ResourceManager
{

    private static ResourceManager selfRef;

    Queue<ClientHandler> processQueue;

    ClientHandlerPoolManager CHPoolManager;
    ImageProcessorPoolManager IPPoolManager;

    ResourceManagerThreadder threadder;

    class ResourceManagerThreadder extends Thread
    {

        ResourceManager resManager;

        public ResourceManagerThreadder(ResourceManager resManager){

            this.resManager = resManager;

            this.start();
        }

        public void run(){

            while(true){

                try{

                    resManager.run();

                }
                
                catch(Exception ex){

                    System.out.println("Exception@ResourceManagerThreadder.run :: "+ex.getMessage());
                }
            }

        }

    }

    private ResourceManager(int CLIENT_HANDLER_COUNT,int IMAGE_PROCESSOR_COUNT)throws Exception
    {

        System.out.println("@ResourceManager.ResourceManager");

        selfRef = this;

        processQueue = new LinkedList<ClientHandler>();

        System.out.println("\nAllocating Client Handlers");

        CHPoolManager = new ClientHandlerPoolManager(CLIENT_HANDLER_COUNT);

        System.out.println("\nAllocating image processors");

        IPPoolManager = new ImageProcessorPoolManager(IMAGE_PROCESSOR_COUNT);

        threadder = new ResourceManagerThreadder(this);
    }

    public static synchronized ResourceManager getInstance(int CLIENT_HANDLER_COUNT,int IMAGE_PROCESSOR_COUNT)throws Exception
    {
        System.out.println("@ResourceManager.getInstance(int,int)");

        if(selfRef == null){

            new  ResourceManager(CLIENT_HANDLER_COUNT,IMAGE_PROCESSOR_COUNT);

        }
            
        return selfRef;
    }

    public static synchronized ResourceManager getInstance() throws Exception
    {
        System.out.println("@ResourceManager.getInstance()");

        if(selfRef == null)throw new Exception("Resource Manager not initialized yet");

        return selfRef;
    }

    public boolean isClientHandlerAvailable()
    {
        return CHPoolManager.isClientHandlerAvailable();
    }

    public synchronized void process(ClientHandler CHandler)
    {

        System.out.println("@ResourceManager.process");

        processQueue.add(CHandler);

        System.out.println("now interrupting the resManagerThreadder");

        threadder.interrupt();

        System.out.println("resmanager interrupted");
    }

    public synchronized ClientHandler getClientHandler()throws Exception
    {
        if(CHPoolManager.isClientHandlerAvailable() == false)throw new Exception("All Client Handlers are busy");
        
        return CHPoolManager.getClientHandler();
    }

    public void interrupt(){

        threadder.interrupt();
    }

    public void run(){

        System.out.println("@ResourceManager.run");

        ClientHandler chHandler;
        ImageProcessor imgProcessor;

        int iterationID = 0;

        while(true){

            System.out.println("@ResourceManager.run iteration "+(iterationID++));

            try{

                if(processQueue.size() == 0 || IPPoolManager.isImageProcessorAvailable() == false){

                    Thread.sleep(Long.MAX_VALUE);

                }

                else{

                    System.out.println("Managing Queue");

                    while(processQueue.size() > 0 && IPPoolManager.isImageProcessorAvailable() == true ){

                        chHandler = processQueue.remove();
                        imgProcessor = IPPoolManager.getImageProcessor();

                        imgProcessor.init(chHandler);

                    }
                }

            }

            catch(Exception ex){

                System.out.println("Exception@ResourceManager.run :: "+ex.getMessage());
            }
        }
    }

}