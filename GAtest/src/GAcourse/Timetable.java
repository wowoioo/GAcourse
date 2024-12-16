package GAcourse;

import java.util.HashMap;

public class Timetable {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Professor> professors;
    private final HashMap<Integer, Module> modules;
    private final HashMap<Integer, Cohort> cohorts;
    private final HashMap<Integer, Timeslot> timeslots;
    private Class classes[];

    private int numClasses = 0;

    public Timetable() {
        this.rooms = new HashMap<Integer, Room>();
        this.professors = new HashMap<Integer, Professor>();
        this.modules = new HashMap<Integer, Module>();
        this.cohorts = new HashMap<Integer, Cohort>();
        this.timeslots = new HashMap<Integer, Timeslot>();
    }

    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.professors = cloneable.getProfessors();
        this.modules = cloneable.getModules();
        this.cohorts = cloneable.getCohorts();
        this.timeslots = cloneable.getTimeslots();
    }

    private HashMap<Integer, Professor> getProfessors() {
        return this.professors;
    }

    private HashMap<Integer, Module> getModules() {
        return this.modules;
    }

    private HashMap<Integer, Cohort> getCohorts() {
        return this.cohorts;
    }

    private HashMap<Integer, Timeslot> getTimeslots() {
        return this.timeslots;
    }

    public void addRoom(int roomId, String roomName, int capacity) {
        this.rooms.put(roomId, new Room(roomId, roomName, capacity));
    }

    public void addProfessor(int professorId, String professorName) {
        this.professors.put(professorId, new Professor(professorId, professorName));
    }

    public void addModule(int moduleId, String practiceArea, String moduleCode, String module, int professorIds[], String software, String courseManager, String gradCert, int professorNum) {
        this.modules.put(moduleId, new Module(moduleId, practiceArea, moduleCode, module, professorIds, software, courseManager, gradCert, professorNum));
    }

    public void addCohort(int cohortId, int cohortSize,int typeId, int moduleIds[]) {
        this.cohorts.put(cohortId, new Cohort(cohortId, cohortSize, typeId, moduleIds));
        this.numClasses = 0;
    }

    public void addTimeslot(int timeslotId, String timeslot) {
        this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
    }

    public void createClasses(Individual individual) {
        Class classes[] = new Class[this.getNumClasses()];

        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int classIndex = 0;

        for (Cohort cohort : this.getCohortsAsArray()) {
            int moduleIds[] = cohort.getModuleIds();
            for (int moduleId : moduleIds) {
                classes[classIndex] = new Class(classIndex, cohort.getCohortId(), moduleId);
                classes[classIndex].addTimeslot(chromosome[chromosomePos]);
                chromosomePos++;

                classes[classIndex].setRoomId(chromosome[chromosomePos]);
                chromosomePos++;
                
                if(this.getModule(classes[classIndex].getModuleId()).getProfessorNum() == 1) {
                	classes[classIndex].addProfessor1(chromosome[chromosomePos]);
                	chromosomePos++;
                }else if (this.getModule(classes[classIndex].getModuleId()).getProfessorNum() == 2) {
                	classes[classIndex].addProfessor1(chromosome[chromosomePos]);
                	chromosomePos++;
                	classes[classIndex].addProfessor2(chromosome[chromosomePos]);
                	chromosomePos++;
                }else if (this.getModule(classes[classIndex].getModuleId()).getProfessorNum() == 3) {
                	classes[classIndex].addProfessor1(chromosome[chromosomePos]);
                	chromosomePos++;
                	classes[classIndex].addProfessor2(chromosome[chromosomePos]);
                	chromosomePos++;
                	classes[classIndex].addProfessor3(chromosome[chromosomePos]);
                	chromosomePos++;
                }
                
                classIndex++;
            }
        }
        this.classes = classes;
    }

    public Room getRoom(int roomId) {
        if (!this.rooms.containsKey(roomId)) {
            System.out.println("Rooms doesn't contain key " + roomId);
        }
        return (Room) this.rooms.get(roomId);
    }

    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    public Room getRandomRoom() {
    Object[] roomsArray = this.rooms.values().toArray( );
    Room room = (Room) roomsArray[(int) (roomsArray.length * Math.random())];
    return room;
    }

    public Professor getProfessor(int professorId) {
        return (Professor) this.professors.get(professorId);
    }

    public Module getModule(int moduleId) {
        return (Module) this.modules.get(moduleId);
    }

    public int[] getCohortModules(int cohortId) {
        Cohort cohort = (Cohort) this.cohorts.get(cohortId);
        return cohort.getModuleIds();
    }

    public Cohort getCohort(int cohortId) {
        return (Cohort) this.cohorts.get(cohortId);
    }

    public Cohort[] getCohortsAsArray() {
        return (Cohort[]) this.cohorts.values().toArray(new Cohort[this.cohorts.size()]);
    }

    public Timeslot getTimeslot(int timeslotId) {
        return (Timeslot) this.timeslots.get(timeslotId);
    }

    public Timeslot getRandomTimeslot() {
        Object[] timeslotArray = this.timeslots.values().toArray();
        Timeslot timeslot = (Timeslot) timeslotArray[(int) (timeslotArray.length * Math.random())];
        return timeslot;
    }

    public Class[] getClasses() {
        return this.classes;
    }

    public int getNumClasses() {
        if (this.numClasses > 0){
            return this.numClasses;
        }

        int numClasses = 0;
        Cohort cohorts[] = (Cohort[]) this.cohorts.values().toArray(new Cohort[this.cohorts.size()]);

        for (Cohort cohort : cohorts) {
            numClasses += cohort.getModuleIds().length;
        }
        this.numClasses = numClasses;

        return this.numClasses;
    }

    public int calcClashes() {
        int clashes = 0;
        for (Class classA : this.classes) {
            int roomCapacity = this.getRoom(classA.getRoomId()).getRoomCapacity();
            int cohortSize = this.getCohort(classA.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                clashes++;
            }

            for (Class classB : this.classes) {
                if (classA.getRoomId() == classB.getRoomId() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                    clashes++;
                    break;
                }
            }
            if (this.getModule(classA.getModuleId()).getProfessorNum() == 1) {
                for (Class classB : this.classes) {
                    if (this.getModule(classB.getModuleId()).getProfessorNum() == 1) {
                        if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        }
                    } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 2) {
                        if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        }
                    } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 3) {
                        if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        } else if (classA.getProfessor1Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                            clashes++;
                            break;
                        }
                    }
                }
            } else if (this.getModule(classA.getModuleId()).getProfessorNum() == 2) {
                if (classA.getProfessor1Id() == classA.getProfessor2Id()) {
                    clashes++;
                    break;
                } else {
                    for (Class classB : this.classes) {
                        if (this.getModule(classB.getModuleId()).getProfessorNum() == 1) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 2) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 3) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        }
                    }
                }
            } else if (this.getModule(classA.getModuleId()).getProfessorNum() == 3) {
                if (classA.getProfessor1Id() == classA.getProfessor2Id()) {
                    clashes++;
                    break;
                } else if (classA.getProfessor1Id() == classA.getProfessor3Id()) {
                    clashes++;
                    break;
                } else if (classA.getProfessor2Id() == classA.getProfessor3Id()) {
                    clashes++;
                    break;
                } else {
                    for (Class classB : this.classes) {
                        if (this.getModule(classB.getModuleId()).getProfessorNum() == 1) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 2) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        } else if (this.getModule(classB.getModuleId()).getProfessorNum() == 3) {
                            if (classA.getProfessor1Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor1Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor2Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor1Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor2Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            } else if (classA.getProfessor3Id() == classB.getProfessor3Id() && classA.getTimeslotId() == classB.getTimeslotId() && classA.getClassId() != classB.getClassId()) {
                                clashes++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return clashes;
    }
}