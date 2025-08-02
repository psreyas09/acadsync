package models;

import java.util.List;

public class ClassGroup {
    private String classId;
    private String department;
    private int semester;
    private List<Subject> subjects;

    public ClassGroup(String classId, String department, int semester, List<Subject> subjects) {
        this.classId = classId;
        this.department = department;
        this.semester = semester;
        this.subjects = subjects;
    }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }
}