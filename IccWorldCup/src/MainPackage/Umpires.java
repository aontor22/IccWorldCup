package MainPackage;

import DB.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Umpires {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(umpire_id) from umpires");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Umpires.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }
    
        public void getUmpValues(JTable table, String searchValues) {
        String sql = "select * from umpires where concat (umpire_id, name, nationality) like ? order by umpire_id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValues + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Umpires.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
