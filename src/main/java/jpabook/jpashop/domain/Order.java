package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)//@~~~ToOne모든 연관관계는 지연로딩으로 설정/ 안그러면 쿼리장애 발생
    @JoinColumn(name ="member_id")
    private Member member;

    //cascade의 경우에는 프라이빗 오너의 입장에서 생각하면 쉬워진다.
    //OrderItem / Delivery는 order에서만 쓴다.???
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //원래같은면 persist를 각각해줘야하는데 cascade = CascadeType.ALL를쓰면
    private List<OrderItem> orderItems = new ArrayList<>();   // order만 persist하면 orderItems까지 persist된다!

    @OneToOne(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id") // 일대일 매핑에서의 연간관계 주인으로 지정
    private Delivery delivery;


    private LocalDateTime orderData;

    @Enumerated(EnumType.STRING)
    private  OrderStatus status;//주문 상태 [order,cancel]


    //연관관계메소드
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }



    //==생성메서드(주문)==//
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderData(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /*
    * 주문 취소
    * */
    public void cancel(){
        //만약 상품이 도착했다면 주문취소가 안된다
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);// 주문상태를 취소로 바꿔줌
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // order를 취소하면 orderitem에도 취소해줘야한다
        }
    }

    /*
    전체 주문 가격 조회
    * */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); // 주문가격과 주문수량이있기때문에
        }
        return totalPrice;
    }











}
