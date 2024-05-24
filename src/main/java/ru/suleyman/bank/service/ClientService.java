package ru.suleyman.bank.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suleyman.bank.exception.ClientNotFoundException;
import ru.suleyman.bank.exception.UserAlreadyExistAuthenticationException;
import ru.suleyman.bank.model.Client;
import ru.suleyman.bank.repository.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void addClient(Client client) {
        clientIncorrect(client);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
    }

    public void deleteClientPhone(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Пользователь не найден"));
        if (client.getEmail() == null) {
            throw new IllegalStateException("Невозможно удалить последний контактный данные.");
        }
        client.setPhone(null);
        clientRepository.save(client);
    }

    public void deleteClientEmail(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Пользователь не найден"));
        if (client.getPhone() == null) {
            throw new IllegalStateException("Невозможно удалить последний контактный данные.");
        }
        client.setEmail(null);
        clientRepository.save(client);
    }


    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {

        Client fromAccount = clientRepository.findById(fromUserId)
                .orElseThrow(() -> new ClientNotFoundException("Учетная запись отправителя не найдена"));

        Client toAccount = clientRepository.findById(toUserId)
                .orElseThrow(() -> new ClientNotFoundException("Учетная запись получателя не найдена"));

        Client firstLock = fromUserId < toUserId ? fromAccount : toAccount;
        Client secondLock = fromUserId < toUserId ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Недостаточно средств");
                }
                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                toAccount.setBalance(toAccount.getBalance().add(amount));

                clientRepository.save(fromAccount);
                clientRepository.save(toAccount);
            }
        }
    }

    @Transactional
    public void updateClientContactInfo(Long clientId, String newPhone, String newEmail) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Пользователь не найден"));

        if (newPhone != null && !newPhone.isEmpty()) {
            clientRepository.findByPhone(newPhone)
                    .ifPresent(existingClient -> {
                        if (!existingClient.getId().equals(clientId)) {
                            throw new IllegalStateException("Номер телефона уже занят другим пользователем.");
                        }
                    });
            client.setPhone(newPhone);
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            clientRepository.findByEmail(newEmail)
                    .ifPresent(existingClient -> {
                        if (!existingClient.getId().equals(clientId)) {
                            throw new IllegalStateException("Email уже занят другим пользователем.");
                        }
                    });
            client.setEmail(newEmail);
        }

        clientRepository.save(client);
    }

    public Page<Client> searchClients(LocalDate birthDate, String phone, String name, String email, Pageable pageable) {
        Specification<Client> spec = Specification.where(null);

        if (birthDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("birthDate"), birthDate));
        }

        if (phone != null && !phone.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("phone"), phone));
        }

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), name + "%"));
        }

        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("email"), email));
        }

        return clientRepository.findAll(spec, pageable);
    }

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        List<Client> clients = clientRepository.findAll();

        // Итерация по списку клиентов
        for (Client client : clients) {
            BigDecimal initialBalance = client.getInitialBalance();
            BigDecimal currentBalance = client.getBalance();
            BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.05));


            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
            }

            client.setBalance(newBalance);
            clientRepository.save(client);
        }
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
