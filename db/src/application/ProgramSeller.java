package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dao.DaoFactory;
import dao.SellerDao;
import model.Department;
import model.Seller;

public class ProgramSeller {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Teste 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n=== Teste 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n=== Teste 3: seller findAll ===");
        list = sellerDao.findAll();
        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n=== Teste 4: seller Insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("\n=== Teste 5: seller Update ===");
        seller = sellerDao.findById(1);
        seller.setName("Martha Waine");
        sellerDao.update(seller);
        System.out.println("Update completed");

        System.out.println("\n=== Teste 7: seller Delete ===");
        System.out.println("Digite um id para o teste do método Delete");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Deleted completed");
        sc.close();
    }
}
