package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDTO;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> ordersV4() {
        return orderSimpleQueryRepository.findOrderDTOs();
    }

    /*
        Repository 계층은 순수 Entity를 조회하는데 사용하는 것이 맞음 -> V3까지가 용도에 맞다
        V4는 따로 분리해서 관리하는 게 좋음
     */
    @GetMapping("api/v3/simple-orders")
    public Result<SimpleOrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDTO> findOrders = orders.stream().map(SimpleOrderDTO::new).collect(Collectors.toList());

        return new Result<>(findOrders.size(), findOrders);
    }

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
