package ru.suleyman.bank.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.suleyman.bank.service.ClientService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/welcome")
    public String welcome() {
        log.info("Страница Client");
        return "client";
    }
}
