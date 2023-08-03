package project.messageApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class MessageJsonTest {

    @Autowired
    private JacksonTester<MyMessage> json;

    @Test
    public void myMessageSerializationTest() throws IOException {
        MyMessage myMessage = new MyMessage(99L, "Message Title", "message text body");

        assertThat(json.write(myMessage)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(myMessage)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(myMessage)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);

        assertThat(json.write(myMessage)).hasJsonPathStringValue("@.title");
        assertThat(json.write(myMessage)).extractingJsonPathStringValue("@.title")
                .isEqualTo("Message Title");

        assertThat(json.write(myMessage)).hasJsonPathStringValue("@.messagebody");
        assertThat(json.write(myMessage)).extractingJsonPathStringValue("@.messagebody")
                .isEqualTo("message text body");
    }

    @Test
    public void myMessageDeserializationTest() throws IOException {
        String expected = """
           {
               "id":99,
               "title":"Message Title",
               "messagebody": "message text body"
           }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new MyMessage(99L, "Message Title", "message text body"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).title()).isEqualTo("Message Title");
        assertThat(json.parseObject(expected).messagebody()).isEqualTo("message text body");
    }
}
