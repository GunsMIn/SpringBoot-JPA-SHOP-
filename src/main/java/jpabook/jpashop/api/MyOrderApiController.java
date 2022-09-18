package jpabook.jpashop.api;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MyOrderApiController {

    private final OrderRepository orderRepository;


    @Data
    @AllArgsConstructor
    static class  OrderDtoMy{
        private Long orderId;
        private String name; // member
        private List<OrderItemsDto> orderItems;
        private Address address; // delivery
        private OrderStatus orderStatus;
        private LocalDateTime orderdate;

        public OrderDtoMy(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderItems = order.getOrderItems().stream()
                    .map(o -> new OrderItemsDto(o))
                    .collect(Collectors.toList());
            this.address = order.getDelivery().getAddress();
            this.orderStatus = order.getStatus();
            this.orderdate = order.getOrderData();
        }
    }

    @Data
    @AllArgsConstructor
    static class OrderItemsDto{
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemsDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }

}
