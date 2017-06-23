package com.square.apps.amigos.common.common.course;


/**
 * Created by YOEL on 9/12/2015.
 */

public class Course {


    /**
     * private memmbers
     **/
    private String CRN;
    private String courseID;
    private String campus;
    private String department;
    private String subjectCRS;
    private String section;
    private String title;
    private String classTime;
    private String instructor;
    private String building;
    private String room;
    private String college;
    private String days;

    public Course(){

    }

    @Override
    public String toString(){
        return title;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String mtitle){
        this.title = mtitle;
    }

    /**
     * getters
     */
    public String getClassTime(){
        return classTime;
    }

    public void setClassTime(String classTime){
        this.classTime = classTime;
    }

    public String getDays(){
        return days;
    }

    public void setDays(String days){
        this.days = days;
    }

    public String getSubjectCRS(){
        return subjectCRS;
    }

    /**
     * setters
     **/
    public void setSubjectCRS(String subjectCRS){
        this.subjectCRS = subjectCRS;
    }

    public String getCRN(){
        return CRN;
    }

    public void setCRN(String CRN){
        this.CRN = CRN;
    }

    public String getCampus(){
        return campus;
    }

    public void setCampus(String campus){
        this.campus = campus;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public String getSection(){
        return section;
    }

    public void setSection(String session){
        this.section = session;
    }

    public String getCollege(){
        return college;
    }

    public void setCollege(String college){
        this.college = college;
    }

    public String getBuilding(){
        return building;
    }

    public void setBuilding(String building){
        this.building = building;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public String getInstructor(){
        return instructor;
    }

    public void setInstructor(String instructor){
        this.instructor = instructor;
    }

    public String getCourseID(){
        return courseID;
    }

    public void setCourseID(String courseID){
        this.courseID = courseID;
    }


}
