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

//to fix:   Na ginei search kai table opos se ola,
// exei anapoda ta orismata name kai description pros stigmin.
public class LessonsDialog extends JDialog {

    public LessonsDialog(Object o, boolean modal, int global_tpos) {
        global_pos = global_tpos;
        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;
            Method method1 = LessonsDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb_lesson = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(LessonsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        tb_lesson.setBm_state(0, false);
        tb_lesson.setBm_state(1, false);
        tb_lesson.setBm_state(5, false);
        tb_lesson.setBm_state(6, false);
        tb_lesson.hideButton(CommonToolbar.TABLE);
        tb_lesson.hideButton(CommonToolbar.REPLICATE);
        setLayout(new BorderLayout());

        p_lesson = new JPanel(new GridLayout(5, 2));

        unow = "1";
        utotal = "0";
        ResultSet bb = new DBConnection().Query("select count(*) from lesson");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbert = new JLabel(unow + " / " + utotal);

        llserial = new JLabel("LessonID:", SwingConstants.RIGHT);
        ldesc = new JLabel("Description", SwingConstants.RIGHT);
        lname = new JLabel("Name:", SwingConstants.RIGHT);

        ttfserial = new JTextField();   //int
        ttfserial.setEnabled(false);    //varchar
        tfdesc = new JTextField();
        tfname = new JTextField();  // int 

        int tempid;
        String tempname, tempdesc;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM lesson");
            rs2.next();
            global_total = rs2.getInt(1);
            System.out.println("dbg");
            System.out.println("Calculated number of lessons:");
            System.out.println(global_total);

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM lesson");
            rs.next();

            Lesson st = new Lesson(rs.getInt(1), rs.getString(3), rs.getString(2));

            rs.beforeFirst();

            tempid = st.getID();
            tempdesc = st.getDesc();
            tempname = st.getName();

            ttfserial.setText(Integer.toString(tempid));
            tfdesc.setText(tempdesc);
            tfname.setText(tempname);

            p_lesson.add(llserial);
            p_lesson.add(ttfserial);
            p_lesson.add(ldesc);
            p_lesson.add(tfdesc);
            p_lesson.add(lname);
            p_lesson.add(tfname);

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }
        add(tb_lesson, BorderLayout.NORTH);
        add(p_lesson, BorderLayout.CENTER);
        add(numbert, BorderLayout.SOUTH);

        setSize(450, 320);
        setModal(modal);
        setTitle("Lessons");
        setVisible(true);
    }

