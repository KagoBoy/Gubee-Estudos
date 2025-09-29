package com.example.first_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first_spring_boot.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

//essa interface nao precisa ser implementada, pois já é implementada pelo
//JPARepository quando é passado a o tipo da entidade e ID
