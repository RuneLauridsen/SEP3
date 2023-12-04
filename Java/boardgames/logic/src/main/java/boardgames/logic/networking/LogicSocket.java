package boardgames.logic.networking;

import boardgames.logic.messages.MessageQueue;
import boardgames.logic.messages.Message;
import boardgames.logic.messages.Messages;
import boardgames.logic.messages.Messages.*;
import boardgames.logic.messages.QueuedMessage;
import boardgames.shared.util.JsonUtil;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LogicSocket {
    private final Socket socket;
    private final DataInputStream inFromClient;
    private final DataOutputStream outToClient;

    private final int clientIdent;
    private final MessageQueue incomingQueue;
    private final MessageQueue outgoingQueue;
    private boolean quit;

    public LogicSocket(Socket socket, int clientIdent, MessageQueue incomingQueue) throws IOException {
        this.socket = socket;

        this.inFromClient = new DataInputStream(socket.getInputStream());
        this.outToClient = new DataOutputStream(socket.getOutputStream());
        this.clientIdent = clientIdent;

        this.incomingQueue = incomingQueue;
        this.outgoingQueue = new MessageQueue();
    }

    public int clientIdent() {
        return clientIdent;
    }

    public void readerThreadProc() {
        try {
            while (!quit) {
                String s = StringStreamer.readString(inFromClient);
                Message m = MessageSerializer.deserialize(s);
                if (m != null) {
                    incomingQueue.post(m, clientIdent);
                }
            }
        } catch (IOException e) {
            // IOException når klient har lukket forbindelse.
        } finally {
            quit();
        }
    }

    public void writerThreadProc() {
        try {
            while (!quit) {
                QueuedMessage outgoingMessage = outgoingQueue.pull(10_000);
                if (outgoingMessage != null) {
                    assert outgoingMessage.clientIdent() == clientIdent;
                    Message m = outgoingMessage.message();
                    String s = MessageSerializer.serialize(m);
                    StringStreamer.sendString(outToClient, s);
                }
            }
        } catch (IOException e) {
            // IOException når klient har lukket forbindelse.
        } finally {
            quit();
        }
    }

    private void quit() {
        quit = true;

        // NOTE: I tilfælde af at socket bliver abrudt, poster vi altid en QuitNotification
        // (også selvom klient selv allerede har sendt en), så LogicServerModelImpl ved at denne
        // clientIdent ikke længere lytter.
        incomingQueue.post(new Message(new QuitNotification(), 0), clientIdent);
    }

    public MessageQueue outgoingQueue() {
        return outgoingQueue;
    }

    public void close() {
        try {
            inFromClient.close();
            outToClient.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
