package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    private Long id; // 나중에 수정까지 보여주려고 id를 넣은 것 같다
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
