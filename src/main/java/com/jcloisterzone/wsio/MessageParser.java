package com.jcloisterzone.wsio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcloisterzone.wsio.message.AbandonGameMessage;
import com.jcloisterzone.wsio.message.ChannelMessage;
import com.jcloisterzone.wsio.message.ChatMessage;
import com.jcloisterzone.wsio.message.ClientUpdateMessage;
import com.jcloisterzone.wsio.message.ClockMessage;
import com.jcloisterzone.wsio.message.CreateGameMessage;
import com.jcloisterzone.wsio.message.DeployFlierMessage;
import com.jcloisterzone.wsio.message.EndTurnMessage;
import com.jcloisterzone.wsio.message.ErrorMessage;
import com.jcloisterzone.wsio.message.GameMessage;
import com.jcloisterzone.wsio.message.GameOverMessage;
import com.jcloisterzone.wsio.message.GameSetupMessage;
import com.jcloisterzone.wsio.message.GameUpdateMessage;
import com.jcloisterzone.wsio.message.HelloMessage;
import com.jcloisterzone.wsio.message.JoinGameMessage;
import com.jcloisterzone.wsio.message.LeaveGameMessage;
import com.jcloisterzone.wsio.message.LeaveSlotMessage;
import com.jcloisterzone.wsio.message.PingMessage;
import com.jcloisterzone.wsio.message.PongMessage;
import com.jcloisterzone.wsio.message.PostChatMessage;
import com.jcloisterzone.wsio.message.RmiMessage;
import com.jcloisterzone.wsio.message.SetExpansionMessage;
import com.jcloisterzone.wsio.message.SetRuleMessage;
import com.jcloisterzone.wsio.message.SlotMessage;
import com.jcloisterzone.wsio.message.StartGameMessage;
import com.jcloisterzone.wsio.message.TakeSlotMessage;
import com.jcloisterzone.wsio.message.ToggleClockMessage;
import com.jcloisterzone.wsio.message.UndoMessage;
import com.jcloisterzone.wsio.message.WelcomeMessage;
import com.jcloisterzone.wsio.message.WsMessage;

public final class MessageParser {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    private final Gson gson;
    private final Map<String, Class<? extends WsMessage>> types = new HashMap<>();


    public MessageParser() {
        gson = new GsonBuilder().disableHtmlEscaping().create();

        registerMsgType(ErrorMessage.class);
        registerMsgType(HelloMessage.class);
        registerMsgType(WelcomeMessage.class);
        registerMsgType(CreateGameMessage.class);
        registerMsgType(JoinGameMessage.class);
        registerMsgType(LeaveGameMessage.class);
        registerMsgType(AbandonGameMessage.class);
        registerMsgType(GameMessage.class);
        registerMsgType(GameSetupMessage.class);
        registerMsgType(TakeSlotMessage.class);
        registerMsgType(LeaveSlotMessage.class);
        registerMsgType(SlotMessage.class);
        registerMsgType(SetExpansionMessage.class);
        registerMsgType(SetRuleMessage.class);
        registerMsgType(StartGameMessage.class);
        registerMsgType(DeployFlierMessage.class);
        registerMsgType(RmiMessage.class);
        registerMsgType(UndoMessage.class);
        registerMsgType(ClientUpdateMessage.class);
        registerMsgType(GameUpdateMessage.class);
        registerMsgType(PostChatMessage.class);
        registerMsgType(ChatMessage.class);
        registerMsgType(ChannelMessage.class);
        registerMsgType(GameOverMessage.class);
        registerMsgType(PingMessage.class);
        registerMsgType(PongMessage.class);
        registerMsgType(ToggleClockMessage.class);
        registerMsgType(ClockMessage.class);
        registerMsgType(EndTurnMessage.class);
    }

    protected String getCmdName(Class<? extends WsMessage> msgType) {
        return msgType.getAnnotation(WsMessageCommand.class).value();
    }

    private void registerMsgType(Class<? extends WsMessage> type) {
        types.put(getCmdName(type), type);
    }

    public WsMessage fromJson(String payload) {
        String s[] = payload.split(" ", 2); //command, arg
        Class<? extends WsMessage> type = types.get(s[0]);
        if (type == null) {
            throw new IllegalArgumentException("Mapping type is not declared for "+s[0]);
        }
        return gson.fromJson(s[1], type);
    }

    public String toJson(WsMessage arg) {
        return getCmdName(arg.getClass()) + " " + gson.toJson(arg);
    }
}
