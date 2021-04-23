import java.util.Observable;


public class HillClimber extends Observable implements Runnable {

	Parameters parameters = Parameters.getInstance();
	private boolean running;
	
	
	@Override
	public void run() {
		Individual best = new Individual();		
		int generation = 0;
		int improv = 0;;
		running = true;
		
		String[] filenames = FileGetter.getFileNames("", "", ".ind");
		if(filenames != null && filenames.length > 0) {
			String filename = filenames[filenames.length - 1];		
			best = Serialize.load(filename);
		}
		
		int optimal = Individual.getOptimal().getFitness();
		while(running) {
			generation++;
			Individual candidate = mutate(best.copy());
			if(candidate.evaluateFitness() > best.getFitness()) {
				best = candidate;				
				if(best.getFitness() == optimal) {					
					running = false;
				}
				setChanged();
				notifyObservers(best);
				improv++;
				if(improv % 250 == 0) {
					Serialize.save(best, System.currentTimeMillis() + ".ind");
				}
			}
			System.out.println(generation + "\t" + best.getFitness() + "\t" + optimal);			
		}
	}

	private Individual mutate(Individual candidate) {
		int x = parameters.getRandon().nextInt(parameters.getWidth());
		int y = parameters.getRandon().nextInt(parameters.getHeight());		
		candidate.getChromosome()[y][x] = !candidate.getChromosome()[y][x]; 		
		return candidate;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return running;
	}
	
	public void stop() {
		running = false;
	}

}
