package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        Resp result3 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        Resp result4 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Данные в топик nord игнорируются, потому что такого топика нет */
        Resp result5 = topicService.process(
                new Req("POST", "topic", "nord", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /*  Клиент оформил подписку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь пустая - получит пустую строку.Клиент пытается забрать данные из строки 33 и 31*/
        Resp result22 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        /*В топик weather кладется пришедшее значение temperature=18 в очереди client407 и client6565*/
        Resp result6 = topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Клиент client407 забирает значение из своей очереди*/
        Resp result7 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /*Клиент client6565 забирает значение из своей очереди*/
        Resp result8 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
        assertThat(result22.text(), is(""));
        assertThat(result3.text(), is(""));
        assertThat(result4.text(), is(""));
        assertThat(result5.text(), is(""));
        assertThat(result6.text(), is(""));
        assertThat(result7.text(), is("temperature=18"));
        assertThat(result8.text(), is("temperature=18"));
        assertThat(result1.status(), is("200"));
        assertThat(result2.status(), is("200"));
        assertThat(result22.status(), is("204"));
        assertThat(result3.status(), is("200"));
        assertThat(result4.status(), is("200"));
        assertThat(result5.status(), is("204"));
        assertThat(result6.status(), is("200"));
        assertThat(result7.status(), is("200"));
        assertThat(result8.status(), is("200"));
    }
}