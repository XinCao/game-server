package com.xincao.game.server.core.network;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xincao.game.server.core.network.AionConnection.State;
import com.xincao.game.server.network.client.AionClientPacket;
import com.xincao.game.server.network.client.CM_DELETE_MAIL;
import com.xincao.game.server.network.client.CM_LOGIN;
import com.xincao.game.server.network.client.CM_MAILLIST_INFO;
import com.xincao.game.server.network.client.CM_READ_MAIL;
import com.xincao.game.server.network.client.CM_SEND_MAIL;
import com.xincao.game.server.network.server.AionServerPacket;
import com.xincao.game.server.network.server.SM_HEART_BEAT;
import com.xincao.game.server.network.server.SM_MAILLIST_INFO;

public class AionPacketHandler {

    public enum AionClientKind {
        CM_LOGIN(0x0002, CM_LOGIN.class, State.CONNECTED),
        CM_MAILLIST_INFO(0x0003, CM_MAILLIST_INFO.class, State.CONNECTED),
        CM_SEND_MAIL(0x0004, CM_SEND_MAIL.class, State.CONNECTED),
        CM_READ_MAIL(0x0005, CM_READ_MAIL.class, State.CONNECTED),
        CM_DELETE_MAIL(0x0006, CM_DELETE_MAIL.class, State.CONNECTED),
        ;
        public static final Map<Integer, AionClientKind> authedGGAionClientKindMap = new HashMap<Integer, AionClientKind>();
        public static final Map<Integer, AionClientKind> authedLoginAionClientKindMap = new HashMap<Integer, AionClientKind>();
        public static final Map<Integer, AionClientKind> connectedAionClientKindMap = new HashMap<Integer, AionClientKind>();

        static {
            for (AionClientKind ack : values()) {
                switch (ack.getState()) {
                    case AUTHED_LOGIN: {
                        authedLoginAionClientKindMap.put(ack.getOpcode(), ack);
                        break;
                    }
                    case CONNECTED: {
                        connectedAionClientKindMap.put(ack.getOpcode(), ack);
                        break;
                    }
                    case AUTHED_GG: {
                        authedGGAionClientKindMap.put(ack.getOpcode(), ack);
                        break;
                    }
                }
            }
        }
        private final Class<AionClientPacket> clazz;
        private final int opcode;
        private final State state;

        private AionClientKind(int opcode, Class clazz, State state) {
            this.opcode = opcode;
            this.clazz = clazz;
            this.state = state;
        }

        public Class<AionClientPacket> getClazz() {
            return this.clazz;
        }

        public int getOpcode() {
            return this.opcode;
        }

        public State getState() {
            return this.state;
        }
    }

    public static enum AionServerKind {
        SM_HEART_BEAT(0x0002, SM_HEART_BEAT.class, State.CONNECTED),
        SM_MAILLIST_INFO(0x0003, SM_MAILLIST_INFO.class, State.CONNECTED)
        ;
        private Class<AionServerPacket> clazz;
        private int opcode;
        private State state;

        private AionServerKind(int opcode, Class clazz, State state) {
            this.opcode = opcode;
            this.clazz = clazz;
            this.state = state;
        }

        public Class<AionServerPacket> getClazz() {
            return this.clazz;
        }

        public int getOpcode() {
            return this.opcode;
        }

        public State getState() {
            return this.state;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(AionPacketHandler.class);

    /**
     * 获得客户端包
     *
     * @param data
     * @param client
     * @return
     */
    public static AionClientPacket handle(ByteBuffer data, AionConnection client) {
        AionClientPacket msg = null;
        State state = client.getState();
        int opcode = data.get() & 0xff;
        AionClientKind ack = null;
        switch (state) {
            case CONNECTED: {
                ack = AionClientKind.connectedAionClientKindMap.get(opcode);
                break;
            } case AUTHED_GG: {
                ack = AionClientKind.authedGGAionClientKindMap.get(opcode);
                break;
            } case AUTHED_LOGIN: {
                ack = AionClientKind.authedLoginAionClientKindMap.get(opcode);
                break;
            } default: {
                unknownPacket(state, opcode);
            }
        }
        if (ack != null) {
            try {
                Class<AionClientPacket> clazz = ack.getClazz();
                Constructor<AionClientPacket> constructor = clazz.getConstructor(ByteBuffer.class, AionConnection.class, Integer.class);
                msg = constructor.newInstance(data, client, ack.getOpcode());
            } catch (NoSuchMethodException ex) {
                log.warn(ex.getMessage());
            } catch (SecurityException ex) {
                log.warn(ex.getMessage());
            } catch (InstantiationException ex) {
                log.warn(ex.getMessage());
            } catch (IllegalAccessException ex) {
                log.warn(ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.warn(ex.getMessage());
            } catch (InvocationTargetException ex) {
                log.warn(ex.getMessage());
            }
        }
        return msg;
    }

    private static void unknownPacket(State state, int id) {
        log.warn(String.format("Unknown packet recived from Aion client: 0x%02X state=%s", id, state.toString()));
    }
}