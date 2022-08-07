package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    //주문은 많은 레퍼지토리가 필요하다. 주문 / 회원 / 상품
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    //주문
    @Transactional // 읽기 전용이 아니기때문에에
   public Long order(Long memberId,Long itemId,int count){ // 주문하려면 회원과 아이템이 필요하다
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 배송정보는 간단하게 member의 배송정보로 설정해주겠다.
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //주문생성
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order); // 이것만해주어도 order의 cascade때문에 orderItem과 Delivery는 자동으로 persist가된다
        return order.getId();
    }

    //주문취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }


    //검색
    /*public List<Order>*/
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

    //fetch 조인인

}