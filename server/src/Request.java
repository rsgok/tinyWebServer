import java.io.IOException;
import java.io.InputStream;

public class Request {
    public InputStream input;
    public String uri = "";
    public String url = "";
    public String method = "";
    public String fileType = "";

    public Request(InputStream input) {
        this.input = input;
    }

    //从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int len = -1;
        byte[] buffer = new byte[2048];
        try {
            len = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < len; j++) {
            request.append((char) buffer[j]);
        }
        this.uri = request.toString();
        System.out.print(uri);
        parseUri(uri);
    }

    /**
     *
     * requestString形式如下：
     * GET /index.html HTTP/1.1
     * Host: localhost:8080
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * ...
     * 该函数目的就是为了获取/index.html字符串
     */
    public void parseUri(String uriString) {
        // get url
        int index1, index2;
        index1 = uriString.indexOf(' ');
        if (index1 != -1) {
            index2 = uriString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                this.url = uriString.substring(index1 + 1, index2);
        }

        // get method
        int index = uriString.indexOf("GET");
        if(index != -1) {
            this.method = "GET";
        }
        index = uriString.indexOf("POST");
        if(index != -1) {
            this.method = "POST";
        }

        // get fileType
        if(this.method != "GET") {
            return;
        }
        index1 = uriString.indexOf(".jpg");
        index2 = uriString.indexOf("HTTP");
        if(index1 != -1 && index1<index2) {
            this.fileType = "img";
            return;
        }
        index = uriString.indexOf(".html");
        if(index != -1) {
            this.fileType = "html";
        }
        index = uriString.indexOf(".txt");
        if(index != -1) {
            this.fileType = "txt";
        }
    }

}
