package GAcourse;

public class Class {
    private final int classId;
    private final int cohortId;
    private final int moduleId;
    private int professor1Id;
    private int professor2Id;
    private int professor3Id;
    private int timeslotId;
    private int roomId;

    public Class(int classId, int cohortId, int moduleId) {
        this.classId = classId;
        this.moduleId = moduleId;
        this.cohortId = cohortId;
    }

    public void addProfessor1(int professorId) {
        this.professor1Id = professorId;
    }
    
    public void addProfessor2(int professorId) {
        this.professor2Id = professorId;
    }
    
    public void addProfessor3(int professorId) {
        this.professor3Id = professorId;
    }
    
    public void addTimeslot(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getClassId() {
        return this.classId;
    }

    public int getCohortId() {
        return this.cohortId;
    }

    public int getModuleId() {
        return this.moduleId;
    }

    public int getProfessor1Id() {
        return this.professor1Id;
    }
    
    public int getProfessor2Id() {
        return this.professor2Id;
    }

    
    public int getProfessor3Id() {
        return this.professor3Id;
    }


    public int getTimeslotId() {
        return this.timeslotId;
    }

    public int getRoomId() {
        return this.roomId;
    }
}
