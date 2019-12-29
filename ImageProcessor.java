import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class ImageProcessor 
{
    int index;
    ImageProcessorPoolManager poolManager;
    ClientHandler ClHandler;

    ImageProcessorThreadder threadder;

    class ImageProcessorThreadder extends Thread
    {

        ImageProcessor imgProcessor;        

        public ImageProcessorThreadder(ImageProcessor imgProcessor){

            this.imgProcessor = imgProcessor;

            this.start();
        }
        public void run(){

            imgProcessor.run();
        }
    }

    public ImageProcessor(ImageProcessorPoolManager poolManager,int index){

        System.out.println("@ImageProcessor.ImageProcessor :: "+index);

        ClHandler = null;

        this.poolManager = poolManager;
        this.index = index;

        threadder = new ImageProcessorThreadder(this);

    }

    private void process(){

        System.out.println("@ImageProcessor.process :: "+index);

    }

    public void init(ClientHandler ClHandler){

        System.out.println("@ImageProcessor.init :: "+index);

        this.ClHandler = ClHandler;

        threadder.interrupt();
    }

    public void run(){

        System.out.println("@ImageProcessor.run :: "+index);

        while(true){

            try{

                if(ClHandler == null)Thread.sleep(1000);

                else {

                    process();

                    ClHandler.interrupt();

                    poolManager.free(index);

                    ClHandler = null;
                }

            }

            catch(Exception ex){

                System.out.println("Exception@ImageProcessor.run :: "+ex.getMessage());

            }
        }
    }

}