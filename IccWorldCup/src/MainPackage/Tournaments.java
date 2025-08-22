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

public class Tournaments {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(tournament_id) from tournaments");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tournaments.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }
    
    public void getTourValues(JTable table, String searchValues) {
        String sql = "select * from tournaments where concat (tournament_id, name, year, host_country, winner_id, runner_up_id) like ? order by tournament_id desc";
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
                row[3] = rs.getInt(4);
                row[4] = rs.getInt(5);
                row[5] = rs.getString(6);
                row[6] = rs.getInt(7);
                row[7] = rs.getInt(8);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Tournaments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
