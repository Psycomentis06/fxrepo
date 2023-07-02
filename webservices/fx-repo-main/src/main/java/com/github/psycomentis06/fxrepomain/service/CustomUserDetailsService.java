package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.User;
import com.github.psycomentis06.fxrepomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var op = userRepository.findByEmail(username);
        User u = op.orElseThrow(() -> new UsernameNotFoundException("No user found for " + username));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        u.getRoles().forEach(
                r -> {
                    authorities.add(
                        new SimpleGrantedAuthority(r.getName())
                );
                    r.getPermissions().forEach(
                            p -> authorities.add(
                                    new SimpleGrantedAuthority(p.getName())
                            )
                    );
                }
        );
        return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(),authorities);
    }
}
