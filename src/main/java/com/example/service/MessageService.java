package com.example.service;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;
    public Optional<Message> createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 || message.getPostedBy() == null) {
            return Optional.empty();
        }
        if (accountRepository.findById(message.getPostedBy()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(messageRepository.save(message));
    }
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public Optional<Message> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }
    public boolean deleteMessageById(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public boolean updateMessage(Integer id, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255)
            return false;
        Optional<Message> messageOpt = messageRepository.findById(id);
        if (messageOpt.isEmpty()) return false;
        Message msg = messageOpt.get();
        msg.setMessageText(newText);
        messageRepository.save(msg);
        return true;
    }
    public List<Message> getMessagesByUserId(Integer accountId) {
        if (accountRepository.findById(accountId).isEmpty()) return Collections.emptyList();
        return messageRepository.findByPostedBy(accountId);
    }
}
