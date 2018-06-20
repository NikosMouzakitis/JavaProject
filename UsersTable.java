package ent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;

public class UsersTable extends AbstractTableModel {

    String[][] data;

    public UsersTable(int rows, int labels) {
        LEN = rows;
        con = new DBConnection();
        data = new String[LEN][labels];
        rs = con.Query("select * from users;");
        try {
            for (int i = 0; i < LEN; i++) {
                rs.next();
                dummy = rs.getInt(1);
                data[i][0] = Integer.toString(dummy);
                data[i][1] = rs.getString(2);
                data[i][2] = rs.getString(3);
                data[i][3] = rs.getString(4);
                data[i][4] = rs.getString(5);
                data[i][5] = rs.getString(6);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(LEN);
        System.out.println(data[0].length);
    }

    @Override
    public int getRowCount() {
        return LEN;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    private ResultSet rs;
    DBConnection con;
    private int LEN;
    private int dummy;
}
