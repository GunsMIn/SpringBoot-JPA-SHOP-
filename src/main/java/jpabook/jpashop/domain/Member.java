package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name ="member_id")
    private Long id;

    @NotEmpty // 무조건 값이 있어야함
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore // api 전송에서 빠질 수 있게 해준다 (비추천)
    @OneToMany(mappedBy = "member") // 나는 연간관계의 주인이 아니다 -> mappesBy(읽기 전용)
    private List<Order> orders = new ArrayList<>();
}
