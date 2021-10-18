package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // V1 Upgrade!
    @GetMapping("api/v1/simple-orders-upgrade")
    public Result<List<OrderDTO>> ordersV1Upgrade() {
        List<Order> findOrders = orderRepository.findByString(new OrderSearch());
        List<OrderDTO> orders = new ArrayList<>();

        for (Order findOrder : findOrders) {
            orders.add(new OrderDTO(findOrder.getMember()));
            findOrder.getMember().getName();
        }

        return new Result<>(orders.size(),orders);
    }

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findByString(new OrderSearch());
        return all;
    }

    @Data
    @AllArgsConstructor
    private static class OrderDTO {

        private Member member;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {

        private int count;
        private T data;
    }
}
