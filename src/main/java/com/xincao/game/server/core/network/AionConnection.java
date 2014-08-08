package com.xincao.game.server.core.network;

import com.xincao.game.server.core.ThreadPoolManager;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xincao.common.nio.IConnection;
import com.xincao.common.nio.IODispatcher;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.network.client.AionClientPacket;
import com.xincao.game.server.network.server.AionServerPacket;
import com.xincao.game.server.service.PlayerService;
import com.xincao.game.server.util.AC;

public class AionConnection extends IConnection {

    public static enum State {
        AUTHED_GG, AUTHED_LOGIN, CONNECTED
    }

    private static final Logger log = LoggerFactory.getLogger(AionConnection.class);
//	private final static PacketProcessor<AionConnection> processor = new PacketProcessor<AionConnection>(1, 8);
    private boolean joinedGs;
    private final Deque<AionServerPacket> sendMsgQueue = new ArrayDeque<AionServerPacket>();
    private final int sessionId = hashCode();

    private State state;

    public AionConnection(SocketChannel sc, IODispatcher ioDispatcher) throws IOException {
        super(sc, ioDispatcher);
        state = State.CONNECTED;
        String ip = getIP();
        log.info("connection from: " + ip);
    }

    public final void close(AionServerPacket closePacket, boolean forced) {
        synchronized (guard) {
            if (isWriteDisabled()) {
                return;
            }
            log.info("sending packet: " + closePacket + " and closing connection after that.");
            pendingClose = true;
            isForcedClosing = forced;
            sendMsgQueue.clear();
            sendMsgQueue.addLast(closePacket);
            enableWriteInterest();
        }
    }

    @Override
    protected final long getDisconnectionDelay() {
        return 0;
    }

    public final int getSessionId() {
        return sessionId;
    }

    public final State getState() {
        return state;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AionConnection other = (AionConnection) obj;
        return this.sessionId == other.sessionId;
    }

    /**
     * DisconnectionTask 调用（可以添加服务逻辑）
     */
    @Override
    protected final void onDisconnect() {
    	log.warn("this connection sessionId = {"+sessionId+"} will disconnection");
        Player player  = this.<Player>getObject();
        AC.getApplicationContext().getBean(PlayerService.class).logout(player);
    }

    /**
     * shutdown 调用 （可以添加服务逻辑）
     */
    @Override
    protected final void onServerClose() {
        close(true);
    }

    @Override
    protected final boolean processData(ByteBuffer data) {
        AionClientPacket pck = AionPacketHandler.handle(data, this);
        log.info("recived packet: " + pck);
        if ((pck != null) && pck.read()) {
            ThreadPoolManager.getInstance().executeGsPacket(pck);
//			processor.executePacket(pck);
        }
        return true;
    }

    public final void sendPacket(AionServerPacket bp) {
        synchronized (guard) {
            if (isWriteDisabled()) {
                return;
            }
            log.debug("sending packet: " + bp);
            sendMsgQueue.addLast(bp);
            enableWriteInterest();
        }
    }

    public final void setJoinedGs() {
        joinedGs = true;
    }

    public final void setState(State state) {
        this.state = state;
    }

    @Override
    protected final boolean writeData(ByteBuffer data) {
        synchronized (guard) {
            AionServerPacket packet = sendMsgQueue.pollFirst();
            if (packet == null) {
                return false;
            }
            log.info("send packet: " + packet);
            packet.write(this, data);
            return true;
        }
    }
}