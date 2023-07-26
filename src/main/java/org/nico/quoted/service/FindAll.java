package org.nico.quoted.service;

import org.nico.quoted.domain.User;

import java.util.List;

public interface FindAll<T> {
    List<T> findAll(User user);
}
