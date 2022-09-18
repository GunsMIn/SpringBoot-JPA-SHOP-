package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;



@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    /* @ManyToOne , @OneToOne
     * 이 컨트롤러에서는
     * Order
     * Order -> Member
     * OrDER -> Delivery
     * 이렇게 걸리게 할 것 이다.
     *
     * 결과적으로 xxToONE 관계이다. (컬렉션이 아닌것)
     * */

    private final OrderRepository orderRepository;

    //이렇게 하면 무한루프
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderDto> orderListV2(){
        List<Order> orders = orderRepository.findAll();
        List<SimpleOrderDto> orderDtoList = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return orderDtoList;
    }

    //fetch join 사용한 api 조회 방법
    @GetMapping("/api/v3/simple-order")
    public List<SimpleOrderDto> orderListV3(){
        List<Order> orders = orderRepository.findAllWithDelivery(); //fetch join사용
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }


    @GetMapping("/api/v4/simple-order")
    public List<SimpleOrderQueryDto> orderListV4(){
        return orderRepository.findOrderDTO();//
        //
    }


    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

    public SimpleOrderDto(Order order){
        this.orderId = order.getId();
        this.name = order.getMember().getName();
        this.orderDate = order.getOrderData();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress();
    }
    }
}
