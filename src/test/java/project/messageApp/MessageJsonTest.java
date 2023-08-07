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
        MyMessage myMessage = new MyMessage(99L, "Hello", "this is the first message");

        assertThat(json.write(myMessage)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(myMessage)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(myMessage)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);

        assertThat(json.write(myMessage)).hasJsonPathStringValue("@.title");
        assertThat(json.write(myMessage)).extractingJsonPathStringValue("@.title")
                .isEqualTo("Hello");

        assertThat(json.write(myMessage)).hasJsonPathStringValue("@.messagebody");
        assertThat(json.write(myMessage)).extractingJsonPathStringValue("@.messagebody")
                .isEqualTo("this is the first message");
    }

    @Test
    public void myMessageDeserializationTest() throws IOException {
        String expected = """
           {
               "id":99,
               "title":"Hello",
               "messagebody": "this is the first message"
           }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new MyMessage(99L, "Hello", "this is the first message"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).title()).isEqualTo("Hello");
        assertThat(json.parseObject(expected).messagebody()).isEqualTo("this is the first message");
    }
}
