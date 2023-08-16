package project.messageApp;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private JacksonTester<MyMessage[]> jsonList;

        private MyMessage[] myMessages;
        @BeforeEach
        void setUp() {
            myMessages = Arrays.array(

            new MyMessage(99L, "Hello", "this is the first message"),
            new MyMessage(100L, "Hi", "this is the second message"),
            new MyMessage(101L, "Ciao", "this is the third message"));

}



    @Test
    public void myMessageSerializationTest() throws IOException {
        MyMessage myMessage = new MyMessage(99L, "Hello", "this is the first message");

        assertThat(json.write(myMessage)).isStrictlyEqualToJson("single.json");

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

    @Test
    void messageListSerializationTest() throws IOException {
        assertThat(jsonList.write(myMessages)).isStrictlyEqualToJson("list.json");
    }
    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected="""
                [
                  {
                           "id": 99,
                           "title": "Hello",
                           "messagebody": "this is the first message"
                         },
                           {
                             "id": 100,
                             "title": "Hi",
                             "messagebody": "this is the second message"
                           },
                           {
                             "id": 101,
                             "title": "Ciao",
                             "messagebody": "this is the third message"
                           }
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(myMessages);
    }
}
