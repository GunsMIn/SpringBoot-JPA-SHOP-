package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.SimpleOrderQueryDto;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/*
* 이 컨트롤러에서는
* Order
* Order -> Member
* OrDER -> Delivery
* 이렇게 걸리게 할 것 이다.
*
* 결과적으로 xxToONE 관계이다.
* */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderQueryDto> orderListV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderQueryDto> result = orders.stream().map(o -> new SimpleOrderQueryDto(o))
                .collect(toList());
        return result;
    }

    //fetch join 사용한 api 조회 방법
    @GetMapping("/api/v3/simple-order")
    public List<SimpleOrderQueryDto> orderListV3(){
        List<Order> orders = orderRepository.findAllWithDelivery();
        List<SimpleOrderQueryDto> result = orders.stream().map(o -> new SimpleOrderQueryDto(o))
                .collect(toList());
        return result;
    }


    @GetMapping("/api/v4/simple-order")
    public List<SimpleOrderQueryDto> orderListV4(){
        return orderRepository.findOrderDTO();
    }


/*    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

    public SimpleOrderDto(Order order){
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderData();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
    }
    }*/
}
