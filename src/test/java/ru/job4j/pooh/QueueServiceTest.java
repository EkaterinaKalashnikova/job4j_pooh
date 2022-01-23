package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        Resp result3 = queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        assertThat(result3.text(), is(""));
        assertThat(result3.status(), is("200"));
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
        assertThat(result.status(), is("200"));
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result1.text(), is(""));
        assertThat(result1.status(), is("204"));
    }
}