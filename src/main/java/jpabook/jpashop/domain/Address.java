package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable//어딘가에 내장 될 수 있다라는 어노테이션
@Getter  // 값 타입은 값이 변경되는것은 추천하지 않는다!
public class Address {

    private String city;
    private String street;
    private String zipcode;


    protected Address() {; }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
