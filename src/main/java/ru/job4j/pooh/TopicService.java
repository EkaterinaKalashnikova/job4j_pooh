package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {
private ConcurrentMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "204";
        switch (req.httpRequestType()) {
            case "POST":
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> actTopic = map.get(req.getSourceName());
                if (actTopic != null) {
                    for (ConcurrentLinkedQueue<String> cl : actTopic.values()) {
                        cl.offer(req.getParam());
                    }
                    status = "200";
                }
                break;
            case "GET":
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> newTopic = new ConcurrentHashMap<>();
                newTopic.put(req.getParam(), new ConcurrentLinkedQueue<>());
                ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> existTopic = map.putIfAbsent(req.getSourceName(), newTopic);
                if (existTopic != null) {
                    ConcurrentLinkedQueue<String> exQueue = existTopic.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
                    if (exQueue != null) {
                        if (!exQueue.isEmpty()) {
                            text = exQueue.poll();
                            status = "200";
                        }
                    } else {
                        status = "200";
                    }
                } else {
                    status = "200";
                }
                break;
            default:
        }
        return new Resp(text, status);
    }
}

