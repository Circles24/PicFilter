import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.util.Random;

class ImageProcessor 
{
    ResourceManager resManager;

    int index;
    ImageProcessorPoolManager poolManager;
    ClientHandler ClHandler;

    ImageProcessorThreadder threadder;

    BufferedImage buffImage;
    
    int imgWidth;
    int imgHeight;
    int editChoice;

    int A,R,G,B,data,temp;
    Random rand;

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

    public ImageProcessor(ImageProcessorPoolManager poolManager,int index)throws Exception
    {

        System.out.println("@ImageProcessor.ImageProcessor :: "+index);

        ClHandler = null;

        resManager = ResourceManager.getInstance();

        this.poolManager = poolManager;
        this.index = index;

        rand = new Random(System.currentTimeMillis());

        threadder = new ImageProcessorThreadder(this);

    }

    private void processData(){

        A = R = G = B = 0;

        A = (data>>24)&0xff; 
        R = (data>>16)&0xff; 
        G = (data>>8)&0xff; 
        B = data&0xff; 

        // System.out.println("    #data :: "+A+" "+R+" "+G+" "+B);

        switch(editChoice){

                case 0:
                {

                    R = (R+G+B)/3;
                    G = R;
                    B = R;

                }break;

                case 1:
                {
                    R = 0;

                }break;

                case 2:
                {
                    G = 0;

                }break;

                case 3:
                {
                    B = 0;

                }break;

                case 4:
                {
                    G = 0;
                    B = 0;

                }break;

                case 5:
                {
                    R = 0;
                    B = 0;

                }break;

                case 6:
                {

                    R = 0;
                    G = 0;

                }break;

                case 7:
                {

                    if(rand.nextBoolean())R /= ((rand.nextInt()%10)+10)%10+1;
                    else R *= ((rand.nextInt()%10)+10)%10+1;
                    
                    if(rand.nextBoolean())G /= ((rand.nextInt()%10)+10)%10+1;
                    else G *= ((rand.nextInt()%10)+10)%10+1;

                    if(rand.nextBoolean())B /= ((rand.nextInt())%10+10)%10+1;
                    else B *= ((rand.nextInt()%10)+10)%10+1;

                }break;

                case 8:
                {

                    if(rand.nextBoolean())R /= ((rand.nextInt()%3)+3)%3+1;
                    else R *= ((rand.nextInt()%3)+3)%3+1;
                    
                    if(rand.nextBoolean())G /= ((rand.nextInt()%3)+3)%3+1;
                    else G *= ((rand.nextInt()%3)+3)%3+1;

                    if(rand.nextBoolean())B /= ((rand.nextInt())%3+3)%3+1;
                    else B *= ((rand.nextInt()%3)+3)%3+1;

                }break;

                case 9:
                {

                    R = (((R^(rand.nextInt()%100+100))%255+255)%255);
                    R = (((G^(rand.nextInt()%100+100))%255+255)%255);
                    R = (((B^(rand.nextInt()%100+100))%255+255)%255);

                }break;

                case 10:
                {

                    R >>= 2;
                    G >>= 2;
                    B >>= 2;

                }break;

                case 11:
                {

                    R >>= 4;
                    G >>= 4;
                    B >>= 4;

                }break;

                case 12:
                {

                    R |= ((rand.nextInt()%100)+100)%100;
                    G |= ((rand.nextInt()%100)+100)%100;
                    B |= ((rand.nextInt()%100)+100)%100;

                }break;

                case 13:
                {

                    R = ~R;
                    G = ~G;
                    B = ~B;

                }break;

                case 14:
                {

                    temp = R;
                    R = G;
                    G = B;
                    B = temp;

                }break;

                case 15:
                {

                    temp = B;
                    B = G;
                    G = R;
                    R = temp;

                }break;

        }

        // System.out.println("    $data :: "+A+" "+R+" "+G+" "+B);

        data = (A<<24) | (R<<16) | (G<<8) | B; 

    }

    private void process() throws Exception
    {

        System.out.println("@ImageProcessor.process :: "+index);

        buffImage = ImageIO.read(new File(ClHandler.getTempDumpAddress()));

        editChoice = ClHandler.getEditChoice();

        imgHeight = buffImage.getHeight();
        imgWidth = buffImage.getWidth();

        for(int i=0;i<imgWidth;i++){

            for(int j=0;j<imgHeight;j++){

                data = buffImage.getRGB(i,j);
                processData();
                buffImage.setRGB(i,j,data);
                
            }
        }

        System.out.println("Image is edited on main memory");

        try{

            System.out.println("writing down the image");

            ImageIO.write(buffImage,"jpg",new File(ClHandler.getTempDumpAddress()));

            System.out.println("wrote down the edited image");

        }

        catch(Exception ex){

            System.out.println("ImageIO.write Exception :: "+ex.getMessage());
            ex.printStackTrace();
        }

            
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

                if(ClHandler == null)Thread.sleep(Long.MAX_VALUE);

                else {

                    try{

                        process();
                    
                    }

                    catch(Exception ex){

                        System.out.println("Exception@ImageProcessor.run :: "+ex.getMessage());
                        ex.printStackTrace();
                    }

                    finally{

                        System.out.println("Image Processor is interrupting Client Handler");

                        ClHandler.interrupt();
                        ClHandler = null;
                        poolManager.free(index);
                        resManager.interrupt();

                    }
                }

            }

            catch(Exception ex){

                System.out.println("Exception@ImageProcessor.run :: "+ex.getMessage());

            }
        }
    }

}