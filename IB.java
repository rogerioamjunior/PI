

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IB {
   public static int pixelsubst(int a,int b,int c,int d, double x_diff, double y_diff){
        int result, green,red,blue, alpha;
   
        //blue element
        // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
        blue = (int)((a&0xff)*(1-x_diff)*(1-y_diff) + (b&0xff)*(x_diff)*(1-y_diff) +
                (c&0xff)*(y_diff)*(1-x_diff) + (d&0xff)*(x_diff*y_diff));

        // green element
        // Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
        green =(int)( ((a>>8)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>8)&0xff)*(x_diff)*(1-y_diff) +
                ((c>>8)&0xff)*(y_diff)*(1-x_diff)   + ((d>>8)&0xff)*(x_diff*y_diff));

        // red element
        // Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
        red = (int)(((a>>16)&0xff)*(1-x_diff)*(1-y_diff) + ((b>>16)&0xff)*(x_diff)*(1-y_diff) +
                ((c>>16)&0xff)*(y_diff)*(1-x_diff)   + ((d>>16)&0xff)*(x_diff*y_diff));

        result = 0xff000000 | //  alpha
                    ((((int)red)<<16)&0xff0000) |
                    ((((int)green)<<8)&0xff00) |
                    ((int)blue) ;
        return result;
    }  
   public static int[][] resizePixels(int[][] pixels,int linhas,int colunas){
        int[][] temp = new int[colunas][linhas] ;
        int i, j,a,b,c,d;
        i=0;
        double x_ratio = (pixels[0].length -1)/(double)linhas;
        double y_ratio = (pixels.length-1)/(double)colunas;
       
        double x_diff, y_diff;

        int k = 0,l = 0;
        int x,y;  
        while(i<linhas){
            j = 0;
            while(j<colunas){
                x = (int)(x_ratio * j) ;
                y = (int)(y_ratio * i) ;
                x_diff = (x_ratio * j) - x ;
                y_diff = (y_ratio * i) - y ;

                a = pixels[x][y] ;
                b = pixels[x][y+1] ;
                c = pixels[x+1][y];
                d = pixels[x+1][y+1];
                
                temp[l++][k] = pixelsubst(a, b, c, d,x_diff,y_diff);
                j++;
                
           }
           l=0;
           k++;
           i++;
       }
       return temp ;
	    
   }
    
   public static int[] toArray(int[][] mO){ // mO: Matriz Original
		int[] retorno = new int[mO.length*mO[0].length];
		for (int i = 0; i < mO.length; i++) 
			for (int j = 0; j < mO[0].length; j++)
				retorno[(i*mO[0].length)+j] = mO[i][j];
		return retorno;
	}
   
   
   public static int[][] getMatrix(BufferedImage input){
		int height = input.getHeight(); 
		int width = input.getWidth();
		int [][] array = new int[height][width];
		for(int i=0; i<height; i++)
                    for(int j=0; j<width; j++)
			array[i][j] = input.getRGB(j, i);
		return array;
	}
   public static void main(String[] args) throws IOException{
        BufferedImage imagem = ImageIO.read(new File("Lenna.jpg"));
        int linhas = imagem.getWidth();
	int colunas = imagem.getHeight();
        
        int[][] matriz = getMatrix(imagem);
        
        int lD = linhas*2;	//	Tamanho das linhas desejadas
	int cD = colunas*2;	//	Tamanho das colunas desejadas
      
        int[][] resultante = resizePixels(matriz, lD, cD);
        
        BufferedImage bufferedImage = new BufferedImage(lD, cD, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, lD, cD, toArray(resultante), 0, lD);
        ImageIO.write(bufferedImage, "jpg", new File("teste.jpg"));

        System.out.println("Pronto!");
    }
}
