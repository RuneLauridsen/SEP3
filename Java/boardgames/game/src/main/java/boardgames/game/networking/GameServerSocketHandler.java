package boardgames.game.networking;

import boardgames.game.messages.Messages;
import boardgames.game.messages.Messages.*;
import boardgames.game.model.GameServerModel;
import boardgames.shared.dto.Account;
import boardgames.shared.util.JsonUtil;
import com.google.gson.JsonSyntaxException;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static boardgames.game.messages.Messages.LoginRequest;

// NOTE(rune): Håndterer enkelt forbindelse til enkelt klient.
public class GameServerSocketHandler implements Runnable {
    private final Socket socket;
    private final GameServerModel model;
    private final DataInputStream inFromClient;
    private final DataOutputStream outToClient;

    // NOTE(m2dx): Så længe activeAccount er null er bruger ikke logget ind.
    private Account account;

    public GameServerSocketHandler(Socket socket, GameServerModel model) throws IOException {
        this.socket = socket;
        this.model = model;

        this.inFromClient = new DataInputStream(socket.getInputStream());
        this.outToClient = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = readMessage();
                Object response = getResponseForRequest(request);
                // TODO(rune): reponse null check -> getResponseForRequest() giver null, hvis client sender invalid data.
                sendMessage(response);
            }
        } catch (IOException e) {
            // NOTE(rune): Når server er ved at lukke smider readObject() en IOException
            // fordi readObject() stream bliver lukket, så readObject() ikke blokerer tråden længere.
            // Hvis vi IOException har en anden årsag, lukker vi også bare socket'en.
        }
    }

    private Object getResponseForRequest(Object request) {
        // TODO(rune): Anti-solid genopstår \o/, men vi kan godt lave noget ulækkert
        // refelection i stedet, eller bare et map der oversætter T -> Consumer<T>.

        if (request instanceof LoginRequest req) {
            LoginResponse res = model.login(req);
            account = res.account();
            return res;
        }

        if (request instanceof MoveRequest req) {
            MoveResponse res = model.move(req, account);
            return res;
        }

        if (request instanceof GetMatchesRequest req) {
            GetMatchesResponse res = model.getMatches(req, account);
            return res;
        }

        if (request instanceof GetGamesRequest req) {
            GetGamesResponse res = model.getGames(req);
            return res;
        }

        if (request instanceof CreateMatchRequest req) {
            CreateMatchResponse res = model.createMatch(req);
            return res;
        }

        return null;
    }

    private void sendString(String string) throws IOException {
        outToClient.writeUTF(string);
        outToClient.flush();
    }

    private String readString() throws IOException {
        String s = inFromClient.readUTF();
        return s;
    }

    private Object readMessage() throws IOException {
        String full = readString();
        int idx = full.indexOf('|');
        if (idx > 0 && idx < full.length() - 1) {
            String head = full.substring(0, idx);
            String body = full.substring(idx + 1);

            try {
                String expected = LoginRequest.class.getName();
                String messageTypeName = Messages.class.getName() + "$" + head; // NOTE(rune): "$" betyder nested class.
                Class<?> messageType = Class.forName(messageTypeName);
                Object ret = JsonUtil.fromJson(body, messageType);
                return ret;
            } catch (JsonSyntaxException e) {
                // TODO(rune): Logging.
                return null;
            } catch (ClassNotFoundException e) {
                // TODO(rune): Logging.
                return null;
            }
        } else {
            // TODO(rune): Logging.
            return null;
        }
    }

    private void sendMessage(Object object) throws IOException {
        String head = object.getClass().getSimpleName();
        String body = JsonUtil.toJson(object);
        String full = head + "|" + body;
        sendString(full);
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
