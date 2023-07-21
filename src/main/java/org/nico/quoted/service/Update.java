package org.nico.quoted.service;

import org.nico.quoted.domain.User;

@FunctionalInterface
interface Update<T> {
    T update(T t, User user) throws IllegalAccessException;
}
