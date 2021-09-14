package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext // EntityManager Injection
    EntityManager em;

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    @Test
    void 상품_주문() throws Exception {
        //given: 테스트에 필요한 데이터 준비
        Member member = createMember();

        Item item = createBook("청춘시대1", 50000, 10);
        int count = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), count);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());
        assertThat(1).isEqualTo(getOrder.getOrderItems().size());
        assertThat(50000 * 2).isEqualTo(getOrder.getTotalPrice());
        assertThat(8).isEqualTo(item.getStockQuantity());
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {
        //given: 테스트에 필요한 데이터 준비
        Member member = createMember();
        Item item = createBook("청춘시대", 50000, 10);
        int count = 11;

        //when, then
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), count), "재고 수량 부족 예외가 발생해야 한다.");
    }
    
    @Test
    void 주문취소() throws Exception {
        //given: 테스트에 필요한 데이터 준비
        Member member = createMember();
        Item item = createBook("청춘시대1", 50000, 10);
        int count = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), count);

        //when
        orderService.cancelOrder(orderId);

        //then
        assertThat(10).isEqualTo(item.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("박은빈");
        member.setAddress(new Address("서울", "망우로 20길 86", "02514"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}