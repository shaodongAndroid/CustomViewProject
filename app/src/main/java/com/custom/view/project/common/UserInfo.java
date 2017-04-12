package com.custom.view.project.common;

import java.util.List;

/**
 * Created by 少东 on 2017/1/18.
 */

public class UserInfo {

    private String userName ;

    private List<Course> courseList ;

    private Course[] courses ;

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
