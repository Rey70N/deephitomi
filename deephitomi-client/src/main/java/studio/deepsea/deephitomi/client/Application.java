package studio.deepsea.deephitomi.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import studio.deepsea.deephitomi.client.configuration.LocalUDPDataReciever;
import studio.deepsea.deephitomi.client.configuration.LocalUDPSocketProvider;
import studio.deepsea.deephitomi.client.utils.UDPUtils;

@EnableAutoConfiguration
@ComponentScan(basePackages={"studio.deepsea.deephitomi.client.*"})
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        echoClient();
    }

    private static void echoClient() {

        // 初始化本地UDP的Socket
        LocalUDPSocketProvider.getInstance().initSocket();
        // 启动本地UDP监听（接收数据用的）
        LocalUDPDataReciever.getInstance().startup();
        // 要发送的数据
        String toServer = "Hi，我是客户端，我的时间戳" + System.currentTimeMillis();

        // 循环发送数据给服务端
        while (true) {
            try {
                byte[] soServerBytes = toServer.getBytes("UTF-8");
                // 开始发送
                boolean ok = UDPUtils.send(soServerBytes, soServerBytes.length);
                if (ok) {
                    LOG.info("发往服务端的信息已送出");
                    //Log.d("EchoClient", "发往服务端的信息已送出.");
                } else {
                    LOG.warn("发往服务端的信息没有成功发出！！！");
                    //Log.e("EchoClient", "发往服务端的信息没有成功发出！！！");
                }
            } catch (Exception e) {
                LOG.error("字符编码不受支持", e.getMessage(), e);
                break;
            }
            try {
                // 3000秒后进入下一次循环
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LOG.error("线程被意外打断", e.getMessage(), e);
                break;
            }
        }
    }
}