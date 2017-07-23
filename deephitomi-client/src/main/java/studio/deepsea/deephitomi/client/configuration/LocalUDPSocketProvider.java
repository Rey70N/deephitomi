package studio.deepsea.deephitomi.client.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class LocalUDPSocketProvider {
    private static final Logger LOG = LoggerFactory.getLogger(LocalUDPSocketProvider.class);
    private static LocalUDPSocketProvider instance = null;

    private DatagramSocket localUDPSocket = null;

    public static LocalUDPSocketProvider getInstance() {
        if (instance == null)
            instance = new LocalUDPSocketProvider();
        return instance;
    }

    public void initSocket() {
        String serverIP;
        String serverUDPPort;
        String localUDPPort;
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getResourceAsStream("/ClientConfig.properties"));
            serverIP = prop.getProperty("serverIP");
            serverUDPPort = prop.getProperty("serverUDPPort");
            localUDPPort = prop.getProperty("localUDPPort");
            // UDP本地监听端口（如果为0将表示由系统分配，否则使用指定端口）
            this.localUDPSocket = new DatagramSocket(Integer.parseInt(localUDPPort));
            // 调用connect之后，每次send时DatagramPacket就不需要设计目标主机的ip和port了
            // * 注意：connect方法一定要在DatagramSocket.receive()方法之前调用，
            // * 不然整send数据将会被错误地阻塞。这或许是官方API的bug，也或许是调
            // * 用规范就应该这样，但没有找到官方明确的说明
            this.localUDPSocket.connect(
                    InetAddress.getByName(serverIP), Integer.parseInt(serverUDPPort));
            this.localUDPSocket.setReuseAddress(true);
            LOG.debug("new DatagramSocket()已成功完成.");
        } catch (Exception e) {
            LOG.warn("localUDPSocket创建时出错，原因是：" + e.getMessage(), e);
        }
    }

    public DatagramSocket getLocalUDPSocket() {
        return this.localUDPSocket;
    }
}
