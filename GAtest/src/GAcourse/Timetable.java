package GAcourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timetable {
    private final HashMap<Integer, Room> rooms;
    private final HashMap<Integer, Professor> professors;
    private final HashMap<Integer, Course> courses;
    private final HashMap<Integer, Cohort> cohorts;
    private final HashMap<Integer, Timeslot> timeslots;
    private TeachingPlan plans[];

    private int plansNum = 0;

    public Timetable() {
        this.rooms = new HashMap<Integer, Room>();
        this.professors = new HashMap<Integer, Professor>();
        this.courses = new HashMap<Integer, Course>();
        this.cohorts = new HashMap<Integer, Cohort>();
        this.timeslots = new HashMap<Integer, Timeslot>();
    }

    //浅拷贝，用于适应度计算
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.professors = cloneable.getProfessors();
        this.courses = cloneable.getCourses();
        this.cohorts = cloneable.getCohorts();
        this.timeslots = cloneable.getTimeslots();
    }

    private HashMap<Integer, Professor> getProfessors() {
        return this.professors;
    }

    private HashMap<Integer, Course> getCourses() {
        return this.courses;
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

    public void addCourse(int courseId, String practiceArea, String courseCode, String course, int professorIds[], String software, String courseManager, String gradCert, int professorNum, int duration, String run) {
        this.courses.put(courseId, new Course(courseId, practiceArea, courseCode, course, professorIds, software, courseManager, gradCert, professorNum, duration, run));
    }

    public void addCohort(int cohortId, int cohortSize,int typeId, String cohortType, int courseIds[]) {
        this.cohorts.put(cohortId, new Cohort(cohortId, cohortSize, typeId, cohortType, courseIds));
        this.plansNum = 0;
    }

    public void addTimeslot(int timeslotId, String timeslot) {
        this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
    }

    public void createPlans(Individual individual) {
        TeachingPlan plans[] = new TeachingPlan[this.getPlansNum()];

        int chromosome[] = individual.getChromosome();
        int chromosomePos = 0;
        int planIndex = 0;

        for (Cohort cohort : this.getCohortsAsArray()) {
            int courseIds[] = cohort.getCourseIds();
            for (int courseId : courseIds) {
                plans[planIndex] = new TeachingPlan(planIndex, cohort.getCohortId(), courseId);
                plans[planIndex].addTimeslot(chromosome[chromosomePos]);
                chromosomePos++;

                plans[planIndex].setRoomId(chromosome[chromosomePos]);
                chromosomePos++;

                int professor1 = chromosome[chromosomePos++];
                int professor2 = chromosome[chromosomePos++];
                int professor3 = chromosome[chromosomePos++];

                plans[planIndex].addProfessor1(professor1);
                plans[planIndex].addProfessor2(professor2);
                plans[planIndex].addProfessor3(professor3);
                planIndex++;
            }
        }
        this.plans = plans;
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

    public Course getCourse(int courseId) {
        return (Course) this.courses.get(courseId);
    }

    public int[] getCohortCourses(int cohortId) {
        Cohort cohort = (Cohort) this.cohorts.get(cohortId);
        return cohort.getCourseIds();
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

    public TeachingPlan[] getPlans() {
        return this.plans;
    }

    public int getPlansNum() {
        if (this.plansNum > 0){
            return this.plansNum;
        }

        int plansNum = 0;
        Cohort cohorts[] = (Cohort[]) this.cohorts.values().toArray(new Cohort[this.cohorts.size()]);

        for (Cohort cohort : cohorts) {
            plansNum += cohort.getCourseIds().length;
        }
        this.plansNum = plansNum;

        return this.plansNum;
    }

    public int calcClashes() {
        int clashes = 0;

        //根据roomId和timeslotId分组
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        //容量
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getRoomCapacity();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                clashes++;
            }
        }

        //检查时间段和房间的冲突
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        clashes++;
                    }
                }
            }
        }

        //统计教授冲突
        Map<Integer, List<TeachingPlan>> professorTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int professorNum = this.getCourse(plan.getCourseId()).getProfessorNum();
            int timeslotId = plan.getTimeslotId();

            if (professorNum >= 1) {
                int key1 = plan.getProfessor1Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key1, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum >= 2) {
                int key2 = plan.getProfessor2Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key2, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum == 3) {
                int key3 = plan.getProfessor3Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key3, k -> new ArrayList<>()).add(plan);
            }
        }

        //检查教授时间段冲突
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                clashes += group.size() - 1;
            }
        }

        return clashes;
    }

}