import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;




public class Individual implements Serializable{

	private static final long serialVersionUID = 1L;	
	private transient static Parameters parameters;
	private transient static int width;
	private transient static int height; 
	private transient static BufferedImage image;
	private transient static Random rnd;
	
	private String imageFilename;
	private boolean[][] chromosome;	
	private int fitness;	
	
	public Individual() {
		parameters = Parameters.getInstance();
		image = parameters.getImage();
		width = parameters.getWidth();
		height = parameters.getHeight();
		rnd = parameters.getRandon();
		imageFilename = parameters.getImageFilename();
		chromosome = new boolean[height][width];
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
//        		chromosome[y][x] = true;
        		if(rnd.nextBoolean()){
        			chromosome[y][x] = true;
        		}else {
        			
        		}
        	}
		}
		evaluateFitness();
	}
	
	
	public int evaluateFitness() {
		fitness = 0;
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(chromosome[y][x] && image.getRGB(x, y) == Color.WHITE.getRGB()){
        			fitness++;
        		}else if (!chromosome[y][x] && image.getRGB(x, y) == Color.BLACK.getRGB()) {
        			fitness++;
        		}
        	}
		}
		return fitness;
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
		for(int y = 0; y < ind.height; y++){
        	for (int x = 0; x < ind.width; x++){
        		if(ind.image.getRGB(x, y) == Color.WHITE.getRGB()){
        			ind.chromosome[y][x] = true;
        		}else{
        			ind.chromosome[y][x] = false;
        		}
        	}
		}
		ind.evaluateFitness();
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
	
	public String getAsciiFromChromosome(){
		StringBuilder sb = new StringBuilder();
		String c = "#";
		for(int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){
        		if(image.getRGB(x, y) == Color.WHITE.getRGB()){
        			sb.append(" ");
        		}else if (image.getRGB(x, y) == Color.BLACK.getRGB()){
        			
        			sb.append(c);
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
		return sb.toString();
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public boolean[][] getChromosome() {
		return chromosome;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static int getWidth() {
		return width;
	}


	public void resetTransientFields() {
		parameters = Parameters.getInstance();
		parameters.setImageFilename(imageFilename);
		image = parameters.getImage();
		width = parameters.getWidth();
		height = parameters.getHeight();
		rnd = parameters.getRandon();
	}
	
}
