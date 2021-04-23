
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
//	private static String imageFilename = "./pics/evolution1000x563.jpg";
	private static String imageFilename = "./pics/me.jpg";
	private static Parameters parameters = null;
	public static boolean threshold = false; 
	public static int thresholdValue = 100;	
	
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
	        if(!threshold) {
		        halftoneErrorDiffusion(original, output);
		        MarvinImageIO.saveImage(output, "halftone.png");
	        }else {	        
		        thresholding(original, output, thresholdValue);	        
		        MarvinImageIO.saveImage(output, "threshold.png");
	        }
	        	        
	        image = output.getBufferedImage();	       	        
	        width = image.getWidth();
			height = image.getHeight();		
			
			
			Individual ind = Individual.getOptimal();
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("ascii.txt")));
				writer.write(ind.getAsciiFromChromosome());
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
	
	public void setImageFilename(String imageFilename) {
		Parameters.imageFilename = imageFilename;
		image = null;
		image = getImage();
	}
}
