package com.sonha.order.service.domain;

import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.entity.Customer;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.entity.Restaurant;
import com.sonha.order.service.domain.event.OrderCreatedEvent;
import com.sonha.order.service.domain.exception.OrderDomainException;
import com.sonha.order.service.domain.mapper.OrderDataMapper;
import com.sonha.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.sonha.order.service.domain.ports.output.repository.CustomerRepository;
import com.sonha.order.service.domain.ports.output.repository.OrderRepository;
import com.sonha.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {

    //@Autowired
    private final OrderDomainService orderDomainService;
    //@Autowired
    private final OrderRepository orderRepository;
    //@Autowired
    private final CustomerRepository customerRepository;
    //@Autowired
    private final RestaurantRepository restaurantRepository;
    //@Autowired
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher;

    public OrderCreateHelper(
            OrderDomainService orderDomainService,
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            RestaurantRepository restaurantRepository,
            OrderDataMapper orderDataMapper,
            OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedEventDomainEventPublisher = orderCreatedEventDomainEventPublisher;
    }

    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        validateRestaurant(restaurant);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        System.out.println("order => " + order);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant, orderCreatedEventDomainEventPublisher);
        System.out.println("orderCreatedEvent => " + orderCreatedEvent);
        saveOrder(order);
        log.info("Order is saved with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.find(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurant(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: " + createOrderCommand.getRestaurantId());
        }
        return optionalRestaurant.get();
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with restaurant id: " + restaurant.getId().getValue() + " is currently not active!");
        }
    }

    private Order saveOrder(Order order) {
        System.out.println("=====================> start");
        Order orderResult = orderRepository.save(order);
        System.out.println("=====================> end");
        if (orderResult == null) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }
}
