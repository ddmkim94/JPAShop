package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;

    private String name;
    private int price; // 가격
    private int stockQuantity; // 재고

    private String author;
    private String isbn;
}
