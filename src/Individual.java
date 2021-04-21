import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;




public class Individual implements Serializable{

	private static final long serialVersionUID = 1L;	
	private transient static Parameters parameters = Parameters.getInstance();
	private transient static int width = parameters.getWidth();
	private transient static int height = parameters.getHeight();
	private transient static BufferedImage image = parameters.getImage();
	private transient static Random rnd = parameters.getRandon();
	
	private static String imageFilename = parameters.getImageFilename();
	private boolean[][] chromosome = new boolean[height][width];	
	private int fitness;	
	
	public Individual() {
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(rnd.nextBoolean()){
        			chromosome[y][x] = true;
        		}
        	}
		}
	}
	
	

	/**
	 * 
	 */
	
	/**
	 * 
	 */
	public void evaluateFitness() {
//		BufferedImage image = new BufferedImage(parameters.width, parameters.height, BufferedImage.TYPE_4BYTE_ABGR);
//		Graphics2D g2 = image.createGraphics();		
//		draw(g2, 1);
//				
//		int cellSize = parameters.cellSize;
//		int fAll = 0;
//		for(int xStart = 0; xStart < parameters.width; xStart+= cellSize){
//			for(int yStart = 0; yStart  < parameters.height; yStart+= cellSize){
//				int b = 0;
//				int g = 0;
//				int r = 0;
//				int a = 0;
//				int n = 0;
//				int xEnd = xStart + cellSize;
//				if(xEnd > width){
//					xEnd = width;
//				}
//				int yEnd = yStart + cellSize;
//				if(yEnd > height){
//					yEnd = height;
//				}
//				for(int x = xStart; x < xEnd; x++){
//					for(int y = yStart; y < yEnd; y++){
//						n++;
//						int rgb = image.getRGB(x, y);
//						a += (rgb>>24)&0xFF;
//						r += (rgb>>16)&0xFF;// * a / 255;
//						g += (rgb>>8)&0xFF;// * a / 255;
//						b += (rgb)&0xFF;// * a / 255;						
//					}
//				}				
//				b = (int) (b / n);
//				g = (int) (g / n);
//				r = (int) (r / n);
//				a = (int) (a / n);
//
//				int b1 = 0;
//				int g1 = 0;
//				int r1 = 0;
//				int a1 = 0;				
//				for(int x = xStart; x < xEnd; x++){
//					for(int y = yStart; y < yEnd; y++){
//						int rgb = parameters.image.getRGB(x, y);						
//						a1 += (rgb>>24)&0xFF;
//						r1 += (rgb>>16)&0xFF;
//						g1 += (rgb>>8)&0xFF;
//						b1 += (rgb)&0xFF;												
//					}
//				}
//				
//				b1 = (int) (b1 / n);
//				g1 = (int) (g1 / n);
//				r1 = (int) (r1 / n);
//				a1 = (int) (a1 / n);
//				
//				//int fCell = (int) Math.abs(b1 - (b * new Double(b) * 255 / a));
//				
//				double fCell = Math.sqrt(Math.abs(b1*b1 - b*b));				
//				fCell+= Math.sqrt(Math.abs(g1*g1 - g*g));
//				fCell+= Math.sqrt(Math.abs(r1*r1 - r*r));
////				fCell+= Math.abs(a1 - a);
//				fAll+= fCell;
////				System.out.println("" + r + ", " + g + ", " + b + ", " + a + ", ");				
//			}
//		}		
//		g2.dispose();			
//		fitness = new Double(fAll);// / parameters.size * parameters.size;//60 * 60
	}

	public void draw(Graphics g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.WHITE);
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(chromosome[y][x]){
        			g.fillRect(x, y, 1, 1);
        		}
        	}
		}
	}
	
	
	


	public Individual copy() {
		Individual individual = new Individual();
		individual.fitness = this.fitness;
		
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		individual.chromosome[y][x] = chromosome[y][x];
        	}
		}
		return individual;
	}

	public static Individual getOptimal(){
		Individual ind = new Individual();
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(image.getRGB(x, y) == Color.WHITE.getRGB()){
        			ind.chromosome[y][x] = true;
        		}else{
        			ind.chromosome[y][x] = false;
        		}
        	}
		}
		return ind;
	}
	
	public BufferedImage getImageFromChromosome(){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(chromosome[y][x]){
        			image.setRGB(x, y, Color.WHITE.getRGB());
        		}else{
        			image.setRGB(x, y, Color.BLACK.getRGB());
        		}
        	}
		}
		return image;
	}
}
