package fi.valonia.pyorallatoihin.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fi.valonia.pyorallatoihin.data.Season;
import fi.valonia.pyorallatoihin.interfaces.ISystemService;

public class SystemService implements ISystemService {

    @Override
    public List<Season> getSeasons() {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = Database.getConnection();
            String sql = "SELECT * FROM Season";
            Statement seasonStatement = conn.createStatement();
            ResultSet set = seasonStatement.executeQuery(sql);

            List<Season> seasons = new ArrayList<Season>();
            while (set.next()) {
                Season season = new Season();
                season.setId(set.getInt("ID"));
                season.setName(set.getString("NAME"));
                season.setStartDate(set.getDate("STARTDATE"));
                seasons.add(season);
            }
            return seasons;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
        return null;
    }

    @Override
    public Season getCurrentSeason() {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = Database.getConnection();
            String sql = "SELECT * FROM Settings WHERE NAME='season'";
            Statement settingStatement = conn.createStatement();
            ResultSet set = settingStatement.executeQuery(sql);
            set.next();
            int seasonId = set.getInt("VALUE");
            sql = "SELECT * FROM Season WHERE ID=?";
            PreparedStatement seasonStatement = conn.prepareStatement(sql);
            seasonStatement.setInt(1, seasonId);
            set = seasonStatement.executeQuery();
            Season season = new Season();
            set.next();
            season.setId(set.getInt("ID"));
            season.setName(set.getString("NAME"));
            season.setStartDate(set.getDate("STARTDATE"));
            return season;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
        return null;
    }

    @Override
    public void setCurrentSeason(Season season) {
        Connection conn = null;
        try {
            // conn = cp.getConnection();
            conn = Database.getConnection();
            String sql = "UPDATE Settings SET VALUE=? WHERE NAME='season';";
            PreparedStatement settingStatement = conn.prepareStatement(sql);
            settingStatement.setInt(1, season.getId());
            settingStatement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection terminated");
                } catch (Exception e) { /* ignore close errors */
                }
            }
        }
    }

}
