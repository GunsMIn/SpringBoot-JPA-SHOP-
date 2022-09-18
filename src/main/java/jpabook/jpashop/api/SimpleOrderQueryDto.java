package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

//@ManyTOone
// 다 : 1 관계만 한 dto !
@Data
public class SimpleOrderQueryDto {
    private Long orderId;
    private String name;//Member 의 name
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address; // Delivery에 있는 address의 값타입

    public SimpleOrderQueryDto(Long orderId,String name,LocalDateTime orderDate,OrderStatus orderStatus,Address address){
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
