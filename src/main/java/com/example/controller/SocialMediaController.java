package com.example.controller;
import java.util.Map;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Map<String, Object> response = accountService.register(account);
        AccountService.RegisterStatus status = (AccountService.RegisterStatus) response.get("status");
        switch (status) {
            case SUCCESS:
                return ResponseEntity.ok((Account) response.get("account"));
            case DUPLICATE:
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            case INVALID:
            default:
                return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account login) {
        return accountService.login(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return messageService.createMessage(message)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer id) {
        return ResponseEntity.ok(messageService.getMessageById(id).orElse(null));
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Integer id) {
        return messageService.deleteMessageById(id) ?
                ResponseEntity.ok(1) :
                ResponseEntity.ok().build();
    }
    @PatchMapping("/messages/{id}")
    public ResponseEntity<Object> updateMessage(@PathVariable Integer id, @RequestBody Message message) {
        boolean updated = messageService.updateMessage(id, message.getMessageText());
        return updated ? ResponseEntity.ok(1) : ResponseEntity.badRequest().build();
    }
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.getMessagesByUserId(accountId);
    }
}
