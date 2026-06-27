/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.KRSController;
import model.Course;
import model.KRS;
import model.Student;
import model.Lecturer; // Pastikan kelas Lecturer Anda ada di package model
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel; // PERBAIKAN: Import yang kurang
import javax.swing.table.DefaultTableModel; // PERBAIKAN: Import yang kurang
import javax.swing.event.DocumentEvent; // PERBAIKAN: Import yang kurang
import javax.swing.event.DocumentListener; // PERBAIKAN: Import yang kurang
/**
 *
 * @author IMELDA
 */
public class InputNilai extends javax.swing.JFrame {
  private final Controller.KRSController krsController;
  private final DefaultComboBoxModel<Student> comboModelStudent = new DefaultComboBoxModel<>();  
    private final DefaultComboBoxModel<Course> comboModelCourse = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<String> ComboModelSemester = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<Lecturer> comboModelLecturer = new DefaultComboBoxModel<>();   
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InputNilai.class.getName());

    // Variabel global penampung data aktif yang dipilih user
    private Student selectedStudent;
    private Course selectedCourse;
    private KRS krs;
    private Lecturer selectedLecturer;
    /**
     * Creates new form ImputNilai
     */
    public InputNilai() {
        initComponents();
        krsController = new KRSController();
    
    // Tambahkan 2 baris ini agar NIM & Prodi tidak bisa diketik manual
    jTextField1.setEditable(false);
    jTextField6.setEditable(false);
    jTextField5.setEditable(false);

        ComboModelSemester.addElement("Semester 1");
        ComboModelSemester.addElement("Semester 2");
        ComboModelSemester.addElement("Semester 3");
        ComboModelSemester.addElement("Semester 4");
        ComboModelSemester.addElement("Semester 5");
        ComboModelSemester.addElement("Semester 6");
        ComboModelSemester.addElement("Semester 7");
        ComboModelSemester.addElement("Semester 8");
        
        // 2. Mengisi Data Tiruan Mahasiswa
        comboModelStudent.addElement(new Student("111", "2515101028","Imelda", "Ilmu Komputer"));
        comboModelStudent.addElement(new Student("112", "2511021029","Laudya" ,"Sistem Informasi"));
        
        // 3. Mengisi Data Tiruan Mata Kuliah
        comboModelCourse.addElement(new Course("MK01", "Pemrograman Objek", 3, "3"));
        comboModelCourse.addElement(new Course("MK02", "Arsitektur dan Komputer", 2, "3"));
        
        // 4. Mengisi Data Tiruan Dosenss
        comboModelLecturer.addElement(new Lecturer("D01", "Ketut Agus Seputra, M.T.", "081923", "Software Engineering"));
        comboModelLecturer.addElement(new Lecturer("D02", "wahyu a, M.T.", "081945", "Komunikasi Data"));
        
        jComboBox1.setModel(comboModelStudent);
        jComboBox2.setModel(comboModelCourse);
        jComboBox3.setModel(ComboModelSemester);
        jComboBox4.setModel(comboModelLecturer);
        
        jComboBox1.addActionListener(e -> jComboBox1ActionPerformed(null));
        jComboBox2.addActionListener(e-> jComboBox2ActionPerformed(null));
        jComboBox3.addActionListener(e-> jComboBox3ActionPerformed(null));
        jComboBox4.addActionListener(e -> jComboBox4ActionPerformed(null));
        // Trigger awal jalankan aksi agar form otomatis terisi data indeks ke-0 saat dibuka
        DocumentListener docListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { hitungKRS(); }
            @Override public void removeUpdate(DocumentEvent e) { hitungKRS(); }
            @Override public void changedUpdate(DocumentEvent e) { hitungKRS(); }
        };
        jTextField2.getDocument().addDocumentListener(docListener); // Sikap
        jTextField3.getDocument().addDocumentListener(docListener); // UTS
        jTextField4.getDocument().addDocumentListener(docListener); // UAS
        
        jComboBox1ActionPerformed(null);
        jComboBox2ActionPerformed(null);
        jComboBox4ActionPerformed(null);
        
        tampilDataKeTabel();
    }

    // Fungsi membaca data dari MySQL database dan menampilkannya ke JTable visual
    private void tampilDataKeTabel() {
        DefaultTableModel model = (DefaultTableModel) tableNilai.getModel();
        model.setRowCount(0); // Bersihkan isi tabel visual terlebih dahulu

        try {
            java.sql.Connection conn = Config.Koneksi.getKoneksi();
            if (conn == null) return;

            String sql = "SELECT nim, kode_mk, semester, nilai_akhir, nilai_huruf FROM krs";
            
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                 java.sql.ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    String namaMhs = rs.getString("nim");
                    String namaMK = rs.getString("kode_mk");
                    
                    // COCOKKAN DENGAN DATA DUMMY AGAR TAMPILAN NAMA BAGUS
                    if(rs.getString("nim").equals("2515101028")) namaMhs = "Imelda";
                    else if(rs.getString("nim").equals("2511021029")) namaMhs = "Laudya";
                    
                    if(rs.getString("kode_mk").equals("MK01")) namaMK = "Pemrograman Objek";
                    else if(rs.getString("kode_mk").equals("MK02")) namaMK = "Arsitektur dan Komputer";

                    model.addRow(new Object[]{
                        namaMhs,
                        namaMK,
                        rs.getString("semester"),
                        rs.getDouble("nilai_akhir"),
                        rs.getString("nilai_huruf")
                    });
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Gagal memuat data ke tabel visual: " + e.getMessage());
        }
    
    }
    private void hitungKRS() {
        try {
            if (jTextField2.getText().trim().isEmpty() || jTextField3.getText().trim().isEmpty() || jTextField4.getText().trim().isEmpty()) {
                jTextField5.setText("-");
                this.krs = null;
                return;
            }
            double sikap = Double.parseDouble(jTextField2.getText().trim());
            double uts = Double.parseDouble(jTextField3.getText().trim());
            double uas = Double.parseDouble(jTextField4.getText().trim());
            
            if (jComboBox3.getSelectedItem() == null) return;
            String semester = jComboBox3.getSelectedItem().toString();
            
            if (selectedStudent != null && selectedCourse != null) {
                // Rumus kalkulasi otomatis objek model KRS
                this.krs = new KRS(selectedStudent, selectedCourse, semester, sikap, uts, uas);
                jTextField5.setText(krs.getNilaiHuruf());
            }
        } catch (NumberFormatException e) {
            jTextField5.setText("-");
            this.krs = null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableNilai = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Form Informasi Mahasiswa");

        jLabel2.setText("Identitas Mahasiswa");

        jLabel3.setText("Nama");

        jLabel4.setText("NIM");

        jComboBox1.setModel(comboModelStudent);

        jLabel14.setText("Prodi");

        jTextField6.addActionListener(this::jTextField6ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel14))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6)
                            .addComponent(jTextField1)
                            .addComponent(jComboBox1, 0, 123, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel5.setText("Informasi Mahasiswa");

        jLabel6.setText("MataKuliah");

        jLabel7.setText("Kode dan SKS");

        jLabel8.setText("Semester");

        jLabel9.setText("Dosen");

        jLabel10.setText("Nilai");

        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jLabel13.setText("Nilai Huruf");

        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jComboBox2.setModel(comboModelCourse);

        jComboBox3.setModel(ComboModelSemester);

        jComboBox4.setModel(comboModelLecturer);
        jComboBox4.addActionListener(this::jComboBox4ActionPerformed);

        jButton1.setText("btnSimpan");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(75, 75, 75)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(131, 131, 131)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGap(143, 143, 143)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jButton1))
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        tableNilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mahasiswa", "MataKuliah", "Semester", "Nilai Akhir", "Huruf"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableNilai);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 703, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(316, 316, 316)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(174, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(216, 216, 216))
        );

        pack();
    }// </editor-fold>                        

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
    selectedStudent = (Student) jComboBox1.getSelectedItem();

    if (selectedStudent != null) {
        jTextField1.setText(selectedStudent.getNim());
        jTextField6.setText(selectedStudent.getProdi());
    }
}                                     

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {                                            
      selectedCourse = (Course) jComboBox2.getSelectedItem();
        if (selectedCourse != null) {
            jLabel7.setText("Kode: " + selectedCourse.getKode() + " | SKS: " + selectedCourse.getSks());
        }  // TODO add your handling code here:
    }                                           


    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        selectedCourse = (Course) jComboBox2.getSelectedItem();
        if (selectedCourse != null) {
            jLabel7.setText("Kode: " + selectedCourse.getKode() + " | SKS: " + selectedCourse.getSks());
            hitungKRS();
        }
    }
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt ) {                                           
        hitungKRS();
    }                                          
    
    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {                                            
hitungKRS(); // Hitung ulang memastikan data tersinkronisasi
       
    // TODO add your handling code here:
    }                                           

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {                                            
hitungKRS();
    }                                           

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
      hitungKRS();
        
        if (selectedStudent != null && selectedCourse != null && this.krs != null) {
            
            // Panggil Controller untuk simpan ke database MySQL
            krsController.simpanDataNilai(this, this.krs);
            
            // Bersihkan form input angka
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("-");
            
            // Segarkan ulang visual tabel JTable
            tampilDataKeTabel();
        } else {
            JOptionPane.showMessageDialog(this, "Data belum lengkap atau format nilai angka salah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }                                        

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {                                           
    selectedLecturer = (Lecturer) jComboBox4.getSelectedItem();
    }                                          

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

      
        java.awt.EventQueue.invokeLater(() -> {
            new InputNilai().setVisible(true);
        
 });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<Student> jComboBox1;
    private javax.swing.JComboBox<Course> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<Lecturer> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTable tableNilai;
    // End of variables declaration                   
}
