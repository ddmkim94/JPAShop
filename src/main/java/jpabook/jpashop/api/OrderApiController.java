package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    // 컬렉션 조회 (엔티티 직접 노출)
    @GetMapping("api/v1/orders")
    public List<Order> ordersV1() {

        List<Order> all = orderRepository.findByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("api/v2/orders")
    public List<OrderDTO> ordersV2() {

        List<Order> findOrders = orderRepository.findByString(new OrderSearch());
        return findOrders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    // 해당 필드들의 정보만 가져옴
    // DTO로 감싸서 사용하는 경우 안에 있는 필드도 DTO로 감싸야하는 경우 같이 감싸서 보내라!
    @Data
    private static class OrderDTO {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address; // 값 타입은 그냥 맘대로 사용해도됨 -> 이 새낀 걍 엔티티가 아님 ㅋㅋ
        private List<OrderItemDTO> orderItems;

        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class OrderItemDTO {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @Data
    private static class ItemDTO {

        private Long id;
        private String name;
        private int price;
        private int stockQuantity;

        public ItemDTO(Item item) {
            id = item.getId();
            name = item.getName();
            price = item.getPrice();
            stockQuantity = item.getStockQuantity();
        }
    }
}
