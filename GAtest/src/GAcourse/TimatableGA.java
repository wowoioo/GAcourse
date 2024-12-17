package GAcourse;

public class TimatableGA {
	public static  int maxGenerations = 1000;

	private static Timetable initializeTimetable() {
		Timetable timetable = new Timetable();
		timetable.addRoom(0, "A0", 30);
		timetable.addRoom(1, "A1", 15);
		timetable.addRoom(2, "B1", 30);
		timetable.addRoom(3, "D11", 20);
		timetable.addRoom(4, "D12", 20);
		timetable.addRoom(5, "D13", 20);
		timetable.addRoom(6, "D14", 20);
		timetable.addRoom(7, "F10", 25);
		timetable.addRoom(8, "F11", 25);
		timetable.addRoom(9, "F12", 25);
		timetable.addRoom(10, "F13", 25);
		timetable.addRoom(11, "F14", 25);
		timetable.addRoom(12, "F15", 25);
		timetable.addRoom(13, "F16", 25);
		timetable.addRoom(14, "F17", 25);
		timetable.addRoom(15, "F18", 25);

		timetable.addTimeslot(1, "2022/2/7 9:00 - 17:00");
		timetable.addTimeslot(2, "2022/2/8 9:00 - 17:00");
		timetable.addTimeslot(3, "2022/2/9 9:00 - 17:00");
		timetable.addTimeslot(4, "2022/2/10 9:00 - 17:00");
		timetable.addTimeslot(5, "2022/2/11 9:00 - 17:00");
		timetable.addTimeslot(6, "2022/2/12 9:00 - 17:00");
		timetable.addTimeslot(7, "2022/2/13 9:00 - 17:00");
		timetable.addTimeslot(8, "2022/2/14 9:00 - 17:00");
		timetable.addTimeslot(9, "2022/2/15 9:00 - 17:00");
		timetable.addTimeslot(10, "2022/2/16 9:00 - 17:00");
		timetable.addTimeslot(11, "2022/2/17 9:00 - 17:00");
		timetable.addTimeslot(12, "2022/2/18 9:00 - 17:00");
		timetable.addTimeslot(13, "2022/2/19 9:00 - 17:00");
		timetable.addTimeslot(14, "2022/2/20 9:00 - 17:00");
		timetable.addTimeslot(15, "2022/2/21 9:00 - 17:00");

		timetable.addProfessor(-1, " - ");
		timetable.addProfessor(1, "Lecturer1");
		timetable.addProfessor(2, "Lecturer2");
		timetable.addProfessor(3, "Lecturer3");
		timetable.addProfessor(4, "Lecturer4");
		timetable.addProfessor(5, "Lecturer5");
		timetable.addProfessor(6, "Lecturer6");
		timetable.addProfessor(7, "Lecturer7");
		timetable.addProfessor(8, "Lecturer8");
		timetable.addProfessor(9, "Lecturer9");

		timetable.addModule(1, "PA2", "CC2", "Course1", new int[]{1, 2, 3, 4, 5}, "S1", "Lecturer1", "Grad Cert 1", 1);
		timetable.addModule(2, "PA2", "CC3", "Course2", new int[]{1, 3, 4, 5, 7, 8, 9}, "S2", "Lecturer1", "Grad Cert 1", 1);
		timetable.addModule(3, "PA2", "CC4", "Course3", new int[]{1, 2, 6, 8, 9}, "-", "Lecturer1", "Grad Cert 1", 1);
		timetable.addModule(4, "PA2", "CC5", "Course4", new int[]{3, 4, 5, 6}, "-", "Lecturer1", "Grad Cert 1", 1);
		timetable.addModule(5, "PA2", "CC6", "Course5", new int[]{1, 2, 3}, "-", "Lecturer1", "Grad Cert 1", 2);
		timetable.addModule(6, "PA2", "CC7", "Course6", new int[]{1, 4, 7, 8}, "S3", "Lecturer1", "Grad Cert 1", 1);

		timetable.addCohort(1, 10, 1, new int[]{1, 3, 4});
		timetable.addCohort(2, 30, 2, new int[]{2, 3, 5, 6});
		timetable.addCohort(3, 18, 2, new int[]{3, 4, 5});
		timetable.addCohort(4, 25, 3, new int[]{1, 4});
		timetable.addCohort(5, 20, 4, new int[]{2, 3, 5});
		timetable.addCohort(6, 22, 5, new int[]{1, 4, 5});
		timetable.addCohort(7, 16, 5, new int[]{1, 3});
		timetable.addCohort(8, 18, 6, new int[]{2, 6});
		timetable.addCohort(9, 24, 7, new int[]{1, 6});
		timetable.addCohort(10, 25, 8, new int[]{3, 4});

		return timetable;
	}
	public static void main(String[] args) {
		Timetable timetable = initializeTimetable();

		GA ga = new GA(100, 0.001, 0.98, 2, 5);

		Population population = ga.initPopulation(timetable);
		
		int generation = 1;

		while(ga.isTerminationConditionMet1(population) == false && ga.isTerminationconditionMet2(generation, maxGenerations) == false) {
			System.out.println("G" + generation + "Best fitness:" + population.getFittest(0).getFitness());
			population = ga.crossoverPopulation(population);
			population = ga.mutatePopulation(population, timetable);
			ga.evalPopulation(population, timetable);
			generation++;
		}

		timetable.createClasses(population.getFittest(0));
		System.out.println();
		System.out.println("Solution found in " + generation + " generations");
		System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
		System.out.println("Clashes: " + timetable.calcClashes());

		System.out.println();
		Class classes[] = timetable.getClasses();

		int classIndex = 1;
		for (Class bestClass : classes) {
			System.out.println("Class " + classIndex + ":");
			System.out.println("Module: " + timetable.getModule(bestClass.getModuleId()).getModuleName());
			System.out.println("Cohort: " + timetable.getCohort(bestClass.getCohortId()).getCohortId());
			System.out.println("Room: " + timetable.getRoom(bestClass.getRoomId( )).getRoomNumber( ));
			if (timetable.getModule(bestClass.getModuleId()).getProfessorNum() == 1) {
				System.out.println("Professor1: " + timetable.getProfessor(bestClass.getProfessor1Id()).getProfessorName());
				System.out.println("Professor2: - ");
				System.out.println("Professor3: - ");
			} else if (timetable.getModule(bestClass.getModuleId()).getProfessorNum() == 2) {
				System.out.println("Professor1: " + timetable.getProfessor(bestClass.getProfessor1Id()).getProfessorName());
				System.out.println("Professor2: " + timetable.getProfessor(bestClass.getProfessor2Id()).getProfessorName());
				System.out.println("Professor3: - ");
			} else if (timetable.getModule(bestClass.getModuleId()).getProfessorNum() == 3) {
				System.out.println("Professor1: " + timetable.getProfessor(bestClass.getProfessor1Id()).getProfessorName());
				System.out.println("Professor2: " + timetable.getProfessor(bestClass.getProfessor2Id()).getProfessorName());
				System.out.println("Professor3: " + timetable.getProfessor(bestClass.getProfessor3Id()).getProfessorName());
			}
			System.out.println("Time: " + timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot());
			System.out.println("-----");
			classIndex++;
		}
	}
}
