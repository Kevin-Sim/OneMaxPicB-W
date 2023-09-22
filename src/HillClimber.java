import java.awt.Point;
import java.util.ArrayList;

public class HillClimber extends  Algorithm{

	boolean[][] alreadyChecked = new boolean[parameters.getWidth()][parameters.getHeight()];
	ArrayList<Point> possiblePoints;
	long start = System.currentTimeMillis();
	public HillClimber() {
		super();
		 possiblePoints = new ArrayList();
		 for (int y = 0; y < parameters.getHeight(); y++) {
				for (int x = 0; x < parameters.getWidth(); x++) {
					Point p = new Point(x, y);
					possiblePoints.add(p);
				}			
		 }
	}
	@Override
	public void run() {
		best = new Individual();	
		startingFitness = best.getFitness();
		
		int improv = 0;;
		running = true;
		
		String[] filenames = FileGetter.getFileNames("", "", ".ind");
		if(seed && filenames != null && filenames.length > 0) {
			String filename = filenames[filenames.length - 1];		
			best = Serialize.load(filename);
		}
		int optimal = Individual.getOptimal().getFitness();
		while(running) {			
			Individual candidate = mutate2(best.copy());
			if(candidate.evaluateFitness() > best.getFitness()) {
				best = candidate;				
				if(best.getFitness() == optimal) {					
					running = false;
				}				
				improv++;
				if(improv % 25 == 0) {
					Serialize.save(best, System.currentTimeMillis() + ".ind");
				}
			}
			setChanged();
			notifyObservers(best);
			System.out.println(Individual.getEvaluations() + "\t" + best.getFitness() + "\t" + optimal);			
		}
		System.out.println("optimal in " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
	}

	private Individual mutate(Individual candidate) {
		boolean found = false;
		while(!found) {			
			int x = parameters.getRandom().nextInt(parameters.getWidth());
			int y = parameters.getRandom().nextInt(parameters.getHeight());
			if(alreadyChecked[x][y]) {
				continue;
			}
			candidate.getChromosome()[y][x] = !candidate.getChromosome()[y][x];
			alreadyChecked[x][y] = true;
			found = true;
		}
		return candidate;
	}

	private Individual mutate2(Individual candidate) {
		Point p = possiblePoints.remove(parameters.getRandom().nextInt(possiblePoints.size()));
		candidate.getChromosome()[p.y][p.x] = !candidate.getChromosome()[p.y][p.x];
		return candidate;
	}
}
