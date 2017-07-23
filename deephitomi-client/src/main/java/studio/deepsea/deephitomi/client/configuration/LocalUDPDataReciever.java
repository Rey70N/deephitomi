package studio.deepsea.deephitomi.client.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

public class LocalUDPDataReciever
{
	private static final Logger LOG = LoggerFactory.getLogger(LocalUDPDataReciever.class);
	private static LocalUDPDataReciever instance = null;
	private Thread thread = null;

	public static LocalUDPDataReciever getInstance()
	{
		if (instance == null)
			instance = new LocalUDPDataReciever();
		return instance;
	}

	public void startup()
	{
		this.thread = new Thread(new Runnable()
		{

			public void run()
			{
				String localUDPPort;
				Properties prop = new Properties();
				try
				{
					prop.load(this.getClass().getResourceAsStream("/ClientConfig.properties"));
					localUDPPort = prop.getProperty("localUDPPort");
					LOG.debug("本地UDP端口侦听中，端口=" + localUDPPort + "...");
					//Log.d(LocalUDPDataReciever.TAG, "本地UDP端口侦听中，端口=" + localUDPPort + "...");

					//开始侦听
					LocalUDPDataReciever.this.udpListeningImpl();
				} catch (Exception e)
				{
					//Log.w(LocalUDPDataReciever.TAG, "本地UDP监听停止了(socket被关闭了?)," + e.getMessage(), e);
					LOG.warn("本地UDP监听停止了(socket被关闭了?)," + e.getMessage(), e);
				}
			}
		});
		this.thread.start();
	}

	private void udpListeningImpl() {
		byte[] data = new byte[1024];
		// 接收数据报的包
		DatagramPacket packet = new DatagramPacket(data, data.length);
		while (true) {
			try {
				DatagramSocket localUDPSocket = LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
				if ((localUDPSocket == null) || (localUDPSocket.isClosed()))
					continue;
				// 阻塞直到收到数据
				localUDPSocket.receive(packet);
			} catch (IOException e) {
				LOG.error("IO异常", e.getMessage(), e);
				break;
			}
			try {
				// 解析服务端发过来的数据
				String pFromServer = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
				//Log.w(LocalUDPDataReciever.TAG, "【NOTE】>>>>>> 收到服务端的消息："+pFromServer);
				LOG.info("【NOTE】>>>>>> 收到服务端的消息：" + pFromServer);
			} catch (UnsupportedEncodingException e) {
				LOG.error("不支持的编码异常", e.getMessage(), e);
				break;
			}
		}
	}
}