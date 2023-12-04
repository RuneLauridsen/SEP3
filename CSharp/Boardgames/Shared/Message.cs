namespace Shared;

public class Message {
    public Head Head { get; }
    public object Body { get; }

    public Message(Head head, object body) {
        Head = head;
        Body = body;
    }

    public Message(object body, string jwt) {
        Head = new Head(body.GetType().Name, jwt, 0.0);
        Body = body;
    }
}
