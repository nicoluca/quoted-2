package org.nico.quoted.service;

import java.util.UUID;

@FunctionalInterface
interface Delete<T> {
        T delete(Long id, UUID userId) throws IllegalAccessException;
}
