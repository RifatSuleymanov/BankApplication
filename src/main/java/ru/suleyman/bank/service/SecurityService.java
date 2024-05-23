package ru.suleyman.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.suleyman.bank.config.security.MyClientDetails;
import ru.suleyman.bank.model.Client;
import ru.suleyman.bank.repository.ClientRepository;

import java.util.Optional;

@Service

public class SecurityService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByLogin(login);
        return client.map(MyClientDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(login + " incorrect"));
    }

}
