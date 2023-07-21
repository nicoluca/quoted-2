package org.nico.quoted.service;

import org.nico.quoted.domain.User;

@FunctionalInterface
interface Delete<T> {
        T delete(Long id, User user) throws IllegalAccessException;
}
