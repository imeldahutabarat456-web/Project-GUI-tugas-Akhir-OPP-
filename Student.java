/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
/**
 *
 * @author IMELDA
 */

public class Student {

    private String idCard;
    private String nim;
    private String nama;
    private String prodi;
    
    private ArrayList<KRS> daftarKRS = new ArrayList<>();

    public Student(String idCard,String nim, String nama, String prodi) {
        this.idCard = idCard;
        this.nim = nim;
        this.nama = nama;
        this.prodi = prodi;
    }

    public String getIdCard() {
        return idCard;
    }
    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getProdi() {
        return prodi;
    }
    public void addKRS(KRS krs) {
        daftarKRS.add(krs);
    }

    public ArrayList<KRS> getDaftarKRS() {
        return daftarKRS;
    }
     public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }


    @Override
    public String toString() {
        return nama;
    }
    
}
