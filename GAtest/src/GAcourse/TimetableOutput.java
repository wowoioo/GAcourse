package GAcourse;

import java.util.ArrayList;
import java.util.List;

public class TimetableOutput {

    public static class InputData {
        private String courseName;
        private String software;
        private int cohortId;
        private String roomNumber;
        private List<String> professors;
        private String time;
        private String courseManager;
        private String gradCert;

        public InputData(String courseName, String software, int cohortId, String roomNumber, List<String> professors, String time, String courseManager, String gradCert) {
            this.courseName = courseName;
            this.software = software;
            this.cohortId = cohortId;
            this.roomNumber = roomNumber;
            this.professors = professors;
            this.time = time;
            this.courseManager = courseManager;
            this.gradCert = gradCert;
        }

        @Override
        public String toString() {
            return "InputData{" +
                    "courseName='" + courseName + '\'' +
                    ", software='" + software + '\'' +
                    ", cohortId='" + cohortId + '\'' +
                    ", roomNumber='" + roomNumber + '\'' +
                    ", professors=" + professors +
                    ", time='" + time + '\'' +
                    ", courseManager='" + courseManager + '\'' +
                    ", gradCert='" + gradCert + '\'' +
                    '}';
        }
    }

    public List<InputData> generateTimetableList(Timetable timetable, Population population, int generation) {
        timetable.createPlans(population.getFittest(0));
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Clashes: " + timetable.calcClashes());

        List<InputData> inputDataList = new ArrayList<>();
        TeachingPlan[] plans = timetable.getPlans();

        //输出字段进List
        for (TeachingPlan bestplan : plans) {
            String courseName = timetable.getCourse(bestplan.getCourseId()).getCourseName();
            String software = timetable.getCourse(bestplan.getCourseId()).getSoftware();
            int cohortId = timetable.getCohort(bestplan.getCohortId()).getCohortId();
            String roomNumber = timetable.getRoom(bestplan.getRoomId()).getRoomNumber();

            int professorNum = timetable.getCourse(bestplan.getCourseId()).getProfessorNum();
            List<String> professors = new ArrayList<>();
            professors.add(timetable.getProfessor(bestplan.getProfessor1Id()).getProfessorName());
            if (professorNum > 1) {
                professors.add(timetable.getProfessor(bestplan.getProfessor2Id()).getProfessorName());
            }
            if (professorNum > 2) {
                professors.add(timetable.getProfessor(bestplan.getProfessor3Id()).getProfessorName());
            }

            String time = timetable.getTimeslot(bestplan.getTimeslotId()).getTimeslot();
            String courseManager = timetable.getCourse(bestplan.getCourseId()).getCourseManager();
            String gradCert = timetable.getCourse(bestplan.getCourseId()).getGradCert();

            InputData inputData = new InputData(courseName, software, cohortId, roomNumber, professors, time, courseManager, gradCert);
            inputDataList.add(inputData);
        }

        return inputDataList;
    }
}
