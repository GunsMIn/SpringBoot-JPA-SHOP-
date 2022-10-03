package jpabook.jpashop.reApi.DTO;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String name; // member
    private Address address; // delivery
    private LocalDateTime orderData;
    private OrderStatus status;
    private List<OrderIemDTO> orderItems;


    public OrderDTO(Order order) {
        this.name = order.getMember().getName();
        this.address = order.getDelivery().getAddress();
        this.orderData = order.getOrderData();
        this.status = order.getStatus();
        this.orderItems = order.getOrderItems().stream()
                .map(oi -> new OrderIemDTO(oi)).collect(Collectors.toList());
    }
}
