/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class TeachingDialog extends JDialog {

    public TeachingDialog(Object o, boolean modal, int gl) {

        this.global_pos = gl;
        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;
            Method method1 = TeachingDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(TeachingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        tb.setBm_state(0, false);
        tb.setBm_state(1, false);
        tb.setBm_state(5, false);
        tb.setBm_state(6, false);
        tb.hideButton(CommonToolbar.REPLICATE);
        tb.hideButton(CommonToolbar.TABLE);
        setLayout(new BorderLayout());
        p_teach = new JPanel(new GridLayout(2, 2));
        unow = "1";
        utotal = "0";
        ResultSet bb = new DBConnection().Query("select count(*) from teaches");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbert = new JLabel(unow + " / " + utotal);

        llesson = new JLabel("Lesson:", SwingConstants.RIGHT);
        lname = new JLabel("Teacher:", SwingConstants.RIGHT);

        tflesson = new JTextField();   //int
        tfname = new JTextField();  // int 

        int tempid;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM teaches");
            rs2.next();
            global_total = rs2.getInt(1);
            System.out.println("dbg");
            System.out.println("Calculated number of lessons:");
            System.out.println(global_total);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("Select * from teaches");
            rs.next();
            id_reg = rs.getInt(1);

            int idteacher, idlesson;
            idteacher = rs.getInt(3);
            idlesson = rs.getInt(2);

            rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
            rs.next();
            String lessonname = rs.getString(1);

            String teachername;
            rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
            rs.next();
            teachername = rs.getString(9) + "," + rs.getString(10);
            tflesson.setText(lessonname);
            tfname.setText(teachername);

            rs = stmt.executeQuery("select * from teaches");
            p_teach.add(llesson);
            p_teach.add(tflesson);
            p_teach.add(lname);
            p_teach.add(tfname);

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }
        add(tb, BorderLayout.NORTH);
        add(p_teach, BorderLayout.CENTER);
        add(numbert, BorderLayout.SOUTH);

        setSize(450, 320);
        setModal(modal);
        setTitle("Teaching");
        setVisible(true);
    }

    public void doFunctionCall(Integer __farg, JButton[] tm) {
        int farg = __farg;
        switch (farg) {

            case CommonToolbar.FIRST:
                doConnect();
                global_pos = 1;

                try {
                    if (rs.next()) {
                        stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM teaches");
                        rs2.next();
                        global_total = rs2.getInt(1);
                        System.out.println("dbg");
                        System.out.println("Calculated number of lessons:");
                        System.out.println(global_total);

                        stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rs = stmt.executeQuery("Select * from teaches");
                        rs.next();
                        id_reg = rs.getInt(1);
                        int idteacher, idlesson;
                        idteacher = rs.getInt(3);
                        idlesson = rs.getInt(2);
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);
                        rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                        rs.next();
                        String lessonname = rs.getString(1);

                        String teachername;
                        rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                        rs.next();
                        teachername = rs.getString(9) + "," + rs.getString(10);
                        tflesson.setText(lessonname);
                        tfname.setText(teachername);
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert = new JLabel(unow + " / " + utotal);
                        System.out.println("Refreshed details back to first element.Teacher table.");
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                break;
            case CommonToolbar.PREVIOUS:
                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs = stmt.executeQuery("SELECT * FROM teaches");

                    for (int i = 0; i < global_pos; i++) {
                        rs.next();
                    }
                    id_reg = rs.getInt(1);
                    if (rs.previous()) {
                        id_reg = rs.getInt(1);
                        global_pos--;
                        String unow, utotal;
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        if (global_pos < global_total) {
                            tm[2].setEnabled(true);
                            tm[3].setEnabled(true);
                        }
                        if (global_pos == 1) {
                            tm[0].setEnabled(false);
                            tm[1].setEnabled(false);
                        }

                        int idteacher, idlesson;
                        idteacher = rs.getInt(3);
                        idlesson = rs.getInt(2);

                        rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                        rs.next();
                        String lessonname = rs.getString(1);

                        String teachername;
                        rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                        rs.next();
                        teachername = rs.getString(9) + "," + rs.getString(10);
                        tflesson.setText(lessonname);
                        tfname.setText(teachername);

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
                    rs = stmt.executeQuery("SELECT * FROM teaches");

                    for (int i = 0; i < global_pos; i++) {
                        rs.next();
                    }
                    id_reg = rs.getInt(1);
                    if (rs.next()) {
                        id_reg = rs.getInt(1);
                        System.out.println("Existing eimai sto next next");
                        global_pos++;
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        System.out.println(global_total);
                        if (global_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        int idteacher, idlesson;
                        idteacher = rs.getInt(3);
                        idlesson = rs.getInt(2);

                        rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                        rs.next();
                        String lessonname = rs.getString(1);

                        String teachername;
                        rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                        rs.next();
                        teachername = rs.getString(9) + "," + rs.getString(10);
                        tflesson.setText(lessonname);
                        tfname.setText(teachername);
                        System.out.println("Psaxnw ta: ");
                        System.out.println(idteacher);
                        System.out.println(idlesson);
                        System.out.println(lessonname + " " + teachername);

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
                    rs = stmt.executeQuery("SELECT * from teaches");

                    rs.last();
                    id_reg = rs.getInt(1);
                    global_pos = global_total;

                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    if (global_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }
                    int idteacher, idlesson;
                    idteacher = rs.getInt(3);
                    idlesson = rs.getInt(2);

                    rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                    rs.next();
                    String lessonname = rs.getString(1);

                    String teachername;
                    rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                    rs.next();
                    teachername = rs.getString(9) + "," + rs.getString(10);
                    tflesson.setText(lessonname);
                    tfname.setText(teachername);

                    rs = stmt.executeQuery("select * from teaches");

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.ADD:
                // New //
                 
                doConnect();

                try {
                    button_start_setup();
                    System.out.println("ta buttons eginan");
                    JDialog ins = new JDialog();
                    ins.setLayout(new GridLayout(3, 2));

                    JComboBox ll, rr;
                    ll = new JComboBox();
                    rr = new JComboBox();
                    JLabel labell, labelr;
                    labell = new JLabel("Lesson:", SwingConstants.RIGHT);
                    labelr = new JLabel("Teacher:", SwingConstants.RIGHT);
                    ArrayList<String> al = new ArrayList<String>(); // lesson
                    ArrayList<String> ar = new ArrayList<String>(); //teachers

                    String tles;

                    rs = stmt.executeQuery("select * from teacher left join users on teacher.idteacher = users.idusers");

                    while (rs.next()) {
                        
                        tles = rs.getString(9) + " " + rs.getString(10);
                        String toinsert = tles;
                        ar.add(toinsert);
                    }
                    int arraylen = ar.size();
                    String[] userlf = ar.stream().toArray(String[]::new);

                    rs = stmt.executeQuery("select * from lesson");
                    String tles2;
                    while (rs.next()) {
                        
                        tles2 = rs.getString(2);
                        String toinsert = tles2;
                        al.add(toinsert);
                    }
                    int arraylen2 = al.size();
                    String[] userlf2 = al.stream().toArray(String[]::new);

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    DefaultComboBoxModel model2 = new DefaultComboBoxModel(userlf2);

                    ll.setModel(model); //
                    rr.setModel(model2);

                    ins.add(labell);
                    ins.add(rr);
                    ins.add(labelr);
                    ins.add(ll);
                    JButton okb = new JButton("ok");
                    ins.add(okb);
                    
                    okb.addActionListener(new ActionListener() { //Search
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            try {
                                rs = stmt.executeQuery("select * from teaches order by idteaches desc");
                                rs.first();
                                desired_id = rs.getInt(1) + 1;

                                String teacherf = "", teacherl = "", namelesson;
                                int idl, idt;
                                int possitl = ll.getSelectedIndex(); //teacher
                                int possitr = rr.getSelectedIndex(); //lesson

                                Object ol = ll.getModel().getElementAt(possitl);
                                String fullname = ol.toString();
                                System.out.println(fullname);
                                Object or = rr.getModel().getElementAt(possitr);
                                namelesson = or.toString();

                                System.out.println("Lesson's name = " + namelesson);

                                rs = stmt.executeQuery("select idlesson from lesson where name = " + "'" + namelesson + "'");
                                rs.first();
                                idl = rs.getInt(1);

                                String delims = "[ ]+";
                                String[] tokens = fullname.split(delims);
                                // 0 - Firstname
                                // 1 - LastName
                                teacherl = tokens[0].substring(0, tokens[0].length());
                                teacherf = tokens[1];

                                rs = stmt.executeQuery("SELECT  *  FROM teacher left join  users on teacher.idteacher = users.idusers "
                                        + "where lastname = " + "'" + teacherl + "'" + " and firstname = " + "'" + teacherf + "'" + ";");

                                rs.next();
                                idt = rs.getInt(1);

                                System.out.println(teacherf + " " + teacherl);

                                String sqlinsert = "INSERT INTO teaches VALUES (" + desired_id + "," + idl + "," + idt + ")";
                                stmt.executeUpdate(sqlinsert);
                                JOptionPane.showMessageDialog(null, "Done", "Insertion", JOptionPane.INFORMATION_MESSAGE);
                                global_total++;

                                String unow, utotal;
                                unow = Integer.toString(global_pos);
                                utotal = Integer.toString(global_total);
                                numbert.setText(unow + " / " + utotal);

                                ins.dispose();
                                tm[1].setEnabled(false);
                                button_start_setup();

                                rs = stmt.executeQuery("Select * from teaches");
                                rs.next();
                                id_reg = rs.getInt(1);
                                int idteacher, idlesson;
                                idteacher = rs.getInt(3);
                                idlesson = rs.getInt(2);

                                rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                                rs.next();
                                String lessonname = rs.getString(1);

                                String teachername;
                                rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                                rs.next();
                                teachername = rs.getString(9) + "," + rs.getString(10);
                                tflesson.setText(lessonname);
                                tfname.setText(teachername);

                                rs = stmt.executeQuery("select * from teaches");
                                
                                button_start_setup();
                                
                            } catch (SQLException ex) {
                                String msg = "Error: " + ex.getMessage();
                                System.out.println(msg);
                            }

                        }
                    });

                    ins.setTitle("Insertion");
                    ins.setModal(true);
                    ins.setSize(300, 250);
                    ins.setVisible(true);

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                button_start_setup();
                break;
            case CommonToolbar.CANCEL:
                //cancel

                button_start_setup();

                if (global_pos > 1) {
                    tm[0].setEnabled(true);
                }

                doConnect();

                try {
                    rs = stmt.executeQuery("SELECT * from teaches");
                    System.out.println("my possition is: " + global_pos);

                    for (int i = 0; i < global_pos; i++) {
                        if (rs.next())
                            ;
                    }
                    id_reg = rs.getInt(1);
                    String unow, utotal;
                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    int idteacher, idlesson;
                    idteacher = rs.getInt(3);
                    idlesson = rs.getInt(2);

                    rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                    rs.next();
                    String lessonname = rs.getString(1);

                    String teachername;
                    rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                    rs.next();
                    teachername = rs.getString(9) + "," + rs.getString(10);
                    tflesson.setText(lessonname);
                    tfname.setText(teachername);

                    if (global_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    if (global_pos > 1) {
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

                if (action == 8) { // coming to delete.

                    try {

                        stmt.executeUpdate("delete from teaches where idteaches = " + id_reg + "");
                        JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                        global_total--;
                        String unow, utotal;

                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow+" / "+utotal);
                        
                        doFunctionCall(0, tm);

                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }
                }
                break;
            case CommonToolbar.MODIFY:

                break;
            case CommonToolbar.DELETE:

                action = 8;

                System.out.println("Desired ID to be deleted: " + desired_id);
                break;
            case CommonToolbar.REFRESH:

                try {
                    rs = stmt.executeQuery("Select * from teaches");

                    for (int j = 0; j < id_reg; j++) {
                        rs.next();
                    }
                    id_reg = rs.getInt(1);

                    int idteacher, idlesson;
                    idteacher = rs.getInt(3);
                    idlesson = rs.getInt(2);

                    rs = stmt.executeQuery("Select name from lesson where idlesson = " + idlesson);
                    rs.next();
                    String lessonname = rs.getString(1);

                    String teachername;
                    rs = stmt.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                    rs.next();
                    teachername = rs.getString(9) + "," + rs.getString(10);
                    tflesson.setText(lessonname);
                    tfname.setText(teachername);

                } catch (SQLException ex) {
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

    private void updateToolbarButtonsState(int iCase) {
        switch (iCase) {
            case 0:
                for (int i = 0; i < 13; i++) {
                    tb.setBm_state(i, true);
                }
                tb.setBm_state(0, false);
                tb.setBm_state(1, false);
                tb.setBm_state(5, false);
                tb.setBm_state(6, false);
                break;
            case 1:

                if (global_pos == 1) {
                    tb.setBm_state(0, false);
                    tb.setBm_state(1, false);
                } else {
                    tb.setBm_state(0, true);
                    tb.setBm_state(1, true);
                }
                break;
            case 2:
                tb.setBm_state(0, true);
                tb.setBm_state(1, true);
                break;
            case 3:
                tb.setBm_state(0, true);
                tb.setBm_state(1, true);
                tb.setBm_state(2, false);
                tb.setBm_state(3, false);
                tb.setBm_state(4, true);
                tb.setBm_state(5, false);
                tb.setBm_state(6, false);
                tb.setBm_state(7, true);
                tb.setBm_state(8, true);
                tb.setBm_state(9, true);
                tb.setBm_state(10, true);
                tb.setBm_state(11, true);
                tb.setBm_state(12, true);
                break;
            case 4:
                button_start_setup();
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
            rs2 = stmt.executeQuery("SELECT idusers FROM users WHERE lastname = " + "'" + ql + "'");
            if (rs2.next()) {

                id_search = rs2.getInt(1);

                rs = stmt.executeQuery("select * from teaches where teacher_idteacher = " + id_search);

                if (rs.next()) {

                    pn_buttoncheck();

                    int idteacher, idlesson;
                    idteacher = rs.getInt(3);
                    idlesson = rs.getInt(2);
                    System.out.println(idlesson);
                    System.out.println("Teacher id: " + idteacher);
                    rs2 = stmt2.executeQuery("Select name from lesson where idlesson = " + idlesson);
                    rs2.next();
                    String lessonname = rs2.getString(1);

                    String teachername;
                    rs2 = stmt2.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                    rs2.next();
                    teachername = rs2.getString(9) + "," + rs2.getString(10);
                    tflesson.setText(lessonname);
                    tfname.setText(teachername);
                }
            } else {
                JOptionPane.showMessageDialog(this, " Teacher Not Found", "Search", JOptionPane.INFORMATION_MESSAGE);
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
                int idteacher, idlesson;
                idteacher = rs.getInt(3);
                idlesson = rs.getInt(2);

                rs2 = stmt2.executeQuery("Select name from lesson where idlesson = " + idlesson);
                rs2.next();
                String lessonname = rs2.getString(1);

                String teachername;
                rs2 = stmt2.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                rs2.next();
                teachername = rs2.getString(9) + "," + rs2.getString(10);
                tflesson.setText(lessonname);
                tfname.setText(teachername);

            }
        } catch (SQLException ex) {
            String msg = "Error: " + ex.getMessage();
            System.out.println(msg);
        }
    }

    private void doSearchNext() {

        try {

            System.out.println("Next1");
            if (rs.next()) {
                System.out.println("Next2");
                pn_buttoncheck();
                int idteacher, idlesson;
                idteacher = rs.getInt(3);
                idlesson = rs.getInt(2);

                System.out.println(idlesson);
                System.out.println("Teacher id: " + idteacher);
                System.out.println("Next3");
                rs2 = stmt2.executeQuery("Select name from lesson where idlesson = " + idlesson);
                rs2.next();
                String lessonname = rs2.getString(1);

                String teachername;
                rs2 = stmt2.executeQuery("select * from teacher left join users on idteacher = idusers where idteacher = " + idteacher);
                rs2.next();
                teachername = rs2.getString(9) + "," + rs2.getString(10);
                tflesson.setText(lessonname);
                tfname.setText(teachername);

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

        JLabel slastname = new JLabel("TEACHER(lastname):", SwingConstants.RIGHT);

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
            rs = stmt.executeQuery("SELECT * FROM teaches ");
            // select all
            System.out.println("Test query complete.");
            rs.next();
        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }

    }

    private void button_yes_no_setup() {
        for (int i = 0; i < 13; i++) {
            tb.setBm_state(i, false);
        }
        tb.setBm_state(5, true);
        tb.setBm_state(6, true);
    }

    private void button_start_setup() {
        System.out.println("trexw");
        for (int i = 0; i < 13; i++) {
            tb.setBm_state(i, true);
        }
        tb.setBm_state(0, false);
        tb.setBm_state(1, false);
        tb.setBm_state(5, false);
        tb.setBm_state(6, false);
        
        if(global_total > 1) {
            tb.setBm_state(0, true);
            tb.setBm_state(1, true);
        }
        
        if(global_pos ==global_total) {
            tb.setBm_state(2, true);
            tb.setBm_state(3, true);
        }   
    }

    private int id_search;
    private CommonToolbar tb;
    private JPanel p_teach;
    private int global_pos;
    private int global_total;
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, rs2;
    private int id_reg;
    private int desired_id;
    private String unow, utotal;
    public JButton[] tm;
    private JLabel numbert;
    private JLabel lname;
    private JTextField tfname;
    private JTextField tflesson;
    public int action, modify_id;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel llesson;
    private JTextField slast;
}
