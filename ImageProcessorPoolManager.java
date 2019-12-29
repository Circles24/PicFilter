import java.util.HashSet;

public class ImageProcessorPoolManager{

    ResourceManager resManager;

    ImageProcessor[] imgProcessorRef;

    HashSet<ImageProcessor> idleImgProcessors;
    HashSet<ImageProcessor> busyImgProcessors;

    public ImageProcessorPoolManager(ResourceManager resManager ,int IMAGE_PROCESSORS_COUNT){

        System.out.println("@ImageProcessorPoolManager.ImageProcessorPoolManager");

        this.resManager = resManager;

        imgProcessorRef = new ImageProcessor[IMAGE_PROCESSORS_COUNT];

        idleImgProcessors = new HashSet<ImageProcessor>();
        busyImgProcessors = new HashSet<ImageProcessor>();
        
        for(int i=0;i<IMAGE_PROCESSORS_COUNT;i++){

            System.out.println("@ initializing image processor "+i+" of "+IMAGE_PROCESSORS_COUNT);

            imgProcessorRef[i] = new ImageProcessor(this,i);
            
            idleImgProcessors.add(imgProcessorRef[i]);
        
        }
    }

    public void free(int index){

        System.out.println("@ImageProcessorPoolManager.free");

        ImageProcessor imgProcessor = imgProcessorRef[index];

        if(busyImgProcessors.remove(imgProcessor)){

            idleImgProcessors.add(imgProcessor);
        }

        resManager.interrupt();
    }

    public boolean isImageProcessorAvailable(){

        return ( idleImgProcessors.size() > 0 );
    }

    public ImageProcessor getImageProcessor()throws Exception
    {
        System.out.println("@ImageProcessorPoolManager.getImageProcessor");

        if(idleImgProcessors.size() == 0)throw new Exception("Image Processors are'nt available");

        ImageProcessor imgProcessor = idleImgProcessors.iterator().next();

        idleImgProcessors.remove(imgProcessor);
        busyImgProcessors.add(imgProcessor);

        return imgProcessor;
    }
}