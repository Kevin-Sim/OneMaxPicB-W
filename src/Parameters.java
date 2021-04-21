
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import java.util.Random;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import static marvin.MarvinPluginCollection.*;


public class Parameters {

	private static Random rnd = new Random();
	private static int width; 
	private static int height;
	private static BufferedImage image = null;
//	public String imageFilename = "./pics/evolution1000x563.jpg";
	private static String imageFilename = "./pics/evolution1000x563.jpg";
	private static Parameters parameters = null;
	
	public static void main(String[] args) {
		parameters = new Parameters();
	}
	private Parameters() {
		image = getImage();		
	}	
	
	public BufferedImage getImage(){
		if(image == null){
			MarvinImage original = MarvinImageIO.loadImage(imageFilename);
	        MarvinImage output = original.clone();        
	        halftoneErrorDiffusion(original, output);
	        MarvinImageIO.saveImage(output, "halftone.png");
	//        image = new BufferedImage(output.getWidth(), output.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
	        image = output.getBufferedImage();
	        width = image.getWidth();
			height = image.getHeight();	
	        StringBuilder sb = new StringBuilder();
			for(int y = 0; y < height; y++){
	        	for (int x = 0; x < width; x++){
	        		if(image.getRGB(x, y) == Color.WHITE.getRGB()){
	        			sb.append(" ");
	        		}else if (image.getRGB(x, y) == Color.BLACK.getRGB()){
	        			sb.append("o");
	        		}else{
	        			System.err.println("Color not allowed");
	        		}
	        		
	        	}
	        	sb.append("\r\n");
	        }       
	        try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("ascii.txt")));
				writer.write(sb.toString());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return image;
	}
	public static Parameters getInstance() {
		if(parameters == null){
			parameters = new Parameters();
		}
		return parameters;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public String getImageFilename() {
		return imageFilename;
	}
	public Random getRandon() {		
		return rnd;
	}
}
