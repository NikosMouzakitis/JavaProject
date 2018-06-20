package ent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UsersDialog extends JDialog {

    public UsersDialog(Object o, boolean modal, int gl) {
        global_pos_users = gl;
        initComp(o, modal);
    }

    private void initComp(Object o, boolean modal) {
        setLayout(new BorderLayout());

        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = JButton[].class;
            Method method1 = UsersDialog.class.getMethod("doFunctionCall", parameterTypes);

            tb_users = new CommonToolbar(this, method1, 0);

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        tb_users.setBm_state(0, false);
        tb_users.setBm_state(1, false);
        tb_users.setBm_state(5, false);
        tb_users.setBm_state(6, false);

        p_users = new JPanel(new GridLayout(6, 2));
        unow = "1";
        utotal = "0";
        ResultSet bb = new DBConnection().Query("select count(*) from users");
        try {
            bb.first();
            utotal = Integer.toString(bb.getInt(1));
            
        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        number = new JLabel(unow + " / " + utotal);

        labels = new JLabel[LABELS_LEN];
        labels[LABELS_LAST] = new JLabel("Lastname:", SwingConstants.RIGHT);
        labels[LABELS_FIRST] = new JLabel("Firstname:", SwingConstants.RIGHT);
        labels[LABELS_EMAIL] = new JLabel("Email:", SwingConstants.RIGHT);
        labels[LABELS_PASSWORD] = new JLabel("Password:", SwingConstants.RIGHT);
        labels[LABELS_LOGIN] = new JLabel("Login:", SwingConstants.RIGHT);
        labels[LABELS_SERIAL] = new JLabel("Serial:", SwingConstants.RIGHT);

        tflast = new JTextField();
        tffirst = new JTextField();
        tfpassword = new JPasswordField();
        tfserial = new JTextField();
        tfserial.setEnabled(false);
        tfemail = new JTextField();
        tflogin = new JTextField();

        rs = new DBConnection().Query("SELECT COUNT(*) from users;");
        try {
            rs.next();
            global_total_users = rs.getInt(1);
            rs = new DBConnection().Query("select * from users");

            int tempid;
            String templogin, temppass, tempemail, templast, tempfirst;

            if (rs.next()) {

                tempid = rs.getInt(1);
                templogin = rs.getString(2);
                tempemail = rs.getString(4);
                temppass = rs.getString(3);
                templast = rs.getString(5);
                tempfirst = rs.getString(6);

                Users tmp = new Users(tempid, templogin, temppass, tempemail, templast, tempfirst);

                tfserial.setText(Integer.toString(tmp.getID()));
                tflogin.setText(tmp.getLogin());
                tfpassword.setText(tmp.getPass());
                tfemail.setText(tmp.getEmail());
                tflast.setText(tmp.getLast());
                tffirst.setText(tmp.getFirst());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        JTextField tmpFields[] = {tfserial, tflast, tffirst, tfemail, tflogin, tfpassword,};

        for (int i = 0; i < LABELS_LEN; i++) {
            p_users.add(labels[i]);
            p_users.add(tmpFields[i]);
        }

        add(tb_users, BorderLayout.NORTH);
        add(p_users, BorderLayout.CENTER);
        add(number, BorderLayout.SOUTH);

        setSize(450, 320);
        setModal(modal);
        setTitle("Users");
        setVisible(true);
        System.out.println("10\n");
    }

    private void doSearch() {

        s_search = new JDialog();
        s_search.setLayout(new BorderLayout());
        s_sp = new JPanel(new GridLayout(2, 3));
        s_spp = new JPanel();

        slastname = new JLabel("Lastname", SwingConstants.RIGHT);

        slast = new JTextField(10);
        JTextField sresults = new JTextField(20);

        searchB = new JButton("Search");

        searchB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doUSearch();
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
        rs = new DBConnection().Query("select * from users");  // select all
    }

    private void button_yes_no_setup() {
        for (int i = 0; i < 13; i++) {
            tb_users.setBm_state(i, false);
        }
        tb_users.setBm_state(5, true);
        tb_users.setBm_state(6, true);
    }

    private void button_start_setup() {
        for (int i = 0; i < 13; i++) {
            tb_users.setBm_state(i, true);
        }
        tb_users.setBm_state(0, false);
        tb_users.setBm_state(5, false);
        tb_users.setBm_state(6, false);
    }

    //things to do.
    private void doUSearch() {
        String ql;
        ql = slast.getText();

        System.out.println("I will Search for " + ql);
        rs = new DBConnection().Query("select * from users where  lastname = " + "'" + ql + "'");

        try {
            if (rs.next()) {

                pn_buttoncheck();

                tfserial.setText(Integer.toString(rs.getInt(1)));
                tflogin.setText(rs.getString(2));
                tfpassword.setText(rs.getString(3));
                tfemail.setText(rs.getString(4));
                tflast.setText(rs.getString(5));
                tffirst.setText(rs.getString(6));

            } else {
                JOptionPane.showMessageDialog(this, "Not Found", "Search", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
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

    private void doSearchPrev() {

        try {
            if (rs.previous()) {

                pn_buttoncheck();

                tfserial.setText(Integer.toString(rs.getInt(1)));
                tflogin.setText(rs.getString(2));
                tfpassword.setText(rs.getString(3));
                tfemail.setText(rs.getString(4));
                tflast.setText(rs.getString(5));
                tffirst.setText(rs.getString(6));
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

                tfserial.setText(Integer.toString(rs.getInt(1)));
                tflogin.setText(rs.getString(2));
                tfpassword.setText(rs.getString(3));
                tfemail.setText(rs.getString(4));
                tflast.setText(rs.getString(5));
                tffirst.setText(rs.getString(6));
            }
        } catch (SQLException ex) {
            String msg = "Error: " + ex.getMessage();
            System.out.println(msg);
        }

    }

    private void updateToolbarButtonsState(int iCase) {
        switch (iCase) {
            case 0:
                for (int i = 0; i < 13; i++) {
                    tb_users.setBm_state(i, true);
                }
                tb_users.setBm_state(0, false);
                tb_users.setBm_state(1, false);
                tb_users.setBm_state(5, false);
                tb_users.setBm_state(6, false);
                break;
            case 1:

                if (global_pos_users == 1) {
                    tb_users.setBm_state(0, false);
                    tb_users.setBm_state(1, false);
                } else {
                    tb_users.setBm_state(0, true);
                    tb_users.setBm_state(1, true);
                }
                break;
            case 2:
                tb_users.setBm_state(0, true);
                tb_users.setBm_state(1, true);
                break;
            case 3:
                tb_users.setBm_state(0, true);
                tb_users.setBm_state(1, true);
                tb_users.setBm_state(2, false);
                tb_users.setBm_state(3, false);
                tb_users.setBm_state(4, true);
                tb_users.setBm_state(5, false);
                tb_users.setBm_state(6, false);
                tb_users.setBm_state(7, true);
                tb_users.setBm_state(8, true);
                tb_users.setBm_state(9, true);
                tb_users.setBm_state(10, true);
                tb_users.setBm_state(11, true);
                tb_users.setBm_state(12, true);
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

    /*  ActionListener Function for buttons in the User Dialog  */
    public void doFunctionCall(Integer __farg, JButton[] bm) {
        int farg = __farg;

        System.out.println("farg: " + Integer.toString(farg));

        /*first*/
        switch (farg) {
            case CommonToolbar.FIRST:

                doConnect();

                try {

                    if (rs.next()) {
                        global_pos_users = 1;
                        unow = Integer.toString(global_pos_users);
                        utotal = Integer.toString(global_total_users);
                        number.setText(unow + " / " + utotal);
                        tfserial.setText(Integer.toString(rs.getInt(1)));
                        tflogin.setText(rs.getString(2));
                        tfpassword.setText(rs.getString(3));
                        tfemail.setText(rs.getString(4));
                        tflast.setText(rs.getString(5));
                        tffirst.setText(rs.getString(6));

                    }
                } catch (SQLException ex) {

                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;

            case CommonToolbar.PREVIOUS:
                /*Previous*/
                try {
                    rs = new DBConnection().Query("select * from users");

                    for (int i = 0; i < global_pos_users; i++) {
                        rs.next();
                    }
                    if (rs.previous()) {

                        global_pos_users--;
                        unow = Integer.toString(global_pos_users);
                        utotal = Integer.toString(global_total_users);
                        number.setText(unow + " / " + utotal);

                        if (global_pos_users < global_total_users) {
                            bm[2].setEnabled(true);
                            bm[3].setEnabled(true);
                        }
                        if (global_pos_users == 1) {
                            bm[0].setEnabled(false);
                            bm[1].setEnabled(false);
                        }

                        tfserial.setText(Integer.toString(rs.getInt(1)));
                        tflogin.setText(rs.getString(2));
                        tfpassword.setText(rs.getString(3));
                        tfemail.setText(rs.getString(4));
                        tflast.setText(rs.getString(5));
                        tffirst.setText(rs.getString(6));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case CommonToolbar.NEXT:
                /*NEXT*/
                try {
                    rs = new DBConnection().Query("select * from users");

                    for (int i = 0; i < global_pos_users; i++) {
                        rs.next();
                    }

                    if (rs.next()) {
                        System.out.println("Existing next");
                        global_pos_users++;

                        unow = Integer.toString(global_pos_users);
                        utotal = Integer.toString(global_total_users);
                        number.setText(unow + " / " + utotal);

                        if (global_pos_users == global_total_users) {
                            bm[2].setEnabled(false);
                            bm[3].setEnabled(false);
                        }

                        System.out.println(Integer.toString(rs.getInt(1)) + rs.getString(5) + rs.getString(5));
                        tfserial.setText(Integer.toString(rs.getInt(1)));
                        tflogin.setText(rs.getString(2));
                        tfpassword.setText(rs.getString(3));
                        tfemail.setText(rs.getString(4));
                        tflast.setText(rs.getString(5));
                        tffirst.setText(rs.getString(6));
                    }
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                break;

            case CommonToolbar.LAST:
                // Last record.
                rs = new DBConnection().Query("select * from users");

                try {
                    rs.last();
                } catch (SQLException ex) {
                    Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                global_pos_users = global_total_users;

                unow = Integer.toString(global_pos_users);
                utotal = Integer.toString(global_total_users);
                number.setText(unow + " / " + utotal);

                if (global_pos_users == global_total_users) {
                    bm[2].setEnabled(false);
                    bm[3].setEnabled(false);
                }

                 {
                    try {
                        tfserial.setText(Integer.toString(rs.getInt(1)));
                        tflogin.setText(rs.getString(2));
                        tfpassword.setText(rs.getString(3));
                        tfemail.setText(rs.getString(4));
                        tflast.setText(rs.getString(5));
                        tffirst.setText(rs.getString(6));

                    } catch (SQLException ex) {
                        Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case CommonToolbar.ADD:
                // New //
                doConnect();
                action = 5;
                rs = new DBConnection().Query("select * from users order by idusers desc");
                 {
                    try {
                        rs.first();

                        desired_id = rs.getInt(1) + 1;
                        System.out.println("Desired_id: " + desired_id);

                        rs = new DBConnection().Query("select * from users");

                        rs.afterLast();
                        tfserial.setText("");
                        tflogin.setText("insert value");
                        tfpassword.setText("insert value");
                        tfemail.setText("insert value");
                        tflast.setText("insert value");
                        tffirst.setText("insert value");

                    } catch (SQLException ex) {
                        Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

            case CommonToolbar.CANCEL:
                //cancel
                button_start_setup();
                if (global_pos_users > 1) {
                    bm[0].setEnabled(true);
                }

                doConnect(); // TODO TO REMOVE
                rs = new DBConnection().Query("select * from users");
                System.out.println("my possition is: " + global_pos_users);

                for (int i = 0; i < global_pos_users; i++) {
                    try {
                        rs.next();
                    } catch (SQLException ex) {
                        Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                unow = Integer.toString(global_pos_users);
                utotal = Integer.toString(global_total_users);
                number.setText(unow + " / " + utotal);

                try {
                    tfserial.setText(Integer.toString(rs.getInt(1)));
                } catch (SQLException ex) {
                    Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    tflogin.setText(rs.getString(2));
                    tfpassword.setText(rs.getString(3));
                    tfemail.setText(rs.getString(4));
                    tflast.setText(rs.getString(5));
                    tffirst.setText(rs.getString(6));
                } catch (SQLException ex) {
                    Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (global_pos_users == global_total_users) {
                    bm[2].setEnabled(false);
                    bm[3].setEnabled(false);
                }

                if (global_pos_users > 1) {
                    bm[0].setEnabled(true);
                    bm[1].setEnabled(true);
                } else {
                    bm[0].setEnabled(false);
                    bm[1].setEnabled(false);
                }

                //doFunctionCall(0);
                break;
            case CommonToolbar.OK:
                //Verify - OK

                if (action == 7) {

                    String ta_login = tflogin.getText();
                    String ta_email = tfemail.getText();
                    String ta_last = tflast.getText();
                    String ta_first = tffirst.getText();
                    char[] ta_pass = tfpassword.getPassword();
                    
                    String touse ="";
                    for (int i = 0; i < ta_pass.length; i++)
                        touse +=ta_pass[i];
                    String sql2 = "update users " + "set  login=" + "'" + ta_login + "'" + ", password=" + "'" + touse + "'" + ",email=" + "'" + ta_email
                            + "', lastname=" + "'" + ta_last + "',firstname=" + "'" + ta_first + "'"
                            + "where idusers = " + modify_id;
                    new DBConnection().Update(sql2);

                    if (global_pos_users == global_total_users) {
                        bm[2].setEnabled(false);
                        bm[3].setEnabled(false);
                    }

                    if (global_pos_users > 1) {
                        bm[0].setEnabled(true);
                        bm[1].setEnabled(true);
                    } else {
                        System.out.println("Kleinw ta koumpia");
                        bm[0].setEnabled(false);
                        bm[1].setEnabled(false);
                    }

                    JOptionPane.showMessageDialog(this, "Done", "Update", JOptionPane.INFORMATION_MESSAGE);

                } else if (action == 8) {
                        // coming to delete.

                        
                        
                        String sqltria = "delete from teaches where teacher_idteacher = " + desired_id;
                        String sqltes = "delete from enrollment where student_idstudent = " + desired_id;
                        String sqlena = "delete from student where idstudent = " + desired_id;
                        String sqlduo = "delete from teacher where idteacher = " + desired_id;
                        
                      
                        new DBConnection().Update(sqltria);
                        new DBConnection().Update(sqltes);
                        new DBConnection().Update(sqlena);
                        new DBConnection().Update(sqlduo);
                        
                        
                        String sql2 = "DELETE FROM users WHERE idusers= " + desired_id + "";

                        new DBConnection().Update(sql2);
                        JOptionPane.showMessageDialog(this, "Done", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                        global_total_users--;

                        unow = Integer.toString(global_pos_users);
                        utotal = Integer.toString(global_total_users);
                        number.setText(unow + " / " + utotal);

                        doFunctionCall(0, bm);
                     
                    

                } else if ((action == 5) || (action == 11)) {
                    String ta_login = tflogin.getText();
                    String ta_email = tfemail.getText();
                    String ta_last = tflast.getText();
                    String ta_first = tffirst.getText();
                    char[] ta_pass = tfpassword.getPassword();
                    String touse ="";
                    for (int i = 0; i < ta_pass.length; i++)
                        touse +=ta_pass[i];
                           
                    
                    
                    String sql2 = "INSERT INTO users VALUES (" + desired_id
                            + "," + "'" + ta_login + "'"
                            + "," + "'" + touse + "'"
                            + "," + "'" + ta_email + "'"
                            + "," + "'" + ta_last + "'"
                            + "," + "'" + ta_first + "'" + ")";
                    System.out.println(sql2);
                    new DBConnection().Update(sql2);
                    JOptionPane.showMessageDialog(this, "Done", "Insertion", JOptionPane.INFORMATION_MESSAGE);
                    global_total_users++;

                    unow = Integer.toString(global_pos_users);
                    utotal = Integer.toString(global_total_users);
                    number.setText(unow + " / " + utotal);

                    doConnect();

                    rs = new DBConnection().Query("select * from users");
                    System.out.println("my possition is: " + global_pos_users);

                    global_pos_users = 1;
                    for (int i = 0; i < global_total_users - 1; i++) {
                        try {
                            if (rs.next()) {
                                global_pos_users++;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("Eftasa stin : " + global_pos_users);

                    unow = Integer.toString(global_pos_users);
                    utotal = Integer.toString(global_total_users);
                    number.setText(unow + " / " + utotal);

                    try {
                        tfserial.setText(Integer.toString(rs.getInt(1)));
                        tflogin.setText(rs.getString(2));
                        tfpassword.setText(rs.getString(3));
                        tfemail.setText(rs.getString(4));
                        tflast.setText(rs.getString(5));
                        tffirst.setText(rs.getString(6));

                    } catch (SQLException ex) {
                        Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (global_pos_users == global_total_users) {
                        bm[0].setEnabled(true);
                        bm[1].setEnabled(true);
                        bm[2].setEnabled(false);
                        bm[3].setEnabled(false);
                    }
                    if (global_pos_users > 1) {
                        bm[0].setEnabled(true);
                        bm[1].setEnabled(true);
                    } else {
                        System.out.println("Kleinw ta koumpia");
                        bm[0].setEnabled(false);
                        bm[1].setEnabled(false);
                    }

                    for (int i = 0; i < 13; i++) {
                        bm[0].setEnabled(true);
                    }

                    bm[2].setEnabled(false);
                    bm[3].setEnabled(false);
                    bm[5].setEnabled(false);
                    bm[6].setEnabled(false);
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
                int tempid;
                String templogin,
                 tempemail,
                 templast,
                 tempfirst,
                 temppass;

                try {
                    tempid = rs.getInt(1);
                    templogin = rs.getString(2);
                    tempemail = rs.getString(4);
                    temppass = rs.getString(3);
                    templast = rs.getString(5);
                    tempfirst = rs.getString(6);

                    Users tmp = new Users(tempid, templogin, temppass, tempemail, templast, tempfirst);

                    tfserial.setText(Integer.toString(tmp.getID()));
                    tflogin.setText(tmp.getLogin());
                    tfpassword.setText(tmp.getPass());
                    tfemail.setText(tmp.getEmail());
                    tflast.setText(tmp.getLast());
                    tffirst.setText(tmp.getFirst());

                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
                System.out.println("Refresh completed");

                break;
            case CommonToolbar.SEARCH: // search for lastnames.

                doSearch();

                break;
            case CommonToolbar.REPLICATE:
                doConnect();

                String login_temp,
                 email_temp,
                 first_temp,
                 last_temp;
                char[] pass_temp;
                login_temp = tflogin.getText();
                email_temp = tfemail.getText();
                first_temp = tffirst.getText();
                last_temp = tflast.getText();
                pass_temp = tfpassword.getPassword();

                action = 11;
                rs = new DBConnection().Query("select * from users order by idusers desc");
                 {
                    try {
                        rs.first();

                        desired_id = rs.getInt(1) + 1;
                        System.out.println("Desired_id for replicate: " + desired_id);

                        rs = new DBConnection().Query("select * from users");

                        rs.afterLast();
                        tfserial.setText((Integer.toString(desired_id)));
                        tflogin.setText(login_temp);
                        tfpassword.setText(Arrays.toString(pass_temp));
                        tfemail.setText(email_temp);
                        tflast.setText(last_temp);
                        tffirst.setText(first_temp);

                    } catch (SQLException ex) {
                        Logger.getLogger(UsersDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case CommonToolbar.TABLE:
                doUsersTable();
                break;
            default:
                break;
        }

        updateToolbarButtonsState(farg);
    }

    private void doUsersTable() {
        //Not working need to be done.!
        utdialog = new JDialog();
        utp = new JPanel(new BorderLayout());
        utt = new JTable(new UsersTable(global_total_users, LABELS_LEN));
        utsc = new JScrollPane(utt);

        utdialog.setSize(450, 300);
        utdialog.add(utsc);
        utdialog.setModal(true);
        utdialog.setTitle("Table View");
        utdialog.setVisible(true);

    }
    private JPanel utp, utpk;
    private JTable utt;
    private JScrollPane utsc;
    private JDialog utdialog;
    private int desired_id;

    private JPanel p_users;
    private ResultSet rs;

    private CommonToolbar tb_users;
    private JLabel[] labels;
    private JTextField tfserial, tflast, tffirst, tflogin, tfemail;
    private JPasswordField tfpassword;
    private JLabel number;
    private int global_pos_users;
    private int action, modify_id;
    private String unow, utotal;
    private int global_total_users;
    private JDialog s_search;
    private JPanel s_sp, s_spp;
    private JButton searchB, prevS, nextS;
    private JLabel slastname;
    private JTextField slast;

    private static final int LABELS_SERIAL = 0;
    private static final int LABELS_LAST = 1;
    private static final int LABELS_FIRST = 2;
    private static final int LABELS_EMAIL = 3;
    private static final int LABELS_LOGIN = 4;
    private static final int LABELS_PASSWORD = 5;
    private static final int LABELS_LEN = 6;

}
