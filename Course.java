/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author IMELDA
 */
public class Course {

    private String kode;
    private String namaMK;
    private int sks;
    private int semester;

    public Course(String kode, String namaMK, int sks,int semester) {
        this.kode = kode;
        this.namaMK = namaMK;
        this.sks = sks;
        this.semester = semester;
    }

    public String getKode() {
        return kode;
    }

    public String getNamaMK() {
        return namaMK;
    }

    public int getSks() {
        return sks;
    }
    public int getSemester() {
        return semester;
    }

    @Override
    public String toString() {
        return namaMK;
    }
}