package com.example.onlineattendance.Pojo;

public class User {

    private String name;
    private String id;
    private String department;
    private String semester;
    private String section;

    public User(String name, String id, String department, String semester, String section) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.semester = semester;
        this.section = section;
    }

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
