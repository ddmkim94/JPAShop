package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // V1 Upgrade!
    @GetMapping("api/v2/simple-orders")
    public Result<SimpleOrderDTO> ordersV2() {
        List<Order> orders = orderRepository.findByString(new OrderSearch());

        // Stream, Lambda 정리
        // Order 2개
        // N + 1 -> 1(최초 Order SQL) + 회원 N(2) + 배송 N(2)
        List<SimpleOrderDTO> findOrder = orders.stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());

        return new Result<>(findOrder.size(), findOrder);
    }

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findByString(new OrderSearch());
        return all;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {

        private int count;
        private List<T> data;
    }

    @Data
    private static class SimpleOrderDTO {

        private Long OrderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // DTO 안에서 엔티티를 파라미터로 받는 건 크게 문제되지 않음
        public SimpleOrderDTO(Order order) {
            OrderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
