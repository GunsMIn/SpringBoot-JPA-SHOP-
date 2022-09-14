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
    private Address address;

    public SimpleOrderQueryDto(Order order){
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderData();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
    }

}
