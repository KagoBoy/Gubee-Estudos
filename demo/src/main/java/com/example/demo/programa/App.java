package com.example.demo.programa;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.example.demo.dominio.Pessoa;

public class App {
    public static void main(String[] args) throws Exception {
        // Pessoa p1 = new Pessoa(null, "Teste123", "teste123@gmail.com");
        // Pessoa p2 = new Pessoa(null, "Pessoa 2", "pessoa2@gmail.com");
        // Pessoa p3 = new Pessoa(null, "Pessoa 3", "pessoa3@gmail.com");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
        EntityManager em = emf.createEntityManager();

        //Para excluir, o objeto precisa estar monitorado
        //Ou seja, acabou de ser instanciado ou acabou de ser buscado com o .find()
        Pessoa p = em.find(Pessoa.class, 2);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();

        // Pessoa p = em.find(Pessoa.class, 2); //busca a pessoa na tabela pelo id
        // System.out.println(p);

        //Cria uma tabela pessoa e insere os dados no banco
        // em.getTransaction().begin();
        // em.persist(p1);
        // em.persist(p2);
        // em.persist(p3);
        // em.getTransaction().commit();
        // System.out.println("Pronto!");

        // System.out.println(p1);
        // System.out.println(p2);
        // System.out.println(p3);
        em.close();
        emf.close();
    }
}
