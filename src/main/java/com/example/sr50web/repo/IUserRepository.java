package com.example.sr50web.repo;

import com.example.sr50web.Models.*;

import java.util.List;


public interface IUserRepository {
    public List<User> findAllUsers();
    public User findUserById(Integer id);

    public User FindUserByEmail(String email);

    public User findUserWithCred(String email, String sifra);

    public int save(User user);

    public int update(User user);

    public int delete(Integer id);

}
