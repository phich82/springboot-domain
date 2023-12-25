package com.sonha.order.service.domain;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.domain.valueobject.OrderStatus;
import com.sonha.domain.valueobject.ProductId;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.create.OrderAddressDto;
import com.sonha.order.service.domain.dto.create.OrderItemDto;
import com.sonha.order.service.domain.entity.Customer;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.entity.Product;
import com.sonha.order.service.domain.entity.Restaurant;
import com.sonha.order.service.domain.exception.OrderDomainException;
import com.sonha.order.service.domain.mapper.OrderDataMapper;
import com.sonha.order.service.domain.ports.input.service.OrderApplicationService;
import com.sonha.order.service.domain.ports.output.repository.CustomerRepository;
import com.sonha.order.service.domain.ports.output.repository.OrderRepository;
import com.sonha.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("6f696143-3e87-4aed-aa0e-935b5abd5813");
    private final UUID RESTAURANT_ID = UUID.fromString("04fc89e6-73d3-4a5a-b5a7-72668671e4af");
    private final UUID PRODUCT_ID = UUID.fromString("38423b4f-6f3a-4e6f-9a77-c14ae4b84a2a");
    private final UUID ORDER_ID = UUID.fromString("f2a72977-17e6-4215-8be7-4af83a35b29f");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDto.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDto.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddressDto.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItemDto.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.find(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findByRestaurant(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurantResponse));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponseDto createOrderResponseDto = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, createOrderResponseDto.getOrderStatus());
        assertEquals("Order created successfully", createOrderResponseDto.getMessage());
        assertNotNull(createOrderResponseDto.getOrderTrackingId());
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals(
                "Total price: 250.00 is not equals to order items total: 200.00!",
                orderDomainException.getMessage()
        );
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals(
                "Order item price: 60.00 is not valid for product: " + PRODUCT_ID,
                orderDomainException.getMessage()
        );
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurantResponse = Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(false)
                .build();
        when(restaurantRepository.findByRestaurant(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals(
                "Restaurant with restaurant id: " + RESTAURANT_ID + " is currently not active!",
                orderDomainException.getMessage()
        );
    }

}
