package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른 곳에서 생성자로 값을 못 넣게 하려고
public class Order { // 연관관계의 주인 : foregin Key를 담당(insert,update)할 수 있는 테이블,엔티티


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    //모든 로딩은 지연로딩이 성능 상 좋다\

    // Member클래스를 보면 @JSONignore를 해주었다
    //한쪽만 해주어도 된다.
    @ManyToOne(fetch = LAZY)//@~~~ToOne모든 연관관계는 지연로딩으로 설정/ 안그러면 쿼리장애 발생
    @JoinColumn(name ="member_id") // 외래키가 있는 곳이 주인
    private Member member;

    //cascade의 경우에는 프라이빗오너의 입장에서 생각하면 쉬워진다.
    //OrderItem / Delivery는 order에서만 쓴다.???

    //api 페이징 처리시 컬렉션에서 페이징처리 안되는 문제 해결
    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //원래같은면 persist를 각각해줘야하는데 cascade = CascadeType.ALL를쓰면
    private List<OrderItem> orderItems = new ArrayList<>();   // order만 persist하면 orderItems까지 persist된다!


    @OneToOne(fetch = LAZY,cascade = CascadeType.ALL) //delivery에 값만 넣어주고 order를 persist를 해주면 같이 persist된다
    @JoinColumn(name="delivery_id") // 일대일 매핑에서의 연간관계 주인으로 지정
    private Delivery delivery;
    //Order를 보면서 delivery를 본다고 생각한다. 그래서 연관관계의 주인을 Order에 주었다

    private LocalDateTime orderData;//주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문 상태 [order,cancel]


    //연관관계편의메소드를 가지고 있어야 할 곳은 핵심적으로 컨트롤 할 수 있는 곳이 좋다
    //연관관계 편의 메소드 --> 연관관계를 맺은 엔티티는 2개다 값을 넣어줘야 null포인터exception이 안 생긴다.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this); //member테이블에도 oreder를 넣어줘야하니까
    }
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); // oredritem 테이블에도 oreder를 넣워줘얗 하니까
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
    * 상품(item)의 stockQuantity를 다시 +1 늘려줘야한다
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
