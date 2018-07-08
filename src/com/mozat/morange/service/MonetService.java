package com.mozat.morange.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.util.MD5;
import com.mozat.morange.util.Statistics;

public class MonetService {
    private static Logger log = LoggerFactory.getLogger(MonetService.class);
    private static byte[] ping = "P".getBytes();

    public static final Statistics STATS_REQ = new Statistics();
    public static final Statistics STATS_REQ_ERR = new Statistics();

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final List<MoPacket> out_task = new ArrayList<MoPacket>();

    private volatile boolean running;

    private Thread heartBeat;
    private ThreadPoolExecutor pool;

    private String userName;
    private String password;
    private String monetAddr;
    private int monetPort;
    private int version = 1;
    private int ssl = 0;
    private int responseQueueSize = 100;
    private int workerThreadMin = 50;
    private int workerThreadMax = 50;
    private int workerKeepAlive = 60;

    static {
        STATS_REQ.register();
        STATS_REQ_ERR.register();
    }

    private MoPacket getPacket() {
        MoPacket packet = null;
        synchronized (out_task) {
            if (out_task.size() == 0) {
                try {
                    out_task.wait();
                } catch (InterruptedException e) {
                    log.error("get packet failed:", e);
                }
            } else if (out_task.size() > 0) {
                packet = out_task.remove(0);
            }
        }
        return packet;
    }

    protected void addPacket(MoPacket packet) {
        if (packet == null)
            return;
        synchronized (out_task) {
            if (out_task.size() < responseQueueSize) {
                try {
                    out_task.add(packet);
                    out_task.notifyAll();
                } catch (Exception e) {
                    log.error("add packet failed:", e);
                }
            }
        }
    }

    protected MonetService() {
        try {
            CompositeConfiguration settings = new CompositeConfiguration();
            settings.addConfiguration(new PropertiesConfiguration("config_deploy/system.properties"));
            Configuration serverConf = settings.subset("service");
            userName = serverConf.getString("username");
            password = serverConf.getString("password");
            monetAddr = serverConf.getString("monetAddr");
            monetPort = serverConf.getInt("monetPort");
            // optional configuration
            if (serverConf.containsKey("version"))
                version = serverConf.getInt("version");
            if (serverConf.containsKey("ssl"))
                ssl = serverConf.getInt("ssl");
            if (serverConf.containsKey("responseQueueSize"))
                responseQueueSize = serverConf.getInt("responseQueueSize");
            if (serverConf.containsKey("workerThreadMin"))
                workerThreadMin = serverConf.getInt("workerThreadMin");
            if (serverConf.containsKey("workerThreadMax"))
                workerThreadMax = serverConf.getInt("workerThreadMax");
            if (serverConf.containsKey("workerKeepAlive"))
                workerKeepAlive = serverConf.getInt("workerKeepAlive");
        } catch (Exception e) {
            log.error("load system setting error: ", e);
            System.exit(1);
        }
    }

//    protected void start() throws Exception {
//        init();
//        boolean login = false;
//        for (int i = 0; i < 10; i++) {
//            if (login()) {
//                login = true;
//                break;
//            }
//            Thread.sleep(3 * 1000);
//        }
//        if (login) {
//            running = true;
//            Thread t1 = new Thread(new Accepter());
//            t1.start();
//            Thread t2 = new Thread(new Sender());
//            t2.start();
//            runningBeat = true;
//            if (heartBeat == null) {
//                heartBeat = new Thread(new Heartbeat());
//                heartBeat.start();
//            }
//        } else {
//            throw new Exception("login to loginService failed");
//        }
//    }

    private void init() throws Exception {
        SocketFactory socketFactory = SocketFactory.getDefault();
        socket = socketFactory.createSocket(monetAddr, monetPort);
        socket.setKeepAlive(true);
        if (ssl == 1)
            ((SSLSocket) socket).startHandshake();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        BlockingQueue<Runnable> in_task = new SynchronousQueue<Runnable>();
        pool = new ThreadPoolExecutor(workerThreadMin, Integer.MAX_VALUE, workerKeepAlive, TimeUnit.SECONDS, in_task, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    protected void stop() {
        running = false;
        synchronized (out_task) {
            try {
                out_task.notifyAll();
            } catch (Exception e) {
                log.error("out task notify all failed:", e);
            }
        }
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null)
                socket.close();
            log.info("close socket success.");
        } catch (IOException e) {
            log.error("close in/out/socket failed:", e);
        } finally {
            in = null;
            out = null;
            socket = null;
        }

        try {
            out_task.clear();
        } catch (Exception e) {
            log.error("", e);
        }

        if (pool != null) {
            try {
                pool.shutdown();
                for (int i = 0; i < 10; i++) {
                    if (!pool.isTerminated()) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ignored) {
                        }
                    } else {
                        log.info("shut down pool success.");
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("shut down pool failed:", e);
            } finally {
                if (!pool.isTerminated())
                    log.error("shut down pool failed");
                pool = null;
            }
        }
    }

