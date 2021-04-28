package bean;

public class ChatMessage {
    private String name;//sender
    private String msg;//send msg

    public ChatMessage() {
    }

    public ChatMessage(String name, String msg) {
        super();
        this.name = name;
        this.msg = msg;
    }

    public ChatMessage(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}