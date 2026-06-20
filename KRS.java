/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author IMELDA
 */
public class KRS {

    private Student student;
    private Course course;
    private String semester;

    private double sikap;
    private double uts;
    private double uas;

    public KRS(Student student,
               Course course,
               String semester,
               double sikap,
               double uts,
               double uas) {

        this.student = student;
        this.course = course;
        this.semester = semester;
        this.sikap = sikap;
        this.uts = uts;
        this.uas = uas;
    }

    public double getNilaiAkhir() {
        return (sikap * 0.2)
                + (uts * 0.3)
                + (uas * 0.5);
    }

    public String getNilaiHuruf() {

        double na = getNilaiAkhir();

        if (na >= 85)
            return "A";
        else if (na >= 70)
            return "B";
        else if (na >= 60)
            return "C";
        else if (na >= 50)
            return "D";
        else
            return "E";
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }
}