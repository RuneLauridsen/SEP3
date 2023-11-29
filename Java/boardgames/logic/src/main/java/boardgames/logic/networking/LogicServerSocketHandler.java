package boardgames.logic.networking;

import boardgames.logic.messages.Message;
import boardgames.logic.messages.Messages;
import boardgames.logic.messages.Messages.*;
import boardgames.logic.model.LogicServerModel;
import boardgames.logic.model.NotAuthorizedException;
import boardgames.shared.dto.Account;
import boardgames.shared.dto.ScoreSum;
import boardgames.shared.util.JsonUtil;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static boardgames.logic.messages.Messages.LoginRequest;

// NOTE(rune): Håndterer enkelt forbindelse til enkelt klient.
public class LogicServerSocketHandler implements Runnable {
    private final Socket socket;
    private final LogicServerModel model;
    private final DataInputStream inFromClient;
    private final DataOutputStream outToClient;

    public LogicServerSocketHandler(Socket socket, LogicServerModel model) throws IOException {
        this.socket = socket;
        this.model = model;

        this.inFromClient = new DataInputStream(socket.getInputStream());
        this.outToClient = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message incomingMessage = readMessage();
                long nanoBegin = System.nanoTime();
                Object response = getResponseForRequest(incomingMessage);
                long nanoEnd = System.nanoTime();
                long nanoDiff = nanoEnd - nanoBegin;
                double elapsedMillis = (double) nanoDiff / (1000.0 * 1000.0);

                // TODO(rune): reponse null check -> getResponseForRequest() giver null, hvis client sender invalid data.
                sendMessage(response, elapsedMillis);
            }
        } catch (IOException e) {
            // NOTE(rune): Når server er ved at lukke smider readObject() en IOException
            // fordi readObject() stream bliver lukket, så readObject() ikke blokerer tråden længere.
            // Hvis vi IOException har en anden årsag, lukker vi også bare socket'en.
        }
    }

    private Object getResponseForRequest(Message msg) {
        // TODO(rune): Anti-solid genopstår \o/, men vi kan godt lave noget ulækkert
        // refelection i stedet, eller bare et map der oversætter T -> Consumer<T>.
        String jwt = msg.head().jwt();
        Object body = msg.body();

        try {
            if (body instanceof LoginRequest req) {
                LoginResponse res = model.login(req);
                return res;
            }

            if (body instanceof MoveReq req) {
                MoveRes res = model.move(req, jwt);
                return res;
            }

            if (body instanceof GetMatchReq req) {
                GetMatchRes res = model.getMatch(req, jwt);
                return res;
            }

            if (body instanceof GetMyMatchesRequest req) {
                GetMyMatchesResponse res = model.getMyMatches(req, jwt);
                return res;
            }

            if (body instanceof GetGamesRequest req) {
                GetGamesResponse res = model.getGames(req, jwt);
                return res;
            }

            if (body instanceof GetAccountReq req) {
                GetAccountRes res = model.getAccount(req, jwt);
                return res;
            }

            if (body instanceof GetAccountsReq req) {
                GetAccountsRes res = model.getAccounts(req, jwt);
                return res;
            }

            if (body instanceof CreateMatchRequest req) {
                CreateMatchResponse res = model.createMatch(req, jwt);
                return res;
            }

            if (body instanceof AddParticipantReq req) {
                AddParticipantRes res = model.addParticipant(req, jwt);
                return res;
            }

            if (body instanceof GetParticipantsReq req) {
                GetParticipantsRes res = model.getParticipants(req, jwt);
                return res;

            }
            if (body instanceof GetPendingReq req) {
                GetPendingRes res = model.getPending(req, jwt);
                return res;

            }

            if (body instanceof DecidePendingReq req) {
                DecidePendingRes res = model.decidePending(req, jwt);
                return res;
            }

            if (body instanceof UpdateUserStatusRequest req) {
                UpdateUserStatusResponse res = model.approveUserReg(req, jwt);
                return res;
            }

            if (body instanceof UpdateAccountReq req) {
                UpdateAccountRes res = model.updateAccount(req, jwt);
                return res;
            }

            if (body instanceof GetScoreSumsRequest req) {
                GetScoreSumsResponse res = model.getScoreSums(req, jwt);
                return res;
            }

            if (body instanceof GetMatchHistoryRequest req) {
                GetMatchHistoryResponse res = model.getMatchHistory(req, jwt);
                return res;
            }

        } catch (NotAuthorizedException e) {
            return new NotAuthorizedResponse();
        }

        return null;
    }

    private void sendString(String string) throws IOException {
        outToClient.writeInt(string.length());
        outToClient.writeChars(string);
        outToClient.flush();
    }

    private String readString() throws IOException {
        int len = inFromClient.readInt();
        byte[] bytes = inFromClient.readNBytes(len * 2);
        String s = new String(bytes, StandardCharsets.UTF_16);
        return s;
    }

    private Message readMessage() throws IOException {
        String full = readString();
        int idx = full.indexOf('|');
        if (idx > 0 && idx < full.length() - 1) {
            String headString = full.substring(0, idx);
            String bodyString = full.substring(idx + 1);

            try {
                Head head = JsonUtil.fromJson(headString, Head.class);
                String bodyTypeName = Messages.class.getName() + "$" + head.bodyType(); // NOTE(rune): "$" betyder nested class.
                Class<?> bodyType = Class.forName(bodyTypeName);
                Object body = JsonUtil.fromJson(bodyString, bodyType);
                return new Message(head, body);
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

    private void sendMessage(Object body, double elapsedMillis) throws IOException {
        String bodyTypeName = body.getClass().getSimpleName();
        Head head = new Head(bodyTypeName, "", elapsedMillis);
        String headString = JsonUtil.toJson(head);
        String bodyString = JsonUtil.toJson(body);
        String full = headString + "|" + bodyString;
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
