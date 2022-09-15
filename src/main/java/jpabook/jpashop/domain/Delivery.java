package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    //1:1 매칭에서 Order를 보면서 delivery를 본다고 생각한다. 그래서 연관관계의 주인을 Order에 주었다
    @JsonIgnore
    @OneToOne(mappedBy = "delivery",fetch = LAZY) //일대일 매핑 거울
    private Order order;

    @Embedded // 값 타입
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;//READY , COMP(배송중)
}
