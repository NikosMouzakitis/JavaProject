package ent;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JToolBar;

public class CommonToolbar extends JToolBar implements ActionListener {

    private final Object buttonObject;
    private final Method buttonMethod;
    private JDialog utdialog;
    private JToolBar tb_users;
    private JButton tb_first, tb_previous, tb_next, tb_last, tb_add, tb_cancel, tb_ok, tb_modify, tb_delete, tb_refresh, tb_search, tb_replicate, tb_table;
    private JButton[] bm = {tb_first, tb_previous, tb_next, tb_last, tb_add, tb_cancel, tb_ok, tb_modify, tb_delete, tb_refresh, tb_search, tb_replicate, tb_table};

    public static final int FIRST = 0;
    public static final int PREVIOUS = 1;
    public static final int NEXT = 2;
    public static final int LAST = 3;
    public static final int ADD = 4;
    public static final int CANCEL = 5;
    public static final int OK = 6;
    public static final int MODIFY = 7;
    public static final int DELETE = 8;
    public static final int REFRESH = 9;
    public static final int SEARCH = 10;
    public static final int REPLICATE = 11;
    public static final int TABLE = 12;

    public void setBm_state(int farg, boolean state) {
        this.bm[farg].setEnabled(state);
    }

    public CommonToolbar(Object obj, Method meth, int dummy) {
        buttonObject = obj;
        buttonMethod = meth;
        initComp();
    }

    public void __doFunctionCall(int farg, JButton[] tm) {
        try {
            buttonMethod.invoke(buttonObject, farg, tm);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CommonToolbar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComp() {
        ImageIcon[] names = {
            new ImageIcon("src/icons/first16.png"),
            new ImageIcon("src/icons/previous16.png"),
            new ImageIcon("src/icons/next16.png"),
            new ImageIcon("src/icons/last16.png"),
            new ImageIcon("src/icons/add16.png"),
            new ImageIcon("src/icons/cancel16.png"),
            new ImageIcon("src/icons/ok16.png"),
            new ImageIcon("src/icons/modify16.png"),
            new ImageIcon("src/icons/delete16.png"),
            new ImageIcon("src/icons/refresh16.png"),
            new ImageIcon("src/icons/search16.png"),
            new ImageIcon("src/icons/replicate16.png"),
            new ImageIcon("src/icons/table16.png"),};

        for (int i = 0; i < bm.length; i++) {
            bm[i] = new JButton(names[i]);
            this.add(bm[i]);
            bm[i].addActionListener(this);
        }
        setLayout(new FlowLayout());
    }

    public void hideButton(int i) {
        bm[i].setVisible(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < bm.length; i++) {
            if (e.getSource() == bm[i]) {
                __doFunctionCall(i, bm);
            }
        }
    }
}
