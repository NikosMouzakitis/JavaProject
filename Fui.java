package fui;

import ent.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.*;
 
public class Fui extends JFrame {

    public Fui() {

        initComponents();
        doConnect();
    }
    private ImageIcon image;
    JLabel blbl;

    private void initComponents() {
        /* buttons for User's Dialog */

        
        image = new ImageIcon("src/icons/teicrete.jpg");
        blbl = new JLabel();
        blbl.setIcon(image);
        p = new JPanel(new BorderLayout());
        JMenuBar mnbar = new JMenuBar();

        mnu = new JMenu("Persons");
        lessons = new JMenu("Lessons");
        help = new JMenu("Help");
        assign = new JMenu("Assignments");
        print = new JMenu("Print");
        mi_users = new JMenuItem("Users");
        mi_teachers = new JMenuItem("Teachers");
        mi_students = new JMenuItem("Students");
        mi_exit = new JMenuItem("Exit");
        mi_lessons = new JMenuItem("Lessons");
        mi_prerequisites = new JMenuItem("Prerequisites");
        mi_teaching = new JMenuItem("Teaching");
        mi_enrollment = new JMenuItem("Enrollment");

        mi_prerequisites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPrereq();
            }
        });

        mi_enrollment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doEnroll();
            }
        });

        mi_users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doUsers();
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
            }
        });

        mi_teachers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doTeachers();
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
            }
        });

        mi_students.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doStudents();
                } catch (SQLException ex) {
                    String msg = "Error: " + ex.getMessage();
                    System.out.println(msg);
                }
            }
        });

        mi_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });

        mi_lessons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLessons();
            }
        });
        mi_teaching.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTeaching();
            }
        });

        mnu.add(mi_users);
        mnu.add(mi_teachers);
        mnu.add(mi_students);
        mnu.add(mi_exit);
        lessons.add(mi_lessons);
        lessons.add(mi_prerequisites);
        assign.add(mi_teaching);
        assign.add(mi_enrollment);
        h_help = new JMenuItem("Help");
        h_about = new JMenuItem("About");
        help.add(h_help);
        help.add(h_about);

        h_help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doHelp();
            }
        });

        h_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doAbout();
            }
        });

        mnbar.add(mnu);
        mnbar.add(lessons);
        mnbar.add(assign);
        mnbar.add(help);
        setJMenuBar(mnbar);
        p.add(blbl, BorderLayout.CENTER);
        add(p);
        setTitle("School Administration System v1.0");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void doTeaching() {
        new TeachingDialog(this, true, 1);
    }

    private void doEnroll() {
        new EnrollDialog(this, true, 1);
    }

    private void doPrereq() {
        new PrerequisiteDialog(this, true, 1);
    }

    private void doConnect() {
        ResultSet rs = new DBConnection().Query("select * from users");  // select all
        System.out.println("Test queasdasdry complete.");
    }

    private void doLessons() {
        new LessonsDialog(this, true, 1);

    }

    private void doStudents() throws SQLException {
        StudentsDialog s = new StudentsDialog(this, true, 1);
    }

    private void doTeachers() throws SQLException {
        TeachersDialog t = new TeachersDialog(this, true, 1);
    }

    private void doUsers() throws SQLException {
        UsersDialog u = new UsersDialog(this, true, 1);
    }

    private void doExit() {
        System.exit(0);
    }

    private void doHelp() {
        JOptionPane.showMessageDialog(this, "Usage of this program\nSimulating a secretary office\nwhere you can administrate teachers\nstudents and lessons", "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void doAbout() {
        JOptionPane.showMessageDialog(this, "Author: Mouzakitis Nikos\nProject for Java Lab", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel p;
    private JMenu mnu, lessons, assign, print, help;
    private JMenuItem mi_users, mi_teachers, mi_students, mi_exit;
    private JMenuItem mi_lessons, mi_prerequisites, mi_teaching, mi_enrollment;
    private JMenuItem h_help, h_about;

    public static void main(String[] args) {
        Fui a = new Fui();
        a.setResizable(false);
        a.setVisible(true);
        a.setSize(620, 350);

    }
}
