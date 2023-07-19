package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@FunctionalInterface
interface Update<T> {
    T update(T t, UUID userId) throws IllegalAccessException;
}
