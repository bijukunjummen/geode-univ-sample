package org.bk.univ.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("courses")
public class CourseEntity {
    @Id
    private final String courseCode;
    private final String name;
    private final String description;

    public CourseEntity(String courseCode, String name, String description) {
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
