package org.bk.univ.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.time.LocalDateTime;
import java.util.Date;

@Region("teachers")
public class TeacherEntity {
    @Id
    private String id;
    private String name;
    private String department;
    private int age;
    private Date joinedDate;
    private Date retirementDate;

    @PersistenceConstructor
    public TeacherEntity(String id,
                         String name,
                         String department,
                         int age,
                         Date joinedDate,
                         Date retirementDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.age = age;
        this.joinedDate = joinedDate;
        this.retirementDate = retirementDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Date getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(Date retirementDate) {
        this.retirementDate = retirementDate;
    }
}