package project.messageApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MyMessageRepository myMessageRepository;

    public MessageController(MyMessageRepository myMessageRepository) {
        this.myMessageRepository = myMessageRepository;
    }


    @GetMapping("/{requestedId}")
    public ResponseEntity<MyMessage> findById(@PathVariable Long requestedId) {

        Optional<MyMessage> myMessageOptional = myMessageRepository.findById(requestedId);

        if(myMessageOptional.isPresent()) {
//        MyMessage myMessage = new MyMessage(99L, "Hello", "this is the first message");
        return ResponseEntity.ok(myMessageOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
