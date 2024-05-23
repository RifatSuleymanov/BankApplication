package ru.suleyman.bank.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suleyman.bank.exception.UserAlreadyExistAuthenticationException;
import ru.suleyman.bank.model.Client;
import ru.suleyman.bank.repository.ClientRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    public void addClient(Client client) {
        clientIncorrect(client);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
    }

    private void clientIncorrect(Client client) {
        List<Client> clients = clientRepository.findAll();
        for (Client cl : clients) {
            if (cl.getLogin().equals(client.getLogin())) {
                throw new UserAlreadyExistAuthenticationException("Клиент с таким логином " + client.getLogin() + " уже существует в базе");

            } else if (cl.getEmail().equals(client.getEmail())) {
                throw new UserAlreadyExistAuthenticationException("Клиент с таким email " + client.getEmail() + "  уже существует в базе");

            } else if (cl.getPhone().equals(client.getPhone())) {
                throw new UserAlreadyExistAuthenticationException("Клиент с таким номером " + client.getPhone() + " уже существует в базе");

            }
        }
    }
}
