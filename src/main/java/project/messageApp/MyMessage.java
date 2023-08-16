package project.messageApp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "MYMESSAGE")
public record MyMessage (
        @Id Long id,
        String title,
        String messagebody,
        String author) {
}
