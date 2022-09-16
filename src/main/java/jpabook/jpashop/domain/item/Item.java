package jpabook.jpashop.domain.item;


import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속 매핑 전략 첫 단계 부모 클래스에 이것을 해주자(전략을 세우자)
@DiscriminatorColumn(name="dtype") // 부모에는 이것을 자식테이블에는 @DiscriminatoeValue("")이것을 해주자!
@Getter @Setter
public abstract class Item { //추상클래스로 일단 만들겠다 구현체를 갖을거기때문에

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();




     //도메인 주도 설계 엔티티자체에서 해결할 수 있을때는 엔티티안에 비지니스 로직을 만드는게
     //가장 응집도가있다
     //==비지니스 로직==//
     //재고 증가 하는 로직
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }//만약 주문취소를 했다면 주문수량을 +1을 해줘야함

    public void removeStock(int quantity){
        int realStock = this.stockQuantity - quantity;
        //재고를 빼려고하는데 뺀 값이 음수라면 예외를 만들어서 발생시킨다
        if(realStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = realStock;
    }

}
