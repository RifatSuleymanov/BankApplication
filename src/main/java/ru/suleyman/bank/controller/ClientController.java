package ru.suleyman.bank.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.suleyman.bank.exception.ClientNotFoundException;
import ru.suleyman.bank.model.Client;
import ru.suleyman.bank.service.ClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("")
    public List<Client> findAll() {
        log.info("Метод findAll");
        return clientService.findAll();
    }

    @DeleteMapping("/{id}/phone")
    public String deletePhone(@PathVariable Long id) {
        log.info("Метод deletePhone");
        clientService.deleteClientPhone(id);
        return "Телефон клиента успешно удален";
    }

    @DeleteMapping("/{id}/email")
    public String deleteEmail(@PathVariable Long id) {
        log.info("Метод deleteEmail");
        clientService.deleteClientEmail(id);
        return "Email клиента успешно удален";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromUserId,
                           @RequestParam Long toUserId,
                           @RequestParam BigDecimal amount) {
        log.info("Метод transfer");
        clientService.transfer(fromUserId, toUserId, amount);
        return "Перевод прошло успешно";
    }

    @PutMapping("/{id}/contact")
    public String updateContactInfo(@PathVariable Long id, @RequestParam(required = false) String phone, @RequestParam(required = false) String email) {
        log.info("Метод обновление {id}/contact");
        try {
            clientService.updateClientContactInfo(id, phone, email);
            return "Контактная информация успешно обновлена";
        } catch (ClientNotFoundException e) {
            return "Пользователь не найден: " + e.getMessage();
        } catch (IllegalStateException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    @GetMapping("/search")
    public Page<Client> searchClients(
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        log.info("Метод поиска /search");

        LocalDate birthDateParsed = null;
        if (birthDate != null) {
            birthDateParsed = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE);
        }

        return clientService.searchClients(birthDateParsed, phone, name, email, pageable);
    }
}