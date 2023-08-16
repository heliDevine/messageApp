package project.messageApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private final MyMessageRepository myMessageRepository;

    public MessageController(MyMessageRepository myMessageRepository) {
        this.myMessageRepository = myMessageRepository;
    }


    @GetMapping("/{requestedId}")
    public ResponseEntity<MyMessage> findById(@PathVariable Long requestedId) {

        Optional<MyMessage> myMessageOptional = myMessageRepository.findById(requestedId);

        if(myMessageOptional.isPresent()) {
        return ResponseEntity.ok(myMessageOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody MyMessage newMessageRequest, UriComponentsBuilder ucb) {

        MyMessage savedMessage = myMessageRepository.save(newMessageRequest);

        URI locationOfNewMessages = ucb
                .path("messages/{id}")
                .buildAndExpand(savedMessage.id())
                .toUri();
        return ResponseEntity.created(locationOfNewMessages).build();
    }

    @GetMapping()
    private ResponseEntity<List<MyMessage>> findAll(Pageable pageable) {
        Page<MyMessage> page = myMessageRepository.findAll(
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort().ascending()
                ));

        return ResponseEntity.ok(page.getContent());
    }

}
