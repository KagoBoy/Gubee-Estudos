package dao;

import dao_impl.SellerDaoJDBC;
import db.db;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(db.getConnection());
    }
}
