package com.patskevich.gpproject.service;

import com.patskevich.gpproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getUser(login);
        System.out.println("----------АВТОРИЗАЦИЯ-----------:"+user);
        if (user == null) {
            throw new UsernameNotFoundException(login);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), encoder().encode(user.getPassword()), true, true, true,
                true, getAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            String role) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> authorities = Arrays.asList(simpleGrantedAuthority);
        return authorities;
    }

    @Autowired
    private PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
