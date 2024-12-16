package GAtest;

public class AllOnesGA {
	public static void main(String[] args) {
		GA ga = new GA(100, 0.001, 0.98, 2);
		Population population = ga.initPopulation(50);
		ga.evalPopulation(population);
		int generation = 1;

		while(ga.isTerminationConditionMet(population) == false) {
			System.out.println("Best solution:" + population.getFittest(0).toString());
			population = ga.crossoverPopulation(population);
			population = ga.mutatePopulation(population);
			ga.evalPopulation(population);
			generation++;
		}
		System.out.println("Found solution in " + generation + "generations");
		System.out.println("Best solution:" + population.getFittest(0).toString());
	}
}
