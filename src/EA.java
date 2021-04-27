import java.util.ArrayList;

public class EA extends Algorithm{
	
	ArrayList<Individual> population;
	
	@Override
	public void run() {
		population = initialise();									
		int improv = 0;
		running = true;
		
		String[] filenames = FileGetter.getFileNames("", "", ".ind");
		if(seed && filenames != null && filenames.length > 0) {
			String filename = filenames[filenames.length - 1];		
			Individual ind = Serialize.load(filename);
			ArrayList<Individual> seeds = new ArrayList<>();
			seeds.add(ind);
			replace(seeds);
		}
		best = getBest();
		int optimal = Individual.getOptimal().getFitness();
		while(running) {			
			ArrayList<Individual> parents = select();			
			ArrayList<Individual> children = crossoverUniform(parents);
			children = mutate(children);
			evaluate(children);
			replace(children);
			Individual generationBest = getBest();			
			if(generationBest.evaluateFitness() > best.getFitness()) {
				best = generationBest;				
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
			System.out.println(Individual.getEvaluations() + "\t" + best.getFitness() + "\t" + optimal);			
		}
	}

	private void evaluate(ArrayList<Individual> children) {
		for(Individual ind : children) {
			ind.evaluateFitness();
		}
	}

	/**
	 * One point crossover;
	 * @param parents
	 * @return
	 */
	
	private ArrayList<Individual> crossoverOnePoint(ArrayList<Individual> parents) {
		if(parents == null || parents.size() < 2) {
			return null;
		}
		if(parameters.getRandon().nextDouble() > parameters.crossoverProbability) {
			return parents;
		}
		ArrayList<Individual> children = new ArrayList<>();
		int yCut = parameters.getRandon().nextInt(parameters.getHeight());
		int xCut = parameters.getRandon().nextInt(parameters.getHeight());
		Individual child = new Individual();
		for(int y = 0; y < parameters.getHeight(); y++){
        	for (int x = 0; x < parameters.getWidth(); x++){
        		if(y < yCut) {
        			child.getChromosome()[y][x] = parents.get(0).getChromosome()[y][x];
        		}else if(y == yCut && x < xCut) {
        			child.getChromosome()[y][x] = parents.get(0).getChromosome()[y][x];
        		}else {
        			child.getChromosome()[y][x] = parents.get(1).getChromosome()[y][x];
        		}
        	}
		}    
		children.add(child);
        return children;
	}
	
	private ArrayList<Individual> crossoverUniform(ArrayList<Individual> parents) {
		if(parents == null || parents.size() < 2) {
			return null;
		}
		if(parameters.getRandon().nextDouble() > parameters.crossoverProbability) {
			return parents;
		}
		ArrayList<Individual> children = new ArrayList<>();		
		Individual child = new Individual();
		for(int y = 0; y < parameters.getHeight(); y++){
        	for (int x = 0; x < parameters.getWidth(); x++){
        		
        		if(parameters.getRandon().nextBoolean()) {
        			child.getChromosome()[y][x] = parents.get(0).getChromosome()[y][x];
        		}else {
        			child.getChromosome()[y][x] = parents.get(1).getChromosome()[y][x];
        		}
        	}
		}    
		children.add(child);
        return children;
	}
	

	/**
	 * Tournament for two parents
	 * @return
	 */
	private ArrayList<Individual> select() {
		ArrayList<Individual> parents = new ArrayList<>();
		for(int i = 0; i < 2; i++) {
			Individual winner = population.get(parameters.getRandon().nextInt(population.size()));
			for(int j = 1; j < parameters.tournamentSize; j++) {
				Individual candidate = population.get(parameters.getRandon().nextInt(population.size()));
				if(candidate.getFitness() > winner.getFitness()) {
					winner = candidate;
				}
			}
			parents.add(winner.copy());
		}
		return parents;
	}

	private void replace(ArrayList<Individual> children) {
		for(Individual child : children) {
			Individual worst = null;
			for(Individual individual : population) {
				if(worst == null || individual.getFitness() < worst.getFitness()) {
					worst = individual;
				}
			}
			if(worst != null && child.getFitness() > worst.getFitness()) {
				population.set(population.indexOf(worst), child);
			}
		}
	}

	private Individual getBest() {
		if(population == null || population.size() == 0) {
			return null;
		}
		Individual theBest = null;
		for(Individual individual : population) {
			if(theBest == null || individual.getFitness() > theBest.getFitness()) {
				theBest = individual;
			}
		}
		return theBest.copy();
	}

	private ArrayList<Individual> initialise() {
		population = new ArrayList<>();
		for(int i = 0; i < parameters.popSize; i++) {
			Individual individual = new Individual();
			population.add(individual);
		}
		return population;
	}

	private ArrayList<Individual> mutate(ArrayList<Individual> children) {		
		if(parameters.getRandon().nextDouble() > parameters.mutationProbability) {
			return children;
		}
		for(Individual child : children) {
			for(int y = 0; y < parameters.getHeight(); y++){
	        	for (int x = 0; x < parameters.getWidth(); x++){
	        		if(parameters.getRandon().nextDouble() > parameters.mutationRate) {
	        			child.getChromosome()[y][x] = !child.getChromosome()[y][x];
	        		}
	        	}
			}		
		}
		return children;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return running;
	}
	
	public void stop() {
		running = false;
	}

}
