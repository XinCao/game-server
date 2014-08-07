package com.xincao.game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支持单人登陆消息支持，和多人在线支持
 * 
 * @author caoxin
 */
public class AionClient {

    private static final boolean SINGLE_PLAYER = true; // 单人账户模拟消息

    static class InputInfo implements Runnable {

        private static final Logger logger = LoggerFactory.getLogger(InputInfo.class);
        private InputStream inputStream;

        public InputInfo(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public boolean go(ByteBuffer byteBuffer) {
            byte opcode = byteBuffer.get();
            StringBuilder sb = new StringBuilder();
            char c;
            while ((c = byteBuffer.getChar()) != '\000') {
                sb.append(c);
            }
            logger.info(String.format("packet opcode = {%d} date = {%s}", opcode, sb.toString()));
            return true;
        }

        private boolean parse(ByteBuffer buf) {
            short sz = 0;
            try {
                buf.reset();
                sz = buf.getShort();
                if (sz > 1) {
                    sz -= 2;
                }
                ByteBuffer b = (ByteBuffer) buf.slice().limit(sz); // 创建新的缓冲区
                b.order(ByteOrder.LITTLE_ENDIAN); // 小端模式，高字节存储在高地址
                buf.position(buf.position() + sz); // 写一个包数据开始处
                buf.mark();
                return go(b);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        private void read(InputStream inputStream, ByteBuffer inputByteBuffer) {
            int numRead = 0;
            try {
                numRead = inputStream.read(inputByteBuffer.array());
            } catch (IOException ex) {
            }
            if (numRead < 0) {
                return;
            } else {
                inputByteBuffer.position(numRead);
            }
            inputByteBuffer.flip();
            inputByteBuffer.mark();
            while (inputByteBuffer.remaining() > 2 && inputByteBuffer.remaining() >= inputByteBuffer.getShort(inputByteBuffer.position())) { // 读取是否为一个整包（也可能大于一个整包（多个包），因此这里会使用循环）
                if (!parse(inputByteBuffer)) { // 判断包是否合法
                    return;
                }
            }
            if (inputByteBuffer.hasRemaining()) {
                inputByteBuffer.compact(); // 将缓冲区的当前位置和界限之间的字节复制到缓冲区的开始处（为下一个包准备）
            } else {
                inputByteBuffer.clear();
            }
        }

        @Override
        public void run() {
            ByteBuffer inputByteBuffer = ByteBuffer.allocate(2048);
            inputByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            logger.info("Begin to accept data!");
            do {
                this.read(inputStream, inputByteBuffer);
            } while (SINGLE_PLAYER);
        }
    }

    static class OutputInfo implements Runnable {

        private static final Logger logger = LoggerFactory.getLogger(OutputInfo.class);
        private BufferedReader outBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        private OutputStream outputStream;

        public OutputInfo(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            ByteBuffer outputByteBuffer = ByteBuffer.allocate(256);
            outputByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            String outputStr = "Start send info!";
            logger.info(outputStr);
            do {
                try {
                    if ((outputStr = outBufferedReader.readLine()) != null) {
                        int sizePosition = outputByteBuffer.position();
                        outputByteBuffer.putShort((short) 0);
                        String[] words = outputStr.split("\\s");
                        int num = words.length;
                        if (num % 2 != 0) {
                            logger.info("Please check the input data!");
                            continue;
                        }
                        for (int i = 0; i < num; i = i + 2) {
                            putValue(words[i], words[i + 1], outputByteBuffer);
                        }
                        int afterPosition = outputByteBuffer.position();
                        outputByteBuffer.position(sizePosition);
                        outputByteBuffer.putShort((short) (afterPosition - sizePosition));
                        outputByteBuffer.position(afterPosition);
                        if (outputByteBuffer.hasArray()) {
                            byte[] b = Arrays.copyOfRange(outputByteBuffer.array(), outputByteBuffer.arrayOffset(), outputByteBuffer.position());
                            outputStream.write(b);
                        }
                        outputByteBuffer.clear();
                    }
                } catch (IOException ex) {
                    logger.debug(ex.getMessage());
                }
            } while (SINGLE_PLAYER);
        }
    }

    public static void putValue(String type, String value, ByteBuffer bb) {
        if ("byte".equalsIgnoreCase(type)) {
            bb.put(Byte.valueOf(value));
        } else if ("char".equalsIgnoreCase(type)) {
            bb.putChar(value.charAt(0));
        } else if ("short".equalsIgnoreCase(type)) {
            bb.putShort(Short.valueOf(value));
        } else if ("int".equalsIgnoreCase(type)) {
            bb.putInt(Integer.valueOf(value));
        } else if ("long".equalsIgnoreCase(type)) {
            bb.putLong(Long.valueOf(value));
        } else {
            for (int j = 0; j < value.length(); j++) {
                bb.putChar(value.charAt(j));
            }
            bb.putChar('\000');
        }
    }

    public static void main(String... args) throws Exception {
        BufferedReader cmdBufferReader = new BufferedReader(new InputStreamReader(System.in));
        String[] cmd = cmdBufferReader.readLine().split("\\s");
        String ip = "127.0.0.1";
        int port = 0;
        int length = cmd.length;
        if ((length == 1 && "help".equals(cmd[0])) || length == 0) {
            System.out.println("use : {ip}, {port} if {ip} is null or {*} default '127.0.0.1'");
            System.exit(1);
        } else if (length == 1 && cmd[0].equals("default")) {
            port = 8888;
        } else if (length == 1) {
            port = new Integer(cmd[0]);
        } else if (length == 2) {
            ip = cmd[0];
            port = new Integer(cmd[1]);
        }
        if (SINGLE_PLAYER) {
            Socket socket = new Socket(ip, port);
            OutputStream outputStream = socket.getOutputStream();
            new Thread(new OutputInfo(outputStream), "send message thread").start();
            InputStream inputStream = socket.getInputStream();
            new Thread(new InputInfo(inputStream), "receive message thread").start();
        } else {
            int max = 10;
            int loop = 0;
            while (++loop <= max) {
                newClient(ip, port);
            }
            loop = 0;
            while (++loop <= max) {
                logger.info("this is the socket no = {" + loop + "}");
                Socket socket = getSocket();
                OutputStream outputStream = socket.getOutputStream();
                new OutputInfo(outputStream).run();
                InputStream inputStream = socket.getInputStream();
                new InputInfo(inputStream).run();
                socket.close();
            }
        }
    }

    private static List<Socket> socketPool = new ArrayList<Socket>();
    private static int no = 0;
    private static final Logger logger = LoggerFactory.getLogger(AionClient.class);

    private static Socket getSocket() {
        if (no >= socketPool.size()) {
            no = 0;
        }
        return socketPool.remove(no++);
    }

    private static void newClient(String ip, int port) throws Exception {
        Socket socket = new Socket(ip, port);
        socketPool.add(socket);
    }
}