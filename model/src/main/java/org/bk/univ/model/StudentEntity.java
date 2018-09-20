package org.bk.univ.model;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;
import org.springframework.data.gemfire.mapping.annotation.Region;


@Region("students")
public class StudentEntity implements PdxSerializable {
    private String studentId;
    private String name;

    public StudentEntity() {

    }

    public StudentEntity(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    @Override
    public void toData(PdxWriter writer) {
        writer
                .writeString("pdx-student-id", studentId)
                .writeString("pdx-name", name);
    }

    @Override
    public void fromData(PdxReader reader) {
        this.studentId = reader.readString("pdx-student-id");
        this.name = reader.readString("pdx-name");
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
