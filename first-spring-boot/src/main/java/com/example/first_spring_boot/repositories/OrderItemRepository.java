package com.example.first_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first_spring_boot.entities.OrderItem;
import com.example.first_spring_boot.entities.pk.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{

}

//essa interface nao precisa ser implementada, pois já é implementada pelo
//JPARepository quando é passado a o tipo da entidade e ID
