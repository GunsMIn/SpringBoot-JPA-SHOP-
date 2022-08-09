package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 생성자 못만들게 해주는 것
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int orderPrice; //주문 가격

    private int count; // 주문 수량

    //이 코드는 다른 곳에서 orderItem을 생성하는것을 막아준다. 만약 이런 코드를 본다면 아 다른곳에서 생성자로 생성하지 말아야하는구나라고 생각해야한다
   /* protected OrderItem(){ // 13번째 줄의 어노테이션으로 대체할 수 있다.
        ;
    }*/


    //==생성메소드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        //orderItem이 생성이 되면 Item재고 수량은 줄어야 하니까
        item.removeStock(count);
        return orderItem;
    }

    /*비지니스로직 (oreder취소시 orderitem도 취소)*/
    public void cancel(){
        getItem().addStock(count); //재고 수량을 원복해준다.
    }

    //조회로직
    //주문상품가격조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }



}
