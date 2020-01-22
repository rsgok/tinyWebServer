import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket = null;
    public static int port = 2728;
    public Server() throws IOException {
        serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        System.out.println("Server starts at 127.0.0.1:"+port);
    }
    public void await() {
        while (true) {
            Socket socket = null;
            try {
                // 等待连接，连接成功后，返回一个Socket对象
                // 连接成功会返回一个Socket对象，否则一直阻塞等待
                socket = serverSocket.accept();
                Thread thread = new MyThread(socket);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}

class MyThread extends Thread {
    Socket socket = null;
    InputStream input = null;
    OutputStream output = null;
    public MyThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 两个流分别对应request请求和response响应
            input = socket.getInputStream();
            output = socket.getOutputStream();
            // 创建Request对象并解析
            Request request = new Request(input);
            request.parse();
            // 创建 Response 对象
            Response response = new Response(output);
            response.setRequest(request);
            response.handleIt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭 socket 对象
                socket.close();
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
