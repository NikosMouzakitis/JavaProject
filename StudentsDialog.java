package ent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StudentsDialog extends JDialog {

    public StudentsDialog(Object o, boolean modal, int global_tpos) {
        global_student_pos = global_tpos;
        System.out.println("I m in the constructor of StudentDialog class");
        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;
            Method method1 = StudentsDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb_student = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(StudentsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        tb_student.setBm_state(0, false);
        tb_student.setBm_state(1, false);
        tb_student.setBm_state(5, false);
        tb_student.setBm_state(6, false);
        tb_student.hideButton(CommonToolbar.REPLICATE);
        tb_student.hideButton(CommonToolbar.TABLE);
        setLayout(new BorderLayout());

        p_student = new JPanel(new GridLayout(5, 2));

        unow = "1";
        utotal = "0";
        ResultSet bb = new DBConnection().Query("select count(*) from student");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbert = new JLabel(unow + " / " + utotal);

        llserial = new JLabel("Serial:", SwingConstants.RIGHT);
        lam = new JLabel("AM", SwingConstants.RIGHT);
        lsemester = new JLabel("Semester:", SwingConstants.RIGHT);

        ttfserial = new JTextField();   //int
        ttfserial.setEnabled(false);    //varchar
        tfam = new JTextField();
        tfsemester = new JTextField();  // int 
        name_label = new JLabel("Name: ", SwingConstants.RIGHT);
        int tempid;
        String tempam;
        int tempsemester;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");
            rs2.next();
            global_total = rs2.getInt(1);
            System.out.println("dbg");
            System.out.println("Calculated number of students:");
            System.out.println(global_total);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");
            rs.next();

            Students st = new Students(rs.getInt(1), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(2), rs.getInt(3));

            String tempfirst = st.getFirst();
            String templast = st.getLast();
            rs.beforeFirst();

            tempid = st.getID();
            tempam = st.getAM();
            tempsemester = st.getSemester();

            ttfserial.setText(Integer.toString(tempid));
            tfam.setText(tempam);
            tfsemester.setText(Integer.toString(tempsemester));

            p_student.add(llserial);
            p_student.add(ttfserial);
            p_student.add(lam);
            p_student.add(tfam);
            p_student.add(lsemester);
            p_student.add(tfsemester);

            String dis = templast + " , " + tempfirst;
            System.out.println(dis);
            System.out.println("I ll display student: " + dis);
            String[] userlf = {dis};
            cbuser = new JComboBox(userlf);
            p_student.add(name_label);
            p_student.add(cbuser);

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }
        add(tb_student, BorderLayout.NORTH);
        add(p_student, BorderLayout.CENTER);
        add(numbert, BorderLayout.SOUTH);

        setSize(450, 320);
        setModal(modal);
        setTitle("Students");
        setVisible(true);
    }

    private void updateToolbarButtonsState(int iCase) {
        switch (iCase) {
            case 0:
                for (int i = 0; i < 13; i++) {
                    tb_student.setBm_state(i, true);
                }
                tb_student.setBm_state(0, false);
                tb_student.setBm_state(1, false);
                tb_student.setBm_state(5, false);
                tb_student.setBm_state(6, false);
                break;
            case 1:

                if (global_student_pos == 1) {
                    tb_student.setBm_state(0, false);
                    tb_student.setBm_state(1, false);
                } else {
                    tb_student.setBm_state(0, true);
                    tb_student.setBm_state(1, true);
                }
                break;
            case 2:
                tb_student.setBm_state(0, true);
                tb_student.setBm_state(1, true);
                break;
            case 3:
                tb_student.setBm_state(0, true);
                tb_student.setBm_state(1, true);
                tb_student.setBm_state(2, false);
                tb_student.setBm_state(3, false);
                tb_student.setBm_state(4, true);
                tb_student.setBm_state(5, false);
                tb_student.setBm_state(6, false);
                tb_student.setBm_state(7, true);
                tb_student.setBm_state(8, true);
                tb_student.setBm_state(9, true);
                tb_student.setBm_state(10, true);
                tb_student.setBm_state(11, true);
                tb_student.setBm_state(12, true);
                break;
            case 4:
                button_yes_no_setup();
                break;
            case 5:
                button_start_setup();
                break;
            case 6:
                button_start_setup();
                break;
            case 7:
                button_yes_no_setup();
                break;
            case 8:
                button_yes_no_setup();
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                button_yes_no_setup();
                break;
            case 12:
                // not implemented yet
                break;
        }

    }

    public void doFunctionCall(Integer __farg, JButton[] tm) {
        int farg = __farg;

        switch (farg) {

            case CommonToolbar.FIRST:
                doConnect();
                global_student_pos = 1;

                String tempfirst,
                 templast;

                tempfirst = "";
                templast = "";
                unow = Integer.toString(global_student_pos);
                utotal = Integer.toString(global_total);
                numbert.setText(unow + " / " + utotal);
                try {
                    if (rs.next()) {

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfam.setText(rs.getString(2));
                        tfsemester.setText(Integer.toString(rs.getInt(3)));

                        tempfirst = rs.getString(8);
                        templast = rs.getString(9);

                        String dis = templast + " , " + tempfirst;
                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        System.out.println("Refreshed details back to first element.Student table.");
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                break;
            case CommonToolbar.PREVIOUS:
                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");

                    for (int i = 0; i < global_student_pos; i++) {
                        rs.next();
                    }

                    if (rs.previous()) {

                        global_student_pos--;

                        unow = Integer.toString(global_student_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        if (global_student_pos < global_total) {
                            tm[2].setEnabled(true);
                            tm[3].setEnabled(true);
                        }
                        if (global_student_pos == 1) {
                            tm[0].setEnabled(false);
                            tm[1].setEnabled(false);
                        }

                        templast = rs.getString(8);
                        tempfirst = rs.getString(9);
                        String dis = templast + " , " + tempfirst;

                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfam.setText(rs.getString(2));
                        tfsemester.setText(Integer.toString(rs.getInt(3)));
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                break;
            case CommonToolbar.NEXT:
                /*NEXT*/

                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");

                    for (int i = 0; i < global_student_pos; i++) {
                        rs.next();
                    }

                    if (rs.next()) {
                        System.out.println("Existing next");
                        global_student_pos++;

                        unow = Integer.toString(global_student_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);
                        System.out.println(global_total);
                        if (global_student_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        templast = rs.getString(8);
                        tempfirst = rs.getString(9);
                        String dis = templast + " , " + tempfirst;

                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfam.setText(rs.getString(2));
                        tfsemester.setText(Integer.toString(rs.getInt(3)));
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.LAST:
                // Last record.
                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");

                    rs.last();

                    global_student_pos = global_total;

                    unow = Integer.toString(global_student_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    if (global_student_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    templast = rs.getString(8);
                    tempfirst = rs.getString(9);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfam.setText(rs.getString(2));
                    tfsemester.setText(rs.getString(3));

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.ADD:
                // New //

                doConnect();

                try {
                    action = 5;

                    rs = stmt.executeQuery("select * from student order by idstudent desc");
                    rs.first();
                    desired_id = rs.getInt(1) + 1;
                    System.out.println("Desired_id: " + desired_id);

                    rs = stmt.executeQuery("SELECT * FROM  users left join  student\n"
                            + "on idusers = student.idstudent\n"
                            + "WHERE (student.idstudent IS NULL) and users.idusers not in ( select idusers from \n"
                            + " users left join  teacher on idusers = teacher.idteacher \n"
                            + "where( teacher.idteacher = users.idusers))");

                    ArrayList<String> ar = new ArrayList<String>();

                    ttfserial.setText("");

                    String tfirst, tlast;

                    while (rs.next()) {
                        System.out.println("Element to insert on ComboBox");
                        tfirst = rs.getString(6);
                        tlast = rs.getString(5);
                        String toinsert = tfirst + ", " + tlast;
                        ar.add(toinsert);
                    }
                    int arraylen = ar.size();

                    String[] userlf = ar.stream().toArray(String[]::new);
                    System.out.println("Creating model for the ComboBox");
                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText("");
                    tfam.setText("");
                    tfsemester.setText("");

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.CANCEL:
                //cancel

                button_start_setup();

                if (global_student_pos > 1) {
                    tm[0].setEnabled(true);
                }

                doConnect();

                try {
                    rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");
                    System.out.println("my possition is: " + global_student_pos);

                    for (int i = 0; i < global_student_pos; i++) {
                        if (rs.next())
                            ;
                    }
                    unow = Integer.toString(global_student_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);
                    templast = rs.getString(8);
                    tempfirst = rs.getString(9);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfam.setText(rs.getString(2));
                    tfsemester.setText(Integer.toString(rs.getInt(3)));
                    if (global_student_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    if (global_student_pos > 1) {
                        tm[0].setEnabled(true);
                        tm[1].setEnabled(true);
                    } else {
                        tm[0].setEnabled(false);
                        tm[1].setEnabled(false);
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                break;
            case CommonToolbar.OK:
                //Verify - OK

                if (action == 7) {

                    // Get changed details.
                    System.out.println("Just pressed OK button");
                    String sem, am;

                    am = tfam.getText();
                    sem = tfsemester.getText();

                    String sql2 = "update student " + "set  AM =" + "'" + am + "'" + ", examino=" + sem + " where idstudent = " + modify_id;
                    try {
                        stmt.executeUpdate(sql2);

                        if (global_student_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        if (global_student_pos > 1) {
                            tm[0].setEnabled(true);
                            tm[1].setEnabled(true);
                        } else {
                            System.out.println("Kleinw ta koumpia");
                            tm[0].setEnabled(false);
                            tm[1].setEnabled(false);
                        }
                        JOptionPane.showMessageDialog(this, "Done", "Update", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                } else if (action == 8) { // coming to delete.
                    System.out.println("To be deleted is: ");
                    System.out.println(desired_id);
                    String sql2 = "DELETE FROM student WHERE idstudent= " + desired_id + "";

                    try {
                        stmt.executeUpdate(sql2);

                        JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                        global_total--;
                        String unow, utotal;

                        unow = Integer.toString(global_student_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);
                        doFunctionCall(0, tm);

                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                } else if (action == 5) {

                    int uid = 0;
                    int possit = cbuser.getSelectedIndex();
                    System.out.println("Selected in the combobox is index:");
                    System.out.println(possit);
                    Object o = cbuser.getModel().getElementAt(possit);
                    String tochop = o.toString();

                    String delims = "[ ]+";
                    String[] tokens = tochop.split(delims);
                    // 0 - Firstname
                    // 1 - LastName
                    universalfirst = tokens[0].substring(0, tokens[0].length() - 1);
                    universallast = tokens[1];

                    try {
                        rs = stmt.executeQuery("select idusers from users where lastname = '" + universallast + "'" + " and firstname=" + "'" + universalfirst + "'");

                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                    try {
                        rs.next();
                        uid = rs.getInt(1);
                        System.out.println("uid = ");
                        System.out.println(uid);
                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                    String sem, am;

                    am = tfam.getText();
                    sem = tfsemester.getText();

                    String sql2 = "INSERT INTO student VALUES "
                            + "(" + uid + "," + "'" + am + "'" + "," + sem + ")";
                    System.out.println(sql2);

                    try {
                        stmt.executeUpdate(sql2);
                        JOptionPane.showMessageDialog(this, "Done", "Student Insertion", JOptionPane.INFORMATION_MESSAGE);
                        global_total++;
                        unow = Integer.toString(global_student_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        doConnect();

                        try {
                            rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");
                            System.out.println("my possition is: " + global_student_pos);

                            global_student_pos = 1;
                            for (int i = 0; i < global_total; i++) {
                                if (rs.next()) {
                                    global_student_pos++;
                                }
                            }
                            System.out.println("Eftasa stin : " + global_student_pos);
                            global_student_pos--;
                            unow = Integer.toString(global_student_pos);
                            utotal = Integer.toString(global_total);
                            numbert.setText(unow + " / " + utotal);

                            templast = rs.getString(8);
                            tempfirst = rs.getString(9);
                            String dis = templast + " , " + tempfirst;
                            String[] userlf = {dis};

                            DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                            cbuser.setModel(model);

                            ttfserial.setText(Integer.toString(rs.getInt(1)));
                            tfam.setText(rs.getString(2));
                            tfsemester.setText(rs.getString(3));

                            System.out.println(global_student_pos);
                            System.out.println(global_total);
                            if (global_student_pos == global_total) {
                                tm[0].setEnabled(true);
                                tm[1].setEnabled(true);
                                tm[2].setEnabled(false);
                                tm[3].setEnabled(false);
                            } else if ((global_student_pos < global_total) && (global_student_pos > 1)) {
                                tm[2].setEnabled(true);
                                tm[3].setEnabled(true);
                            }
                            if (global_student_pos > 1) {
                                tm[0].setEnabled(true);
                                tm[1].setEnabled(true);
                            } else {
                                System.out.println("Kleinw ta koumpia");
                                tm[0].setEnabled(false);
                                tm[1].setEnabled(false);
                            }

                        } catch (SQLException ex) {
                            String msg = "Error: " + ex.getMessage();
                            System.out.println(msg);
                        }
                        tm[0].setEnabled(true);
                        tm[1].setEnabled(true);
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                        tm[4].setEnabled(true);
                        tm[5].setEnabled(false);
                        tm[6].setEnabled(false);
                        tm[7].setEnabled(true);
                        tm[8].setEnabled(true);
                        tm[9].setEnabled(true);
                        tm[10].setEnabled(true);
                        tm[11].setEnabled(true);
                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }
                }
                break;
            case CommonToolbar.MODIFY:

                try {
                    modify_id = rs.getInt(1);
                    System.out.println("Tha peiraksw to id:" + modify_id);
                    action = 7;
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.DELETE:

                action = 8;

                try {
                    desired_id = rs.getInt(1);

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                System.out.println("Desired ID to be deleted: " + desired_id);
                break;
            case CommonToolbar.REFRESH:

                try {
                    if (rs.isBeforeFirst()) {
                        rs.next();
                    }

                    templast = rs.getString(8);
                    tempfirst = rs.getString(9);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfam.setText(rs.getString(2));
                    tfsemester.setText(rs.getString(3));

                } catch (SQLException ex) {
                    System.out.println("Its me your lovely bug");
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                System.out.println("Refresh completed");
                break;
            case CommonToolbar.SEARCH: // search for lastnames.

                doTSearch();

                break;
        }
        updateToolbarButtonsState(farg);
    }

    private void pn_buttoncheck() throws SQLException {
        if (rs.isFirst()) {
            prevS.setEnabled(false);
        } else {
            prevS.setEnabled(true);
        }

        if (rs.isLast()) {
            nextS.setEnabled(false);
        } else {
            nextS.setEnabled(true);
        }
    }

    private void dotSearch() {
        String ql;
        ql = slast.getText();

        System.out.println("I will Search for " + ql);
        try {

            rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers and lastname = " + "'" + ql + "'");

            if (rs.next()) {

                pn_buttoncheck();

                ttfserial.setText(Integer.toString(rs.getInt(1)));

                String templast = rs.getString(8);
                String tempfirst = rs.getString(9);
                String dis = templast + " , " + tempfirst;

                String[] userlf = {dis};

                DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                cbuser.setModel(model);
                ttfserial.setText(Integer.toString(rs.getInt(1)));
                tfam.setText(rs.getString(2));
                tfsemester.setText(rs.getString(3));

            } else {
                JOptionPane.showMessageDialog(this, "Not Found", "Search", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            String msg = "Error: " + ex.getMessage();
            System.out.println(msg);
        }
    }

    private void doSearchPrev() {

        try {
            if (rs.previous()) {

                pn_buttoncheck();

                ttfserial.setText(Integer.toString(rs.getInt(1)));
                tfam.setText(rs.getString(2));
                tfsemester.setText(rs.getString(3));

                String templast = rs.getString(8);
                String tempfirst = rs.getString(9);
                String dis = templast + " , " + tempfirst;

                String[] userlf = {dis};

                DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                cbuser.setModel(model);
            }
        } catch (SQLException ex) {
            String msg = "Error: " + ex.getMessage();
            System.out.println(msg);
        }
    }

    private void doSearchNext() {

        try {
            if (rs.next()) {

                pn_buttoncheck();

                ttfserial.setText(Integer.toString(rs.getInt(1)));
                tfam.setText(rs.getString(2));
                tfsemester.setText(rs.getString(3));
                String templast = rs.getString(8);
                String tempfirst = rs.getString(9);
                String dis = templast + " , " + tempfirst;

                String[] userlf = {dis};

                DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                cbuser.setModel(model);
            }
        } catch (SQLException ex) {
            String msg = "Error: " + ex.getMessage();
            System.out.println(msg);
        }

    }

    private void doTSearch() {

        s_search = new JDialog();
        s_search.setLayout(new BorderLayout());
        s_sp = new JPanel(new GridLayout(2, 3));
        s_spp = new JPanel();

        slastname = new JLabel("Lastname", SwingConstants.RIGHT);

        slast = new JTextField(10);

        searchB = new JButton("Search");

        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dotSearch();
            }
        });

        prevS = new JButton("Prev");
        prevS.setEnabled(false);

        prevS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearchPrev();
            }
        });

        nextS = new JButton("Next");
        nextS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearchNext();
            }
        });
        nextS.setEnabled(false);

        s_sp.add(slastname);
        s_sp.add(slast);
        s_sp.add(searchB);
        s_sp.add(prevS);
        s_sp.add(nextS);

        s_search.add(s_sp, BorderLayout.NORTH);
        s_search.add(s_spp, BorderLayout.CENTER);
        s_search.setSize(450, 140);
        s_search.setModal(true);
        s_search.setTitle("Search");
        s_search.setVisible(true);

    }

    private void doConnect() {
        con = null;
        stmt = null;
        rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            String msg = "The com.mysql.jdbc.Driver is missing\n"
                    + "install and rerun the application";
            System.out.println(msg);
            System.exit(1);
        }

        // connect to db
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM student left join users on idusers = student.idstudent WHERE student.idstudent = users.idusers");
            // select all
            System.out.println("Test query complete.");

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }
    }

    private void button_yes_no_setup() {
        for (int i = 0; i < 13; i++) {
            tb_student.setBm_state(i, false);
        }
        tb_student.setBm_state(5, true);
        tb_student.setBm_state(6, true);
    }

    private void button_start_setup() {
        for (int i = 0; i < 13; i++) {
            tb_student.setBm_state(i, true);
        }
        tb_student.setBm_state(0, false);
        tb_student.setBm_state(5, false);
        tb_student.setBm_state(6, false);
    }

    private int global_student_pos;
    private int global_total;
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, rs2;
    private String universallast, universalfirst;
    private int desired_id;
    private JComboBox cbuser;
    public JButton[] tm;
    private JLabel name_label;
    private JLabel numbert;
    private JPanel p_student;
    private CommonToolbar tb_student;
    private JLabel lam, lsemester, llserial;
    private JTextField tfam, tfsemester;
    private JTextField ttfserial;
    public int action, modify_id;
    private String unow, utotal;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel slastname;
    private JTextField slast;
}
