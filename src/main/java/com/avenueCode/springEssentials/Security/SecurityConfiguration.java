package com.avenueCode.springEssentials.Security;

import com.avenueCode.springEssentials.controller.UserController;
import com.avenueCode.springEssentials.model.User;
import com.avenueCode.springEssentials.model.UserPrincipal;
import com.avenueCode.springEssentials.repository.UserRepository;
import com.avenueCode.springEssentials.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService service;
    @Autowired
    private UserController controller;
    @Bean
    public static PasswordEncoder passwordEncoder(){return NoOpPasswordEncoder.getInstance();}
    @Bean
    public UserDetailsService userDetailsService(){
        return (UserDetailsService) email -> {
            User user = repository.findUserByEmail(email);
            if(user == null) throw new UsernameNotFoundException("No user found with the email:" + email);
            controller.setId(user.getId());
            return new UserPrincipal(user);
        };
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().authorizeRequests()
                .antMatchers("/","/home","/list-users","/add-edit-user","/createUser","/logOut").permitAll()
                .anyRequest().authenticated().and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/homeLog",true).permitAll()
                .and().logout().invalidateHttpSession(true)
                .clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/home").permitAll();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
}
