package project.messageApp;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageAppApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAMessageWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages/99", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");

        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(99);

        String title = documentContext.read("$.title");
        assertThat(title).isEqualTo("Hello");

        String body = documentContext.read("$.messagebody");
        assertThat(body).isEqualTo("this is the first message");
    }

    @Test
    void shouldNotReturnAMessageWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }
}
