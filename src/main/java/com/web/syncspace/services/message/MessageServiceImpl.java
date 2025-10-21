package com.web.syncspace.services.message;

import com.web.syncspace.dto.message.MessageDTO;
import com.web.syncspace.exceptions.others.ResourceNotFoundException;
import com.web.syncspace.models.message.Message;
import com.web.syncspace.repositories.authorities.UsersRepository;
import com.web.syncspace.repositories.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UsersRepository usersRepository;
    private final MessageRepository messageRepository;

    @Override
    public void saveMessage(MessageDTO messageDTO) {
        Message message = Message.builder()
                .sender(usersRepository.findByUsername(messageDTO.getSenderUserName()).orElseThrow(() -> new ResourceNotFoundException("User with username " + messageDTO.getSenderUserName() + " not found")))
                .receiver(usersRepository.findByUsername(messageDTO.getReceiverUserName()).orElseThrow(() -> new ResourceNotFoundException("User with username " + messageDTO.getReceiverUserName() + " not found")))
                .content(messageDTO.getContent())
                .build();

        messageRepository.save(message);
    }


}
