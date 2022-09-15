package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    //다 대 다 경우 조인테이블을 생성해주어야한다.
    @ManyToMany
    @JoinTable(name="category_item",
            joinColumns =@JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    ) // 다:다 관계일때는 조인 테이블이 필요하다
    private List<Item> items = new ArrayList<>();


    //self로 연간관계를 걸었다
    //@XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야 한다
    @ManyToOne(fetch =LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    //연관관계편의메서드
    public void setAddChildCatagory(Category child){
        this.child.add(child);
        child.setParent(this);
    }


}
