package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDTO {
     private Long orderId;
     private String name;
     private LocalDateTime orderDate;
     private OrderStatus orderStatus;
     private Address address;

     // DTO 안에서 엔티티를 파라미터로 받는 건 크게 문제되지 않음
     public OrderSimpleQueryDTO(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
         this.orderId = orderId;
         this.name = name;
         this.orderDate = orderDate;
         this.orderStatus = orderStatus;
         this.address = address;
     }
}