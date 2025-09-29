package com.example.first_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first_spring_boot.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}

//essa interface nao precisa ser implementada, pois já é implementada pelo
//JPARepository quando é passado a o tipo da entidade e ID
