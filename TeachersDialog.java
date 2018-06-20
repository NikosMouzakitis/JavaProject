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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TeachersDialog extends JDialog {

    public TeachersDialog(Object o, boolean modal, int global_tpos) {
        global_teacher_pos = global_tpos;
        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {

        try {
            Class[] parameterTypes = new Class[2];

            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;

            Method method1 = TeachersDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb_teachers = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {

            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        tb_teachers.setBm_state(0, false);
        tb_teachers.setBm_state(1, false);
        tb_teachers.setBm_state(5, false);
        tb_teachers.setBm_state(6, false);
        tb_teachers.hideButton(CommonToolbar.REPLICATE);
        tb_teachers.hideButton(CommonToolbar.TABLE);
        setLayout(new BorderLayout());

        p_teachers = new JPanel(new GridLayout(5, 2));
        unow = "1";
        utotal = "0";

        ResultSet bb = new DBConnection().Query("select count(*) from teacher");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        numbert = new JLabel(unow + " / " + utotal);

        labels = new JLabel[LABELS_LEN];
        labels[LABELS_SERIAL] = new JLabel("Serial:", SwingConstants.RIGHT);

        labels[LABELS_USER] = new JLabel("User", SwingConstants.RIGHT);
        labels[LABELS_SPECIAL] = new JLabel("Specialization:", SwingConstants.RIGHT);
        labels[LABELS_INTERESTS] = new JLabel("Interests:", SwingConstants.RIGHT);
        labels[LABELS_PHONE] = new JLabel("Phone:", SwingConstants.RIGHT);

        ttfserial = new JTextField();
        ttfserial.setEnabled(false);
        tfphone = new JTextField();
        tfspecial = new JTextField();
        tfinterests = new JTextField();

        JTextField tmpFields[] = {ttfserial, tfphone, tfspecial, tfinterests};

        String tempspecialty, tempemail, templogin, temppass, templast, tempfirst, tempinterests, tempphone;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");
            rs2.next();
            global_total = rs2.getInt(1);
            System.out.println("dbg");

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");

            // select all
            int tempid;
            System.out.println("den mpenw pote edw.");
            if (rs.next()) {
                tempid = rs.getInt(1);
                tempspecialty = rs.getString(2);
                tempphone = rs.getString(4);
                tempinterests = rs.getString(3);
                tempemail = rs.getString(8);
                temppass = rs.getString(7);
                templast = rs.getString(9);
                tempfirst = rs.getString(10);
                templogin = rs.getString(6);
                Teachers tmp = new Teachers(tempid, tempspecialty, tempinterests, tempphone, templogin, temppass, tempemail, templast, tempfirst);

                ttfserial.setText(Integer.toString(tmp.getID()));

                tfspecial.setText(tmp.getSpecialty());
                tfinterests.setText(tmp.getInterests());
                tfphone.setText(tmp.getTel());

                String dis = templast + " , " + tempfirst;
                System.out.println(dis);
                String[] userlf = {dis};
                cbuser = new JComboBox(userlf);

                p_teachers.add(labels[0]);
                p_teachers.add(ttfserial);
                p_teachers.add(labels[1]);
                p_teachers.add(cbuser);
                p_teachers.add(labels[2]);
                p_teachers.add(tfphone);
                p_teachers.add(labels[3]);
                p_teachers.add(tfspecial);
                p_teachers.add(labels[4]);
                p_teachers.add(tfinterests);
            }
            System.out.println("ok1");
        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }

        System.out.println("ok2");
        add(tb_teachers, BorderLayout.NORTH);
        add(p_teachers, BorderLayout.CENTER);
        add(numbert, BorderLayout.SOUTH);

        setSize(450, 320);
        setModal(modal);
        setTitle("Teachers");
        setVisible(true);

        System.out.println("ok3");
    }

    public void doFunctionCall(Integer __farg, JButton[] tm) {
        int farg = __farg;
        System.out.println("farg: " + Integer.toString(farg));
        switch (farg) {

            case CommonToolbar.FIRST:
                doConnect();
                global_teacher_pos = 1;
                int tempid;
                unow = Integer.toString(global_teacher_pos);
                utotal = Integer.toString(global_total);
                numbert.setText(unow + " / " + utotal);

                String tempspecialty,
                 tempemail,
                 templogin,
                 temppass,
                 templast,
                 tempfirst,
                 tempinterests,
                 tempphone;
                try {
                    if (rs.next()) {

                        tempid = rs.getInt(1);
                        tempspecialty = rs.getString(2);
                        tempphone = rs.getString(4);
                        tempinterests = rs.getString(3);
                        tempemail = rs.getString(8);
                        temppass = rs.getString(7);
                        templast = rs.getString(9);
                        tempfirst = rs.getString(10);
                        templogin = rs.getString(6);
                        Teachers tmp = new Teachers(tempid, tempspecialty, tempinterests, tempphone, templogin, temppass, tempemail, templast, tempfirst);
                        ttfserial.setText(Integer.toString(tmp.getID()));
                        tfspecial.setText(tmp.getSpecialty());
                        tfinterests.setText(tmp.getInterests());
                        tfphone.setText(tmp.getTel());
                        String dis = templast + " , " + tempfirst;
                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        unow = Integer.toString(global_teacher_pos);
                        utotal = Integer.toString(global_total);

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
                    rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");

                    for (int i = 0; i < global_teacher_pos; i++) {
                        rs.next();
                    }

                    if (rs.previous()) {

                        global_teacher_pos--;
                        String unow, utotal;
                        unow = Integer.toString(global_teacher_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        if (global_teacher_pos < global_total) {
                            tm[2].setEnabled(true);
                            tm[3].setEnabled(true);
                        }
                        if (global_teacher_pos == 1) {
                            tm[0].setEnabled(false);
                            tm[1].setEnabled(false);
                        }

                        templast = rs.getString(9);
                        tempfirst = rs.getString(10);
                        String dis = templast + " , " + tempfirst;

                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfspecial.setText(rs.getString(2));
                        tfinterests.setText(rs.getString(3));
                        tfphone.setText(rs.getString(4));

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
                    rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");

                    for (int i = 0; i < global_teacher_pos; i++) {
                        rs.next();
                    }

                    if (rs.next()) {
                        System.out.println("Existing next");
                        global_teacher_pos++;

                        unow = Integer.toString(global_teacher_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);
                        System.out.println(global_total);
                        if (global_teacher_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        templast = rs.getString(9);
                        tempfirst = rs.getString(10);
                        String dis = templast + " , " + tempfirst;

                        String[] userlf = {dis};

                        DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                        cbuser.setModel(model);

                        ttfserial.setText(Integer.toString(rs.getInt(1)));
                        tfspecial.setText(rs.getString(2));
                        tfinterests.setText(rs.getString(3));
                        tfphone.setText(rs.getString(4));

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
                    rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");

                    rs.last();

                    global_teacher_pos = global_total;

                    String unow, utotal;

                    unow = Integer.toString(global_teacher_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    if (global_teacher_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    templast = rs.getString(9);
                    tempfirst = rs.getString(10);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfspecial.setText(rs.getString(2));
                    tfinterests.setText(rs.getString(3));
                    tfphone.setText(rs.getString(4));

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

                    rs = stmt.executeQuery("select * from teacher order by idteacher desc");
                    rs.first();
                    desired_id = rs.getInt(1) + 1;
                    System.out.println("Desired_id: " + desired_id);

                    rs = stmt.executeQuery("SELECT * FROM users left join teacher\n"
                            + "on idusers = teacher.idteacher\n"
                            + "WHERE (teacher.idteacher IS NULL) and users.idusers not in (select idusers from users  left join student\n"
                            + "on idusers = student.idstudent\n"
                            + "WHERE student.idstudent = users.idusers) ");

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

                    tfspecial.setText("");
                    tfinterests.setText("");
                    tfphone.setText("");

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;
            case CommonToolbar.CANCEL:
                //cancel

                button_start_setup();

                if (global_teacher_pos > 1) {
                    tm[0].setEnabled(true);
                }

                doConnect();

                try {
                    rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");
                    System.out.println("my possition is: " + global_teacher_pos);

                    for (int i = 0; i < global_teacher_pos; i++) {
                        if (rs.next())
                            ;
                    }
                    String unow, utotal;

                    unow = Integer.toString(global_teacher_pos);
                    utotal = Integer.toString(global_total);
                    numbert.setText(unow + " / " + utotal);

                    templast = rs.getString(9);
                    tempfirst = rs.getString(10);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfspecial.setText(rs.getString(2));
                    tfinterests.setText(rs.getString(3));
                    tfphone.setText(rs.getString(4));

                    if (global_teacher_pos == global_total) {
                        tm[2].setEnabled(false);
                        tm[3].setEnabled(false);
                    }

                    if (global_teacher_pos > 1) {
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
                    String eid, end, tilef;

                    eid = tfspecial.getText();
                    end = tfinterests.getText();
                    tilef = tfphone.getText();

                    String sql2 = "update teacher " + "set  eidikotita=" + "'" + eid + "'" + ", endiaferonta=" + "'" + end + "'" + ",tilefono=" + "'" + tilef
                            + "'" + "where idteacher = " + modify_id;
                    try {
                        stmt.executeUpdate(sql2);

                        if (global_teacher_pos == global_total) {
                            tm[2].setEnabled(false);
                            tm[3].setEnabled(false);
                        }

                        if (global_teacher_pos > 1) {
                            tm[0].setEnabled(true);
                            tm[1].setEnabled(true);
                        } else {
                            System.out.println("Kleinw ta koumpia");
                            tm[0].setEnabled(false);
                            tm[1].setEnabled(false);
                        }
                        System.out.println(eid);
                        System.out.println(end);
                        System.out.println(tilef);
                        JOptionPane.showMessageDialog(this, "Done", "Update", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        String msg = "Error: " + ex.getMessage();
                        System.out.println(msg);
                    }

                } else if (action == 8) { // coming to delete.
                    String sql2 = "DELETE FROM teacher WHERE idteacher= " + desired_id + "";

                    try {
                        stmt.executeUpdate(sql2);

                        JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                        global_total--;
                        String unow, utotal;

                        unow = Integer.toString(global_teacher_pos);
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

                    String eid, end, til;

                    eid = tfspecial.getText();
                    end = tfinterests.getText();
                    til = tfphone.getText();

                    String sql2 = "INSERT INTO teacher VALUES "
                            + "(" + uid + "," + "'" + eid + "'" + "," + "'" + end + "'" + "," + "'" + til + "'" + ")";
                    System.out.println(sql2);

                    try {
                        stmt.executeUpdate(sql2);
                        JOptionPane.showMessageDialog(this, "Done", "Teacher Insertion", JOptionPane.INFORMATION_MESSAGE);
                        global_total++;
                        String unow, utotal;

                        unow = Integer.toString(global_teacher_pos);
                        utotal = Integer.toString(global_total);
                        numbert.setText(unow + " / " + utotal);

                        doConnect();

                        try {
                            rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");
                            System.out.println("my possition is: " + global_teacher_pos);

                            global_teacher_pos = 1;
                            for (int i = 0; i < global_total; i++) {
                                if (rs.next()) {
                                    global_teacher_pos++;
                                }
                            }
                            System.out.println("Eftasa stin : " + global_teacher_pos);
                            global_teacher_pos--;
                            unow = Integer.toString(global_teacher_pos);
                            utotal = Integer.toString(global_total);
                            //number.setText(unow+" / "+utotal);

                            templast = rs.getString(9);
                            tempfirst = rs.getString(10);
                            String dis = templast + " , " + tempfirst;
                            String[] userlf = {dis};

                            DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                            cbuser.setModel(model);

                            ttfserial.setText(Integer.toString(rs.getInt(1)));
                            tfspecial.setText(rs.getString(2));
                            tfinterests.setText(rs.getString(3));
                            tfphone.setText(rs.getString(4));
                            System.out.println(global_teacher_pos);
                            System.out.println(global_total);
                            if (global_teacher_pos == global_total) {
                                tm[0].setEnabled(true);
                                tm[1].setEnabled(true);
                                tm[2].setEnabled(false);
                                tm[3].setEnabled(false);
                            } else if ((global_teacher_pos < global_total) && (global_teacher_pos > 1)) {
                                tm[2].setEnabled(true);
                                tm[3].setEnabled(true);
                            }
                            if (global_teacher_pos > 1) {
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
                    templast = rs.getString(9);
                    tempfirst = rs.getString(10);
                    String dis = templast + " , " + tempfirst;

                    String[] userlf = {dis};

                    DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                    cbuser.setModel(model);

                    ttfserial.setText(Integer.toString(rs.getInt(1)));
                    tfspecial.setText(rs.getString(2));
                    tfinterests.setText(rs.getString(3));
                    tfphone.setText(rs.getString(4));

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
            rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers and lastname = " + "'" + ql + "'");

            if (rs.next()) {

                pn_buttoncheck();

                ttfserial.setText(Integer.toString(rs.getInt(1)));
                tfspecial.setText(rs.getString(2));
                tfinterests.setText(rs.getString(3));
                tfphone.setText(rs.getString(4));
                String templast = rs.getString(9);
                String tempfirst = rs.getString(10);
                String dis = templast + " , " + tempfirst;

                String[] userlf = {dis};

                DefaultComboBoxModel model = new DefaultComboBoxModel(userlf);
                cbuser.setModel(model);

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
                tfspecial.setText(rs.getString(2));
                tfinterests.setText(rs.getString(3));
                tfphone.setText(rs.getString(4));
                String templast = rs.getString(9);
                String tempfirst = rs.getString(10);
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
                tfspecial.setText(rs.getString(2));
                tfinterests.setText(rs.getString(3));
                tfphone.setText(rs.getString(4));
                String templast = rs.getString(9);
                String tempfirst = rs.getString(10);
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
            rs = stmt.executeQuery("SELECT * FROM teacher left join users on idusers = teacher.idteacher WHERE teacher.idteacher = users.idusers");
            // select all
            System.out.println("Test query complete.");

        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }

    }

    private void button_yes_no_setup() {
        for (int i = 0; i < 13; i++) {
            tb_teachers.setBm_state(i, false);
        }
        tb_teachers.setBm_state(5, true);
        tb_teachers.setBm_state(6, true);
    }

    private void button_start_setup() {
        for (int i = 0; i < 13; i++) {
            tb_teachers.setBm_state(i, true);
        }
        tb_teachers.setBm_state(0, false);
        tb_teachers.setBm_state(5, false);
        tb_teachers.setBm_state(6, false);
    }

    private void updateToolbarButtonsState(int iCase) {
        switch (iCase) {
            case 0:
                for (int i = 0; i < 13; i++) {
                    tb_teachers.setBm_state(i, true);
                }
                tb_teachers.setBm_state(0, false);
                tb_teachers.setBm_state(1, false);
                tb_teachers.setBm_state(5, false);
                tb_teachers.setBm_state(6, false);
                break;
            case 1:

                if (global_teacher_pos == 1) {
                    tb_teachers.setBm_state(0, false);
                    tb_teachers.setBm_state(1, false);
                } else {
                    tb_teachers.setBm_state(0, true);
                    tb_teachers.setBm_state(1, true);
                }
                break;
            case 2:
                tb_teachers.setBm_state(0, true);
                tb_teachers.setBm_state(1, true);
                break;
            case 3:
                tb_teachers.setBm_state(0, true);
                tb_teachers.setBm_state(1, true);
                tb_teachers.setBm_state(2, false);
                tb_teachers.setBm_state(3, false);
                tb_teachers.setBm_state(4, true);
                tb_teachers.setBm_state(5, false);
                tb_teachers.setBm_state(6, false);
                tb_teachers.setBm_state(7, true);
                tb_teachers.setBm_state(8, true);
                tb_teachers.setBm_state(9, true);
                tb_teachers.setBm_state(10, true);
                tb_teachers.setBm_state(11, true);
                tb_teachers.setBm_state(12, true);
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
    private String utotal;
    private String unow;
    private static final int LABELS_SERIAL = 0;
    private static final int LABELS_USER = 1;
    private static final int LABELS_SPECIAL = 2;
    private static final int LABELS_INTERESTS = 3;
    private static final int LABELS_PHONE = 4;
    private static final int LABELS_LEN = 5;
    private JLabel[] labels;
    private Connection con;
    private Statement stmt, stmt2;
    private ResultSet rs, rs2;
    private String universallast, universalfirst;
    private int desired_id;
    private JComboBox cbuser;
    public JButton[] bm;
    private int global_teacher_pos;
    private int global_total;
    private JPanel p_teachers;
    private CommonToolbar tb_teachers;
    private JLabel numbert;
    private JTextField tfspecial, tfinterests, tfphone;
    private JTextField ttfserial;
    public int action, modify_id;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel slastname;
    private JTextField slast;
}
