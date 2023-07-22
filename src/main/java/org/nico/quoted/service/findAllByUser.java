package org.nico.quoted.service;

public interface findAllByUser<T> {
    Iterable<T> findAllByUserId(long userId);
}
