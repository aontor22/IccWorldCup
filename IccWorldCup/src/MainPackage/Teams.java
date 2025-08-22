package MainPackage;

import DB.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Teams {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(team_id) from teams");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    public void insert(int tid, String tname, String captain, String coach, String ranking) {

        String sql = "insert into teams values(?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, tid);
            ps.setString(2, tname);
            ps.setString(3, captain);
            ps.setString(4, coach);
            ps.setString(5, ranking);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showConfirmDialog(null, "Team added successfully");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getTeamValues(JTable table, String searchValues) {
        String sql = "select * from teams where concat (team_id, name, captain, coach, ranking) like ? order by team_id desc";
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
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean isTIDExist(int id) { // When same id exists
        try {
            ps = con.prepareStatement("select * from teams where team_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updateTeam(int id, String tname, String captain, String coach, String ranking) {

        String sql = "update teams set name = ?, captain = ?, coach = ?, ranking = ? where team_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, tname);
            ps.setString(2, captain);
            ps.setString(3, coach);
            ps.setString(4, ranking);
            ps.setInt(5, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Team data updated successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTeam(int id) {
        int yesNo = JOptionPane.showConfirmDialog(null, "All the information of this Team will also be deleted", "Player delete", JOptionPane.OK_CANCEL_OPTION, 0);
        if (yesNo == JOptionPane.OK_OPTION) {
            try {
                ps = con.prepareStatement("delete from teams where team_id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Team data deleted successfully");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Teams.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
