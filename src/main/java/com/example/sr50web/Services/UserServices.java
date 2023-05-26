package com.example.sr50web.Services;
import com.example.sr50web.Models.*;
import com.example.sr50web.repo.*;
import com.example.sr50web.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepository repo;

    public List<User> listAll(){
        return (List<User>) repo.findAllUsers();
    }


    public User get(Integer id) throws UserNotFoundException {
        User result = repo.findUserById(id);
        return result;
    }

    public User get(String email) throws UserNotFoundException {
        User result = repo.FindUserByEmail(email);
        return result;
    }

    public User get(String email,String password) throws UserNotFoundException {
        User result = repo.findUserWithCred(email,password);
        return result;
    }
    public void save(User user) {
        if(repo.findUserById(user.getId()) != null){
            if(user.getPassword().isEmpty() || user.getPassword() == null ){
                User temp = repo.FindUserByEmail(user.getEmail());
                user.setPassword(temp.getPassword());
            }
            repo.update(user);
        }   else{
            repo.save(user);
        }
    }


    public void delete(Integer id) {
        repo.delete(id);
    }
}
