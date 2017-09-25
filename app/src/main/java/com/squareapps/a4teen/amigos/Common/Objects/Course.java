package com.squareapps.a4teen.amigos.Common.Objects;


import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;


import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.COLLEGE;
import static com.squareapps.a4teen.amigos.Common.Contract.COREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;
import static com.squareapps.a4teen.amigos.Common.Contract.COURSE_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.COURSE_REQUIREMENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.CO_PREREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.CREDITS;
import static com.squareapps.a4teen.amigos.Common.Contract.CREDIT_HOURS;
import static com.squareapps.a4teen.amigos.Common.Contract.DEPARTMENT;
import static com.squareapps.a4teen.amigos.Common.Contract.DESCRIPTION;
import static com.squareapps.a4teen.amigos.Common.Contract.NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.PREFIX;
import static com.squareapps.a4teen.amigos.Common.Contract.PREREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.TITLE;


/**
 * Created by YOEL on 9/12/2015.
 */

public class Course {

    /**
     * private memmbers
     **/

    private String courseID;
    private String department;
    private String title;
    private String college;
    private String prefix;
    private String number;

    @SerializedName("Description:")
    private String description;
    private String credits;
    private String creditHours;

    @SerializedName("Prerequisites:")
    private String prerequisites;

    @SerializedName("Corequisites:")
    private String corequisites;

    @SerializedName("Co-Prerequisites:")
    private String coPrerequisites;

    @SerializedName("Course Requirements:")
    private String courseRequirements;

    public Course() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getCorequisites() {
        return corequisites;
    }

    public void setCorequisites(String corequisites) {
        this.corequisites = corequisites;
    }

    public String getCoPrerequisites() {
        return coPrerequisites;
    }

    public void setCoPrerequisites(String coPrerequisites) {
        this.coPrerequisites = coPrerequisites;
    }

    public String getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(String courseRequirements) {
        this.courseRequirements = courseRequirements;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mtitle) {
        this.title = mtitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Exclude
    public HashMap<String, Object> courseSearchToMap() {
        String courseCode = getPrefix() + getNumber();
        String path = COURSES + "/" + courseCode + "/";
        HashMap<String, Object> map = new HashMap<>();
        map.put(path + COURSE_ID, courseID);
        map.put(path + TITLE, title);
        map.put(path + DEPARTMENT, department);
        map.put(path + COLLEGE, college);
        map.put(path + PREFIX, prefix);
        map.put(path + NUMBER, number);
        map.put(path + CREDITS, credits);
        return map;
    }

    @Exclude
    public HashMap<String, Object> courseDetailtoMap(String courseCode) {
        HashMap<String, Object> map = new HashMap<>();
        String path = COURSES + "/" + courseCode + "/";
        map.put(path + DESCRIPTION, description);
        map.put(path + CREDIT_HOURS, creditHours);
        map.put(path + PREREQUISITES, prerequisites);
        map.put(path + COREQUISITES, corequisites);
        map.put(path + CO_PREREQUISITES, coPrerequisites);
        map.put(path + COURSE_REQUIREMENTS, courseRequirements);
        return map;
    }

}
