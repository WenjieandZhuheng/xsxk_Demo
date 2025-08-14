package com.example.entity;

/**
 * @author 朱恒
 * @version 1.0
 * @description: TODO
 * @date 2025/6/17 14:50
 */

public class Speciality {

    private Integer id;
    private String name;
    private Integer collegeId;

    private  String collegeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}
