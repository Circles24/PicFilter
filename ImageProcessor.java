import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class ImageProcessor extends Thread
{
    ClientHandler CHandler;

    public ImageProcessor(){

        CHandler = null;

        this.start();

    }

    private void process(){


    }

    public void run(){

        while(true){

            try{

                if(CHandler == null)This.Sleep(1000);

                else {

                    process();

                    CHandler.interrupt();

                    resManager.free(this);

                    CHandler = null;
                }

            }

            catch(Exception ex){


            }
        }
    }

}