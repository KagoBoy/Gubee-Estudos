package dao_impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.DepartmentDao;
import db.DbException;
import db.db;
import model.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                "INSERT INTO department "
                + "(Name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                db.closeResultSet(rs);
            } else {
                    throw new DbException("Erro inesperado! Nenhuma linha afetada");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                "UPDATE department "
                + "SET Name = ? WHERE Id = ?"
            );
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "DELETE FROM department WHERE Id = ?"
                );
            st.setInt(1, id);

            int rows = st.executeUpdate();

            if (rows == 0){
                throw new DbException("Não existe Vendedor com esse ID");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally{
            db.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                "SELECT * FROM department WHERE Id = ?"
                );
            st.setInt(1, id);

            rs = st.executeQuery();
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                return dep;
            }
            return null;
            
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally{
            db.closeStatement(st);
            db.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            st = conn.prepareStatement(
                "SELECT * FROM department"
                );

            rs = st.executeQuery();
            while(rs.next()){
                Department dep = instantiateDepartment(rs);
                list.add(dep);
            }
            return list;
            
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally{
            db.closeStatement(st);
            db.closeResultSet(rs);
        }
    }

}
