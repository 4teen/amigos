package com.squareapps.a4teen.amigos.Common.POJOS;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;
import com.squareapps.a4teen.amigos.BR;

import java.util.HashMap;

import static com.squareapps.a4teen.amigos.Common.Contract.COURSES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COLLEGE;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_ID;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.COURSE_REQUIREMENTS;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.CO_PREREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.CREDITS;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.CREDIT_HOURS;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.DEPARTMENT;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.DESCRIPTION;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.NUMBER;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.PREFIX;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.PREREQUISITES;
import static com.squareapps.a4teen.amigos.Common.Contract.Course.TITLE;
import static java.io.File.separator;


public class Course extends BaseObservable {

    /**
     * private memmbers
     **/

    private String action;
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

    @Bindable
    public String getDescription() {
        return description;

    }


    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getCredits() {
        return credits;

    }

    public void setCredits(String credits) {
        this.credits = credits;
        notifyPropertyChanged(BR.credits);
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    @Bindable
    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
        notifyPropertyChanged(BR.prerequisites);
    }

    @Bindable
    public String getCorequisites() {
        return corequisites;

    }

    public void setCorequisites(String corequisites) {
        this.corequisites = corequisites;
        notifyPropertyChanged(BR.corequisites);
    }

    @Bindable
    public String getCoPrerequisites() {
        return coPrerequisites;
    }

    public void setCoPrerequisites(String coPrerequisites) {
        this.coPrerequisites = coPrerequisites;
        notifyPropertyChanged(BR.coPrerequisites);
    }

    @Bindable
    public String getCourseRequirements() {
        return courseRequirements;

    }

    public void setCourseRequirements(String courseRequirements) {
        this.courseRequirements = courseRequirements;
        notifyPropertyChanged(BR.courseRequirements);
    }

    @Bindable
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        notifyPropertyChanged(BR.prefix);
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        notifyPropertyChanged(BR.number);
    }

    @Override
    public String toString() {
        return title;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String mtitle) {
        this.title = mtitle;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        notifyPropertyChanged(BR.department);
    }

    @Bindable
    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
        notifyPropertyChanged(BR.college);
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
        String path = COURSES + separator + courseCode + separator;
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
        String path = COURSES + separator + courseCode + separator;
        map.put(path + DESCRIPTION, description);
        map.put(path + CREDIT_HOURS, creditHours);
        map.put(path + PREREQUISITES, prerequisites);
        map.put(path + COREQUISITES, corequisites);
        map.put(path + CO_PREREQUISITES, coPrerequisites);
        map.put(path + COURSE_REQUIREMENTS, courseRequirements);
        return map;
    }

    @Bindable
    public String getCode() {
        return prefix + number;
    }

    @Bindable
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        notifyPropertyChanged(BR.action);
    }

}
