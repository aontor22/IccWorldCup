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

public class Players {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public int getMax() {
        int id = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(player_id) from players");
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id + 1;
    }

    public void insert(int id, String pname, String nationality, String role, String bastl, String bostl) {

        String sql = "insert into players values(?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, pname);
            ps.setString(3, nationality);
            ps.setString(4, role);
            ps.setString(5, bastl);
            ps.setString(6, bostl);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showConfirmDialog(null, "Player added successfully");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPlayerValues(JTable table, String searchValues) {
        String sql = "select * from players where concat (player_id, name, nationality, role, batting_style, bowling_style) like ? order by player_id desc";
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
                row[5] = rs.getString(6);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isPIDExist(int id) { // When same id exists
        try {
            ps = con.prepareStatement("select * from players where player_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updatePlayer(int id, String pname, String nationality, String role, String bstl, String bostl) {

        String sql = "update players set name = ?, nationality = ?, role = ?, batting_style = ?, bowling_style = ? where player_id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, pname);
            ps.setString(2, nationality);
            ps.setString(3, role);
            ps.setString(4, bstl);
            ps.setString(5, bostl);
            ps.setInt(6, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Player data updated successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePlayer(int id) {
        int yesNo = JOptionPane.showConfirmDialog(null, "All the information of this Player will also be deleted", "Player delete", JOptionPane.OK_CANCEL_OPTION, 0);
        if (yesNo == JOptionPane.OK_OPTION) {
            try {
                ps = con.prepareStatement("delete from Player where player_id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Player data deleted successfully");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Players.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
