package application;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.util.Arrays;
// import java.util.List;

import db.db;

public class App {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = db.getConnection();
            st = conn.createStatement();

            rs = st.executeQuery("select * from department");
            
            while(rs.next()){
                System.out.println(rs.getInt("Id") + ", " + rs.getString("name"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally{
            rs.close();
            st.close();
            db.closeConnection();
        }
    }
}
