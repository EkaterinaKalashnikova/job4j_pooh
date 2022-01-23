package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] strings = content.split(System.lineSeparator());
        String[] data = strings[0].split("/");
        String httpRequestType = data[0].trim();
        String poohMode = data[1];
        String sourceName = data[2].split(" ")[0];
        String param;
        switch (httpRequestType) {
            case "POST":
                param = strings[strings.length - 1];
                break;
            case "GET":
                param = data.length == 5 ? data[3].split(" ")[0] : "";
                break;
            default:
                throw new IllegalArgumentException("Неверно введены данные!!!");
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
