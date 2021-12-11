package com.avenueCode.springEssentials.service;

import com.avenueCode.springEssentials.model.User;
import com.avenueCode.springEssentials.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Transactional
@NoArgsConstructor
@Service
public class UserService {

    @Autowired
    UserRepository repository;
    public UserService(UserRepository repo){
        this.repository = repo;
    }

    public List<User> getAllUsers(){
        List<User> result = (List<User>) repository.findAll();

        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public User getUserById(Long id){
        Optional<User> user = repository.findById(id);

        if(user.isPresent()) {
            return user.get();

        } else return null;

    }

    public User createOrUpdateUser(User user){
        if(user.getId()  == null){
            user = repository.save(user);

            return user;
        }
        else {
            Optional<User> userOptional = repository.findById(user.getId());

            if(userOptional.isPresent()){
                User newUser = userOptional.get();
                newUser.setEmail(user.getEmail());
                newUser.setName(user.getName());
                newUser.setSurname(user.getSurname());
                newUser.setBirthday(user.getBirthday());
                if(!user.getPassword().equals("")) newUser.setPassword(user.getPassword());
                newUser = repository.save(newUser);

                return newUser;
            } else {
                user = repository.save(user);

                return user;
            }
        }
    }

    public void deleteUserById(Long id) {

            repository.deleteById(id);
    }
}
