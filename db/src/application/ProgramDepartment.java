package application;

import java.util.List;
import java.util.Scanner;

import dao.DaoFactory;
import dao.DepartmentDao;
import model.Department;

public class ProgramDepartment {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Teste 1: department findById ===");
        Department department = departmentDao.findById(3);
        System.out.println(department);
        //Fashion

        System.out.println("\n=== Teste 2: department findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department obj : list){
            System.out.println(obj);
        }

        System.out.println("\n=== Teste 3: department Insert ===");
        Department newDepartment = new Department(null, "Yan");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted! New id = " + newDepartment.getId());
        //Insere Yan com Id=5

        System.out.println("\n=== Teste 4: department Update ===");
        department = departmentDao.findById(1);
        department.setName("Endy");
        departmentDao.update(department);
        System.out.println("Update completed");
        //Atualiza o nome do ID=1 para Endy

        System.out.println("\n=== Teste 5: department Delete ===");
        System.out.println("Digite um id para o teste do método Delete");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Deleted completed");
        sc.close();
        //Deleta o departamento de ID de escolha do usuário
    }
}
