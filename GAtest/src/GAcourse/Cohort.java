package GAcourse;

public class Cohort {
    private final int cohortId;
    private final int cohortSize;
    private final int typeId;
    private final String cohortType;
    private final int courseIds[];

    public Cohort(int cohortId, int cohortSize, int typeId, String cohortType, int courseIds[]) {
        this.cohortId = cohortId;
        this.cohortSize = cohortSize;
        this.typeId = typeId;
        this.cohortType = cohortType;
        this.courseIds = courseIds;
    }

    public int getCohortId() {
        return this.cohortId;
    }

    public int getCohortSize() {
        return this.cohortSize;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public String getCohortType() {
        return cohortType;
    }

    public int[] getCourseIds() {
        return this.courseIds;
    }
}
