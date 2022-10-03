package jpabook.jpashop.reApi;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.reApi.DTO.OrderDTO;
import jpabook.jpashop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
public class ReOrderAPI {
    private final ReOrderRepository orderRepository;


    @GetMapping("v3/orders")
    public List<OrderDTO> getOrders(@RequestParam(value = "offset",defaultValue = "0") int offset,
                                    @RequestParam(value="limit",defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.getOrders(offset, limit);
        List<OrderDTO> orderDTOList = orders.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());
        return orderDTOList;
    }





}
