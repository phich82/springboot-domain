package com.sonha.restaurant.service.domain;

import com.sonha.restaurant.service.domain.service.RestaurantDomainService;
import com.sonha.restaurant.service.domain.service.impl.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
