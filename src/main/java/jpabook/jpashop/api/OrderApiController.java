package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    //페이징 처리를 하기윈한 방법
    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3(){
        List<Order> orders = orderRepository.findAllWithDelivery();
        //xxxToOne관계는 우선 fetchjoin으로 가져온다

        List<OrderDTO> orderDTOS = orders.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());
        return orderDTOS;
    }
    //페이징 처리를 위한 api
    //먼저 ToOne(OneToOne, ManyToOne) 관계를 모두 페치조인 한다. ToOne 관계는 row수를
    //증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
    //컬렉션은 지연 로딩으로 조회한다.
    //지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용한다.
    //hibernate.default_batch_fetch_size: 글로벌 설정
    //@BatchSize: 개별 최적화
    //이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회한다
    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> orderv3_page(
            @RequestParam(value = "offset",defaultValue = "0") int offset,
            @RequestParam(value ="limit",defaultValue = "100") int limit
    ) {
        //default_batch_fetch_size: 100 orders와 관련된 컬렉션을 미리 인쿼리를 100개 땡겨온다.
        // 1:n:m 관계를 1:1:1관계로 가져올 수 있다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDTO> collect = orders.stream().map(o -> new OrderDTO(o))
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
