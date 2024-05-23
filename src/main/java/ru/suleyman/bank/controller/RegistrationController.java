package ru.suleyman.bank.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suleyman.bank.model.Client;
import ru.suleyman.bank.service.ClientService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final ClientService clientService;

    @PostMapping("")
    public String registration(@RequestBody Client client) {
        log.info("Метод регистрация клиента");
        clientService.addClient(client);
        return "Клиент успешно зарегистрирован!";
    }
}
