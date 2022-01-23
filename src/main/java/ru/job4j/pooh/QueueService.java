package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class QueueService implements Service {
    ConcurrentMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "204";
        switch (req.httpRequestType()) {
            case "POST":
                ConcurrentLinkedQueue<String> newQueue = new ConcurrentLinkedQueue<>();
                newQueue.add(req.getParam());
                ConcurrentLinkedQueue<String> existQueue = map.putIfAbsent(req.getSourceName(), newQueue);
                if (existQueue != null) {
                    existQueue.add(req.getParam());
                }
                status = "200";
                break;
            case "GET":
                ConcurrentLinkedQueue<String> actQueue = map.get(req.getSourceName());
                if (actQueue != null && !actQueue.isEmpty()) {
                    text = actQueue.poll();
                    status = "200";
                }
                break;
            default:
        }
        return new Resp(text, status);
    }
}
