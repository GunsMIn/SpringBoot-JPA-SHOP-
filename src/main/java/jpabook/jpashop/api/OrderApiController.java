package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    //의존성 주입
    private final OrderRepository orderRepository;


    @GetMapping("/api/v2/orders")
    public List<OrderDTO> orderV2(){
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOS = orders.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());
        return orderDTOS;

    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3(){
        List<Order> allWithItem = orderRepository.findAllWithItem();
        List<OrderDTO> collect = allWithItem.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());
        return collect;
    }

    //속에 있는것까지 엔티티를 노출하면 안된다.
    @Data
    @AllArgsConstructor
    static class OrderDTO{
        private Long orderId;
        private String name; // member의 name
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address; //delivey의 address
        private List<OrderItemDto> orderItems;


        public OrderDTO(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderData();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems()
                    .stream().map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
            //OrderItem 조차도 DTO로 바꾸어야한다.
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;//상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
}
}
