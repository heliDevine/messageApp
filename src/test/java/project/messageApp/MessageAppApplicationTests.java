package project.messageApp;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    @DirtiesContext
    void shouldCreateANewMessage() {
        MyMessage newMessage = new MyMessage(null, "Hello", "a message here");
        ResponseEntity<Void> createResponse =
                restTemplate.postForEntity("/messages", newMessage, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        String messageBody = documentContext.read("$.messagebody");
        assertThat(id).isNotNull();
        assertThat(title).isEqualTo("Hello");
        assertThat(messageBody).isEqualTo("a message here");
    }

    @Test
    void shouldReturnAllMessagesWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int messageCount = documentContext.read("$.length()");
        assertThat(messageCount).isEqualTo(3);


        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray titles = documentContext.read("$..title");
        assertThat(titles).containsExactlyInAnyOrder("Hello", "Hi", "Ciao");
    }

    @Test
    void shouldReturnAPageOfMessages() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages?page=0&size=1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$.[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnASortedPageOfMessages() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages?page=0&size=1&sort=title,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        JSONArray read = documentContext.read("$.[*]");
        assertThat(read.size()).isEqualTo(1);

        String title = documentContext.read("$.[0].title");
        assertThat(title).isEqualTo("Ciao");
    }

    @Test
    void shouldReturnASortedPageOfMessagesWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate.getForEntity("/messages", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        JSONArray titles = documentContext.read("$..title");
        assertThat(titles).containsExactly("Hello", "Hi", "Ciao");
    }

    @Test
    void shouldCreateNewMessage() {
        MyMessage newMyMessage = new MyMessage(null, "Moi", "This is fourth message");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity(("/messages"), newMyMessage, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewMessage = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewMessage, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        assertThat(id).isNotNull();
        assertThat(title).isEqualTo("Moi");
    }
}
