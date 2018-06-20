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

public class PrerequisiteDialog extends JDialog {

    public PrerequisiteDialog(Object o, boolean modal, int global_tpos) {
        global_pos = global_tpos;

        System.out.println("I m in the constructor of PrerequsiteDialog class");

        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;
            Method method1 = PrerequisiteDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(PrerequisiteDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        tb.hideButton(9);
        tb.hideButton(7);
        tb.hideButton(11);

        tb.hideButton(12);
        tb.setBm_state(0, false);
        tb.setBm_state(1, false);
        tb.setBm_state(5, false);
        tb.setBm_state(6, false);

        setLayout(new BorderLayout());

        p = new JPanel(new GridLayout(5, 2));
        unow = "1";
        utotal = "0";
        ResultSet bb = new DBConnection().Query("select count(*) from prerequisite");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbert = new JLabel(unow + " / " + utotal);

        llserial = new JLabel("Lesson:", SwingConstants.CENTER);
        lreq = new JLabel("Requirement:", SwingConstants.CENTER);

        ttfserial = new JTextField();   //int
        tflreq = new JTextField();  // int 

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM prerequisite");
            rs2.next();
            global_total = rs2.getInt(1);
            System.out.println("dbg");
            System.out.println("Calculated number of prereqs:");
            System.out.println(global_total);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = new DBConnection().Query("select * from prerequisite");
            rs.next();
            rowNumber = rs.getRow();
            Prerequisite st = new Prerequisite(rs.getInt(1), rs.getInt(2));
            idl = rs.getInt(2);
            idr = rs.getInt(1);

            rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
            rs.next();
            String sidl = rs.getString(1);

            rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
            rs.next();
            String sidr = rs.getString(1);

            ttfserial.setText(sidl);
            tflreq.setText(sidr);

            if (global_total == global_pos) {
                tb.setBm_state(2, false);
                tb.setBm_state(3, false);
            }

            p.add(llserial);
            p.add(ttfserial);
            p.add(lreq);
            p.add(tflreq);

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }

        add(tb, BorderLayout.NORTH);
        add(p, BorderLayout.CENTER);
        add(numbert, BorderLayout.SOUTH);
        tflreq.setEnabled(false);
        ttfserial.setEnabled(false);
        setSize(450, 320);
        setModal(modal);
        setTitle("Prerequisites");
        setVisible(true);
    }

