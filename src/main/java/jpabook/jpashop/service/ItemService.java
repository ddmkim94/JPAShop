package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기 전용!
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // saveItem 변경
    @Transactional(readOnly = false)
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // findItems 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // findOne 조회
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
