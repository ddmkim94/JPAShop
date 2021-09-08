package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // 다른 객체의 내장 객체가 될 수 있다!!
@Getter
@NoArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 구현 라이브러리가 객체 생성시 리플렉션 같은 기술을 사용하려면 기본 생성자가 필요하기 때문에 기본생성자는 반드시 있어야함
    // @NoArgsConstructor, 기본 생성자를 수동으로 만들어주면 된다~

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}