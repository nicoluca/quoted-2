package org.nico.quoted.service;

@FunctionalInterface
interface Delete<T> {
        T delete(Long id);
}
