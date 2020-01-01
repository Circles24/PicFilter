import java.util.HashSet;

public class ImageProcessorPoolManager{

    ImageProcessor[] imgProcessorRef;

    HashSet<Integer> idleImgProcessors;
    HashSet<Integer> busyImgProcessors;

    public ImageProcessorPoolManager(int IMAGE_PROCESSORS_COUNT)throws Exception
    {

        System.out.println("@ImageProcessorPoolManager.ImageProcessorPoolManager");

        imgProcessorRef = new ImageProcessor[IMAGE_PROCESSORS_COUNT];

        idleImgProcessors = new HashSet<Integer>();
        busyImgProcessors = new HashSet<Integer>();
        
        for(int i=0;i<IMAGE_PROCESSORS_COUNT;i++){

            System.out.println("@ initializing image processor "+i+" of "+IMAGE_PROCESSORS_COUNT);

            imgProcessorRef[i] = new ImageProcessor(this,i);
            
            idleImgProcessors.add(i);
        
        }
    }

    public void free(int index){

        System.out.println("@ImageProcessorPoolManager.free");

        if(busyImgProcessors.remove(index)){

            idleImgProcessors.add(index);
        }
    }

    public boolean isImageProcessorAvailable(){

        return ( idleImgProcessors.size() > 0 );
    }

    public ImageProcessor getImageProcessor()throws Exception
    {
        System.out.println("@ImageProcessorPoolManager.getImageProcessor");

        if(idleImgProcessors.size() == 0)throw new Exception("Image Processors are'nt available");

        int imgProcessorIndex = idleImgProcessors.iterator().next();

        idleImgProcessors.remove(imgProcessorIndex);
        busyImgProcessors.add(imgProcessorIndex);

        return imgProcessorRef[imgProcessorIndex];
    }
}