/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Lecturer {

    private String idCard;
    private String nama;
    private String nidn;
    private String expertise;

    public Lecturer(String idCard, String nama, String nidn, String expertise) {
        this.idCard = idCard;
        this.nama = nama;
        this.nidn = nidn;
        this.expertise = expertise;
    }

    public String getIdCard() {
        return idCard;
    }
    public String getNama() {
        return nama;
    }
    public String getNidn() {
        return nidn;
    }
    public String getExpertise() {
        return expertise;
    }
    @Override
    public String toString() {
        return nama; // Agar nama dosen tampil dengan benar di JComboBox
    }

    
}