    private boolean login() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(128);
        DataOutputStream os = new DataOutputStream(bout);
        os.write(MD5.getHash(password));
        os.writeInt(version);
        os.writeInt(0);
        byte[] userBuf = userName.getBytes();
        if (userBuf.length > 104) {
            log.warn("username's length is larger than 104");
            os.write(userBuf, 0, 104);
        } else {
            os.write(userBuf);
            for (int i = 0; i < 104 - userBuf.length; i++) {
                os.write(0);
            }
        }
        os.close();
        MoPacket domainLoginPkt = new MoPacket(Type.SYSTEM, SystemID.Login,
                128, bout.toByteArray());
        domainLoginPkt.sendOut(out);

        MoPacket loginResultPkt = new MoPacket(in);
        log.debug("loginResultPacket:" + loginResultPkt.toString());
        if (loginResultPkt.type.isSystem()) {
            if (loginResultPkt.id == SystemID.LoginResult) {
                ByteArrayInputStream bin = new ByteArrayInputStream(
                        loginResultPkt.payload);
                DataInputStream is = new DataInputStream(bin);
                int status = is.readInt();
                if (status == 0) {
                    log.info("login succeed!");
                    return true;
                } else if (status == 1) {
                    log.error("login failed: Wrong username or password");
                } else if (status == 2) {
                    log.error("login failed: System under maintenance");
                } else {
                    log.error("login failed: unknown status=" + status);
                }
            }
        }
        return false;
    }

    private long packets = 0L;

//    private class Accepter implements Runnable {
//        private final Map<Integer, Task> playerTaskMap = new HashMap<Integer, Task>();
//
//        private Task getTask(int monetId, MoPacket pkt) {
//            Task t = playerTaskMap.get(monetId);
//            if (null == t) {
//                t = new Task(pkt);
//                playerTaskMap.put(monetId, t);
//                return t;
//            } else {
//                /* 成功的话仍在原线程处理请求 */
//                if (t.addPacket(pkt)) {
//                    return null;
//                }
//                t = new Task(pkt);
//                playerTaskMap.put(monetId, t);
//                return t;
//            }
//        }
//
//        public void run() {
//            while (running) {
//                MoPacket packet = null;
//                try {
//                    packet = new MoPacket(in);
//                } catch (Exception e) {
//                    if (running) {
//                        log.error("get packet from monet failed:", e);
//                        try {
//                            stop();
//                        } catch (Exception e1) {
//                            log.error("stop failed:", e1);
//                        }
//                    }
//                }
//                if (packet != null) {
//                    long t1 = System.nanoTime();
//                    try {
//                        packets++;
//                        if (packets == Long.MAX_VALUE)
//                            packets = 0;
//                        if (packet.type.isClient()) {
//                            Task task = getTask(packet.id, packet);
//                            if (task != null) {
//                                pool.execute(task);
//                            }
//                        } else {
//                            Task task = new Task(packet);
//                            pool.execute(task);
//                        }
//                    } catch (Exception e) {
//                        PlayerService.monetErrorCounts++;
//                        log.error("excute task,poolActiveCount = " + pool.getActiveCount() + ",packetId = " + packet.id + ", failed:", e);
//                    } finally {
//                        STATS_REQ.reportServiceTime(System.nanoTime() - t1);
//                    }
//                }
//            }
//        }
//    }

    private class Sender implements Runnable {
        @Override
		public void run() {
            while (running) {
                try {
                    MoPacket packet = getPacket();
                    if (packet != null) {
                        packet.sendOut(out);
                    }
                } catch (Exception e) {
                    if (running) {
                        log.error("send response to monet failed:", e);
                        try {
                            stop();
                        } catch (Exception e1) {
                            log.error("stop failed:", e1);
                        }
                    }
                }
            }
        }
    }

    private boolean runningBeat = false;

//    private class Heartbeat implements Runnable {
//        public void run() {
//            while (runningBeat) {
//                try {
//                    Thread.sleep(60000);
//                } catch (InterruptedException e) {
//                    log.error("Heartbeat,exception:", e);
//                }
//                MoPacket p = new MoPacket(Type.SYSTEM, SystemID.PingPong, 1, ping);
//                try {
//                    p.sendOut(out);
//                    //log.debug("PingPong");
//                    log.info("WorkThreadPool active threads:" + pool.getActiveCount() +
//                            ", total threads:" + pool.getPoolSize() + ", max threads:" + pool.getMaximumPoolSize());
//                } catch (Exception e) {
//                    log.error("PingPong failed:", e);
//                    try {
//                        stop();
//                        start();
//                    } catch (Exception e1) {
//                        log.error("start service failed:", e1);
//                    }
//                }
//            }
//        }
//    }

    int getActiveThreadCount() {
        return pool.getActiveCount();
    }

    int getInitThreadCount() {
        return workerThreadMin;
    }

    int getMaxThreadCount() {
        return workerThreadMax;
    }

    int getCurrentThreadCount() {
        return pool.getPoolSize();
    }

    public int getReqQueueSize() {
        return pool.getQueue().size();
    }

    public int getRespQueueSize() {
        return out_task.size();
    }

    public int getActiveReqCount() {
        return pool.getActiveCount();
    }
}
