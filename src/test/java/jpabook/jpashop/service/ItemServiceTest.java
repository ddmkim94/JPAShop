package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest // 스프링 부트와 연동해서 테스트 진행
@Transactional // 테스트가 끝나면 데이터 롤백!
class ItemServiceTest {

    // JUnit5에서는 DI를 자동으로 지원하기 때문에 생성자나 Lombok방식으로 DI가 불가능하다고 함....
    // 테스트를 작성할 떄는 @Autowired를 통해서 필드 주입을 받는 방법 밖에 없나봄... 아님 Junit4로 ㄱㄱㄱ
    @Autowired private ItemRepository itemRepository;
    @Autowired private  ItemService itemService;

    @Test
    public void 상품등록() {
        Book book = new Book();
        book.setName("규장각 각신들의 나날");
        book.setAuthor("정은궐");
        book.setPrice(15000);

        itemService.saveItem(book);

        Assertions.assertThat(book).isEqualTo(itemRepository.findOne(book.getId()));
    }
}