    public void doFunctionCall(Integer __farg, JButton[] tm) {
        int farg = __farg;
        switch (farg) {

            case CommonToolbar.FIRST:
                doConnect();
                global_pos = 1;

                break;
            case CommonToolbar.PREVIOUS:
                try {
                    stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs = stmt.executeQuery("SELECT * from prerequisite");
                    rowNumber = rs.getRow();
                    for (int i = 0; i < global_pos; i++) {
                        rs.next();
                    }

                    if (rs.previous()) {

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

                        idl = rs.getInt(2);
                        idr = rs.getInt(1);

                        rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                        rs.next();
                        String sidl = rs.getString(1);

                        rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                        rs.next();
                        String sidr = rs.getString(1);

                        ttfserial.setText(sidl);
                        tflreq.setText(sidr);
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
                    rs = stmt.executeQuery("SELECT * FROM prerequisite");
                    rowNumber = rs.getRow();
                    for (int i = 0; i < global_pos; i++) {
                        rs.next();
                    }

                    if (rs.next()) {
                        System.out.println("Existing next");
                        global_pos++;

                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);
                        System.out.println(global_total);
                        if (global_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        idl = rs.getInt(2);
                        idr = rs.getInt(1);

                        rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                        rs.next();
                        String sidl = rs.getString(1);

                        rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                        rs.next();
                        String sidr = rs.getString(1);

                        ttfserial.setText(sidl);
                        tflreq.setText(sidr);
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
                    rs = stmt.executeQuery("SELECT * FROM prerequisite");

                    rs.last();
                    rowNumber = rs.getRow();
                    global_pos = global_total;

                    String unow, utotal;
                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    if (global_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }
                    idl = rs.getInt(2);
                    idr = rs.getInt(1);
                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                    rs.next();
                    String sidl = rs.getString(1);
                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                    rs.next();
                    String sidr = rs.getString(1);
                    ttfserial.setText(sidl);
                    tflreq.setText(sidr);
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.ADD:
                // New //

                doConnect();

                try {

                    JDialog ins = new JDialog();
                    ins.setLayout(new GridLayout(3, 2));

                    JComboBox ll, rr;
                    ll = new JComboBox();
                    rr = new JComboBox();
                    JLabel labell, labelr;
                    labell = new JLabel("Lesson:", SwingConstants.RIGHT);
                    labelr = new JLabel("Required:", SwingConstants.RIGHT);
                    ArrayList<String> al = new ArrayList<String>();
                    ArrayList<String> ar = new ArrayList<String>();

                    String tempreq;

                    rs = stmt.executeQuery("select * from lesson");
                    String tles;
                    while (rs.next()) {
                        System.out.println("Element to insert on ComboBox");
                        tles = rs.getString(3);
                        String toinsert = tles;
                        ar.add(toinsert);
                    }
                    int arraylen = ar.size();
                    String[] userlf = ar.stream().toArray(String[]::new);

                    rs = stmt.executeQuery("select * from lesson");
                    String tles2;
                    while (rs.next()) {
                        System.out.println("Element to insert on ComboBox");
                        tles2 = rs.getString(3);
                        String toinsert = tles2;
                        al.add(toinsert);
                    }
                    int arraylen2 = al.size();
                    String[] userlf2 = al.stream().toArray(String[]::new);
                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    DefaultComboBoxModel model2 = new DefaultComboBoxModel(userlf2);

                    ll.setModel(model);
                    rr.setModel(model2);

                    ins.add(labell);
                    ins.add(ll);
                    ins.add(labelr);
                    ins.add(rr);
                    JButton okb = new JButton("ok");
                    ins.add(okb);

                    okb.addActionListener(new ActionListener() { //insert prerequisites
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String namel, namer;
                            int possitl = ll.getSelectedIndex();
                            int possitr = rr.getSelectedIndex();
                            Object ol = ll.getModel().getElementAt(possitl);
                            namel = ol.toString();
                            Object or = rr.getModel().getElementAt(possitr);
                            namer = or.toString();

                            try {
                                rs = stmt.executeQuery("select idlesson from lesson where description = " + "'" + namel + "'");
                                rs.first();
                                int id_lesson = rs.getInt(1);

                                rs = stmt.executeQuery("select idlesson from lesson where description = " + "'" + namer + "'");
                                rs.first();
                                int id_req = rs.getInt(1);
                                String sql2 = "INSERT INTO prerequisite VALUES " + "(" + id_req + "," + id_lesson + ")";
                                stmt.executeUpdate(sql2);
                                JOptionPane.showMessageDialog(null, "Done", "Prerequisite Insertion", JOptionPane.INFORMATION_MESSAGE);
                                global_total++;
                                String unow, utotal;

                                unow = Integer.toString(global_pos);
                                utotal = Integer.toString(global_total);
                                numbert.setText(unow + " / " + utotal);
                                ins.dispose();
                                //is it ok?

                                rs = stmt.executeQuery("SELECT * FROM prerequisite");
                                rs.next();
                                rowNumber = rs.getRow();
                                idl = rs.getInt(2);
                                idr = rs.getInt(1);

                                rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                                rs.next();
                                String sidl = rs.getString(1);

                                rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                                rs.next();
                                String sidr = rs.getString(1);
                                ttfserial.setText(sidl);
                                tflreq.setText(sidr);
                                System.out.println("Print to button setup");

                                button_start_setup();
                                global_pos = 1;
                                if (global_pos == 1) {
                                    tm[1].setEnabled(false);
                                }

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
                break;
            case CommonToolbar.CANCEL:
                //cancel

                button_start_setup();

                if (global_pos > 1) {
                    tm[0].setEnabled(true);
                }

                doConnect();

                try {
                    rs = stmt.executeQuery("SELECT * FROM prerequisite");
                    System.out.println("my possition is: " + global_pos);

                    for (int i = 0; i < global_pos; i++) {
                        if (rs.next())
                            ;
                    }
                    rowNumber = rs.getRow();
                    String unow, utotal;
                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    idl = rs.getInt(2);
                    idr = rs.getInt(1);

                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                    rs.next();
                    String sidl = rs.getString(1);

                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                    rs.next();
                    String sidr = rs.getString(1);

                    ttfserial.setText(sidl);
                    tflreq.setText(sidr);

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

                if (action == 7) {

                    // Get changed details.
                    System.out.println("Just pressed OK button");
                    String sem, am;

                    am = tfam.getText();
                    sem = tfsemester.getText();

                    String sql2 = "update student " + "set  AM =" + "'" + am + "'" + ", examino=" + sem + " where idstudent = " + modify_id;
                    try {
                        stmt.executeUpdate(sql2);

                        if (global_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        if (global_pos > 1) {
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

                    JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                    global_total--;
                    String unow, utotal;

                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);
                    doFunctionCall(0, tm);

                }
                break;
            case CommonToolbar.MODIFY:

                break;
            case CommonToolbar.DELETE:

                action = 8;
                System.out.println("dsds");
                try {

                    rs = stmt.executeQuery("select * from prerequisite");

                    for (int j = 0; j < rowNumber; j++) {
                        rs.next();
                    }

                    rs.deleteRow();
                    System.out.println("deleted from prerequisite");

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }

                System.out.println("Desired ID to be deleted: " + delr + " " + dell);
                break;
            case CommonToolbar.REFRESH:

                try {

                    rs = stmt.executeQuery("SELECT * FROM prerequisite");

                    for (int j = 0; j < rowNumber; j++) {
                        rs.next();
                    }
                    if (rs.isBeforeFirst()) {
                        rs.next();
                    }

                    rowNumber = rs.getRow();

                    idl = rs.getInt(2);
                    idr = rs.getInt(1);

                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                    rs.next();
                    String sidl = rs.getString(1);

                    rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                    rs.next();
                    String sidr = rs.getString(1);
                    ttfserial.setText(sidl);
                    tflreq.setText(sidr);

                } catch (SQLException ex) {
                    System.out.println("Its me your lovely bug");
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                System.out.println("Refresh completed");
                break;
            case CommonToolbar.SEARCH: // search for lesson with Prerequisite..

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

            rs = stmt.executeQuery("SELECT * FROM lesson WHERE name = " + "'" + ql + "'");
            if (rs.next()) {
                search_id = rs.getInt(1);

                rs = stmt.executeQuery("select * from prerequisite where lesson_idlesson1 = " + search_id);
                rs.first();
                String sidl, sidr;
                idl = rs.getInt(2);
                idr = rs.getInt(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                rs.next();
                sidl = rs.getString(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                rs.next();
                sidr = rs.getString(1);

                ttfserial.setText(sidl);
                tflreq.setText(sidr);

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
                String sidl, sidr;
                idl = rs.getInt(2);
                idr = rs.getInt(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                rs.next();
                sidl = rs.getString(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                rs.next();
                sidr = rs.getString(1);

                ttfserial.setText(sidl);
                tflreq.setText(sidr);

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
                String sidl, sidr;
                idl = rs.getInt(2);
                idr = rs.getInt(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
                rs.next();
                sidl = rs.getString(1);

                rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
                rs.next();
                sidr = rs.getString(1);

                ttfserial.setText(sidl);
                tflreq.setText(sidr);

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

        slastname = new JLabel("Lesson", SwingConstants.RIGHT);

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
            rs = stmt.executeQuery("SELECT * FROM prerequisite");
            // select all
            System.out.println("Test query complete.");
            rs = stmt.executeQuery("SELECT * FROM prerequisite");
            rs.next();

            Prerequisite st = new Prerequisite(rs.getInt(1), rs.getInt(2));

            idl = rs.getInt(2);
            idr = rs.getInt(1);

            rs = stmt.executeQuery("select name from lesson where idlesson = " + idl);
            rs.next();
            String sidl = rs.getString(1);

            rs = stmt.executeQuery("select name from lesson where idlesson = " + idr);
            rs.next();
            String sidr = rs.getString(1);

            ttfserial.setText(sidl);
            tflreq.setText(sidr);
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
        for (int i = 0; i < 13; i++) {
            tb.setBm_state(i, true);
        }
        tb.setBm_state(0, false);
        tb.setBm_state(5, false);
        tb.setBm_state(6, false);
    }

    private int search_id;
    private int rowNumber;
    private int global_pos;
    private int global_total;
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, rs2;
    private int dell, delr;
    private int desired_id;
    private JComboBox cbuser;
    private int idl, idr;
    public JButton[] tm;
    private JLabel lreq;
    private String unow, utotal;
    private JLabel numbert;
    private JPanel p;
    private CommonToolbar tb;
    private JLabel llserial;
    private JTextField tfam, tfsemester;
    private JTextField ttfserial;
    public int action, modify_id;
    private JTextField tflreq;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel slastname;
    private JTextField slast;
}
