package org.bk.univ.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.Date;

@Region("teachers")
public class TeacherEntity {
    @Id
    private final String id;
    private final String name;
    private final String department;
    private final int age;
    private final Date joinedDate;
    private final Date retirementDate;

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

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getAge() {
        return age;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }


    public Date getRetirementDate() {
        return retirementDate;
    }

}