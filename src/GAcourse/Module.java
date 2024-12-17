package GAcourse;

public class Module {
    private final int moduleId;
    private final String practiceArea;
    private final String moduleCode;
    private final String module;
    private final int professorIds[];
    private final String software;
    private final String courseManager;
    private final String gradCert;
    private final int professorNum;
//    private int duration;

    public Module(int moduleId, String practiceArea, String moduleCode, String module, int professorIds[], String software, String courseManager, String gradCert, int professorNum) {
        this.moduleId = moduleId;
        this.practiceArea = practiceArea;
        this.moduleCode = moduleCode;
        this.module = module;
        this.professorIds = professorIds;
        this.software = software;
        this.courseManager = courseManager;
        this.gradCert = gradCert;
        this.professorNum = professorNum;
    }

    public int getModuleId() {
        return this.moduleId;
    }

    public String getPracticeArea() {
        return this.practiceArea;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }

    public String getModuleName() {
        return this.module;
    }

    public String getSoftware() {
        return this.software;
    }

    public String getCourseManager() {
        return this.courseManager;
    }

    public String getGradCert() {
        return this.gradCert;
    }

    public int getProfessorNum() {
        return professorNum;
    }

    public int[] getProfessorIds() {
        return this.professorIds;
    }

    public int getRandomProfessorId() {
        int professorId = professorIds[(int) (professorIds.length * Math.random())];
        return professorId;
    }
}