    public void doFunctionCall(Integer __farg, JButton[] tm) {
        int farg = __farg;
        switch (farg) {

            case CommonToolbar.FIRST:
                doConnect();
                global_pos = 1;

                unow = Integer.toString(global_pos);
                utotal = Integer.toString(global_total);
                numbert.setText(unow + " / " + utotal);

                try {
                    if (rs.next()) {

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfdesc.setText(rs.getString(3));
                        tfname.setText(rs.getString(2));

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
                    rs = stmt.executeQuery("SELECT * FROM lesson");

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

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfdesc.setText(rs.getString(3));
                        tfname.setText(rs.getString(2));
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
                    rs = stmt.executeQuery("SELECT * FROM lesson");

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

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfdesc.setText(rs.getString(3));
                        tfname.setText(rs.getString(2));
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
                    rs = stmt.executeQuery("SELECT * FROM lesson");

                    rs.last();

                    global_pos = global_total;

                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    if (global_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfdesc.setText(rs.getString(3));
                    tfname.setText(rs.getString(2));

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

                    rs = stmt.executeQuery("select * from lesson order by idlesson desc");
                    rs.first();
                    desired_id = rs.getInt(1) + 1;
                    System.out.println("Desired_id: " + desired_id);

                    ttfserial.setText(Integer.toString(desired_id));
                    ttfserial.setEnabled(false);
                    tfname.setText("");
                    tfdesc.setText("");

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
                    rs = stmt.executeQuery("SELECT * FROM lesson");
                    System.out.println("my possition is: " + global_pos);

                    for (int i = 0; i < global_pos; i++) {
                        if (rs.next())
                            ;
                    }
                    unow = Integer.toString(global_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfdesc.setText(rs.getString(3));
                    tfname.setText(rs.getString(2));

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
                    String desc, ename;

                    ename = tfname.getText();
                    desc = tfdesc.getText();

                    String sql2 = "update lesson set idlesson = " + modify_id + ", name =" + "'" + ename + "'" + ", description =" + "'" + desc + "'" + " where idlesson = " + modify_id;

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
                    System.out.println("To be deleted is: ");
                    System.out.println(desired_id);
                    String sql2 = "DELETE FROM lesson WHERE idlesson= " + desired_id + "";

                    try {
                        String sqlrena = "delete from prerequisite where lesson_idlesson =" + desired_id + " or lesson_idlesson1 = " + desired_id + "";
                        String sqlrduo = "delete from teaches where lesson_idlesson =" + desired_id + "";
                        String sqlrtria = "delete from enrollment where lesson_idlesson =" + desired_id + "";
                        stmt.executeUpdate(sqlrena);
                        stmt.executeUpdate(sqlrduo);
                        stmt.executeUpdate(sqlrtria);

                        System.out.println("esvisa ta prota eksartomena apo alla tables");
                        stmt.executeUpdate(sql2);

                        JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                        global_total--;
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow+" / "+utotal);
                        doFunctionCall(0, tm);

                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                } else if (action == 5) {

                    String sem, am;

                    am = tfdesc.getText();
                    sem = tfname.getText();

                    String sql2 = "INSERT INTO lesson VALUES " + "(" + desired_id + "," + "'" + sem + "'" + "," + "'" + am + "'" + ")";
                    System.out.println(sql2);

                    try {
                        stmt.executeUpdate(sql2);
                        JOptionPane.showMessageDialog(this, "Done", "Lesson Insertion", JOptionPane.INFORMATION_MESSAGE);
                        global_total++;
                        unow = Integer.toString(global_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow+" / "+utotal);
                        doConnect();

                        try {
                            rs = stmt.executeQuery("SELECT * FROM lesson");
                            System.out.println("my possition is: " + global_pos);

                            global_pos = 1;
                            for (int i = 0; i < global_total; i++) {
                                if (rs.next()) {
                                    global_pos++;
                                }
                            }
                            System.out.println("Eftasa stin : " + global_pos);
                            global_pos--;
                            unow = Integer.toString(global_pos);
                            utotal = Integer.toString(global_total);
                            //number.setText(unow+" / "+utotal);

                            ttfserial.setText(Integer.toString(rs.getInt(1)));
                            tfdesc.setText(rs.getString(3));
                            tfname.setText(rs.getString(2));

                            System.out.println(global_pos);
                            System.out.println(global_total);
                            if (global_pos == global_total) {
                                tm[0].setEnabled(true);
                                tm[1].setEnabled(true);
                                tm[2].setEnabled(false);
                                tm[3].setEnabled(false);
                            } else if ((global_pos < global_total) && (global_pos > 1)) {
                                tm[2].setEnabled(true);
                                tm[3].setEnabled(true);
                            }
                            if (global_pos > 1) {
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
                    if (rs.isBeforeFirst()) {
                        rs.next();
                    }
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
                    if (rs.isBeforeFirst()) {
                        rs.next();
                    }
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

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfdesc.setText(rs.getString(3));
                    tfname.setText(rs.getString(2));

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
            case CommonToolbar.REPLICATE:
                //Unhandled yet.
                break;
            default:
                System.out.println("Unhandled");
        }
        updateToolbarButtonsState(farg);
    }

    private void updateToolbarButtonsState(int iCase) {
        switch (iCase) {
            case 0:
                for (int i = 0; i < 13; i++) {
                    tb_lesson.setBm_state(i, true);
                }
                tb_lesson.setBm_state(0, false);
                tb_lesson.setBm_state(1, false);
                tb_lesson.setBm_state(5, false);
                tb_lesson.setBm_state(6, false);
                break;
            case 1:

                if (global_pos == 1) {
                    tb_lesson.setBm_state(0, false);
                    tb_lesson.setBm_state(1, false);
                } else {
                    tb_lesson.setBm_state(0, true);
                    tb_lesson.setBm_state(1, true);
                }
                break;
            case 2:
                tb_lesson.setBm_state(0, true);
                tb_lesson.setBm_state(1, true);
                break;
            case 3:
                tb_lesson.setBm_state(0, true);
                tb_lesson.setBm_state(1, true);
                tb_lesson.setBm_state(2, false);
                tb_lesson.setBm_state(3, false);
                tb_lesson.setBm_state(4, true);
                tb_lesson.setBm_state(5, false);
                tb_lesson.setBm_state(6, false);
                tb_lesson.setBm_state(7, true);
                tb_lesson.setBm_state(8, true);
                tb_lesson.setBm_state(9, true);
                tb_lesson.setBm_state(10, true);
                tb_lesson.setBm_state(11, true);
                tb_lesson.setBm_state(12, true);
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

            rs = stmt.executeQuery("SELECT * FROM lesson  WHERE name = " + "'" + ql + "'");

            if (rs.next()) {

                pn_buttoncheck();

                ttfserial.setText(Integer.toString(rs.getInt(1)));
                tfdesc.setText(rs.getString(3));
                tfname.setText(rs.getString(2));

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
                tfdesc.setText(rs.getString(3));
                tfname.setText(rs.getString(2));

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
                tfdesc.setText(rs.getString(2));
                tfname.setText(rs.getString(3));
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

        slastname = new JLabel("Lesson Name", SwingConstants.RIGHT);

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
            rs = stmt.executeQuery("SELECT * FROM lesson");
            // select all
            System.out.println("Test query complete.");

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }
    }

    private void button_yes_no_setup() {
        for (int i = 0; i < 13; i++) {
            tb_lesson.setBm_state(i, false);
        }
        tb_lesson.setBm_state(5, true);
        tb_lesson.setBm_state(6, true);
    }

    private void button_start_setup() {
        for (int i = 0; i < 13; i++) {
            tb_lesson.setBm_state(i, true);
        }
        tb_lesson.setBm_state(0, false);
        tb_lesson.setBm_state(5, false);
        tb_lesson.setBm_state(6, false);
    }

    private int global_pos;
    private int global_total;
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, rs2;
    private int desired_id;
    private JComboBox cbuser;
    public JButton[] tm;
    private String unow, utotal;
    private JLabel numbert;
    private JPanel p_lesson;
    private CommonToolbar tb_lesson;
    private JLabel ldesc, lname, llserial;
    private JTextField tfdesc, tfname;
    private JTextField ttfserial;
    public int action, modify_id;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel slastname;
    private JTextField slast;
}
