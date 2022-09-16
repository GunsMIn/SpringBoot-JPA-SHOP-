package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다") // validiation 의 일종
    private String name;

    /*@Embedded
    private Address address;*/
    private String city;

    private String street;

    private String zipcode;

}
