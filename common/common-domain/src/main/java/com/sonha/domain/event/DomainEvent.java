package com.sonha.domain.event;

public interface DomainEvent<T> {
    void fire();
}
