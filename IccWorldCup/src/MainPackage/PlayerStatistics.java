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

public class PlayerStatistics {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(player_stat_id) from playerstatistics");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }
    
    public void getPSValues(JTable table, String searchValues) {
        String sql = "select * from playerstatistics where concat (player_stat_id, player_id, match_id, runs_scored, wickets_taken, catches, stumpings) like ? order by player_stat_id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValues + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getInt(4);
                row[4] = rs.getInt(5);
                row[5] = rs.getInt(6);
                row[6] = rs.getInt(7);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerStatistics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
