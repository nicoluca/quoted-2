package org.nico.quoted.service;

@FunctionalInterface
interface Update<T> {
    T update(T t);
}
