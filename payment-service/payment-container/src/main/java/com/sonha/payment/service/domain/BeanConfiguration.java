package com.sonha.payment.service.domain;

import com.sonha.payment.service.domain.service.PaymentDomainService;
import com.sonha.payment.service.domain.service.impl.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
