import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private static final int BUFFER_SIZE = 1024;
    public static String assetPath = System.getProperty("user.dir")+"/server/file";
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void handleIt() throws IOException {
        System.out.println("\nmethod: "+request.method);
        if(request.method.equals("GET")) {
            this.sendStaticResource();
        }
        else if(request.method.equals("POST")) {
            this.handlePost();
        }
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fs = null;
        String fileType = request.fileType;
        String contentType = "text/html";
        int statusNum = 0;
        try {
            //将web文件写入到OutputStream字节流中
            String fileParentPath = "";
            if(fileType.equals("html")) {
                fileParentPath = assetPath + "/html";
            }
            else if(fileType.equals("txt")) {
                fileParentPath = assetPath + "/txt";
            }
            else if(fileType.equals("img")) {
                fileParentPath = assetPath;
                contentType = "application/jpeg";
            }

            System.out.println("fileType: "+fileType);
            System.out.println("fileParentPath: "+fileParentPath+"\n");

            File file = new File(fileParentPath, request.url);
            if (file.exists()) {
                statusNum = 200;
                String prefixStr = "HTTP/1.1 "+ statusNum +" OK \r\n" + "Content-Type: "+ contentType +";charset=UTF-8; \r\n"
                        + "Content-Length:" + file.length() + "\r\n\r\n";
                output.write(prefixStr.getBytes());
            }else{
                statusNum = 404;
                String prefixStr = "HTTP/1.1 "+ statusNum +" Not Found\r\n" + "Content-Type: text/html;charset=UTF-8; \r\n"
                        + "Content-Length: 0 \r\n\r\n";
                output.write(prefixStr.getBytes());
            }
            if(statusNum == 200) {
                fs = new FileInputStream(file);
                int ch = fs.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fs.read(bytes, 0, BUFFER_SIZE);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fs != null)
                fs.close();
        }
    }

    private void handlePost() throws IOException {
        System.out.println("handle post function");
        System.out.println("url: "+request.url);
        int statusNum = 0;
        String url = request.url;
        if(url.equals("/dopost")) {
            statusNum = 200;
            // 响应post请求
            // 读取post参数
            int pos1 = request.uri.indexOf("login");
            System.out.println("pos1: "+pos1);
            if(pos1 != -1) {
                int pos2 = request.uri.indexOf('=', pos1+1);
                int pos3 = request.uri.indexOf('&', pos2+1);
                int pos4 = request.uri.indexOf('=', pos3+1);
                String name="", password="";
                name = request.uri.substring(pos2+1, pos3);
                password = request.uri.substring(pos4 + 1);
                System.out.println("name: " + name);
                System.out.println("password: " + password);
                if(name.equals("3170102728") && password.equals("2728")) {
                    String msg = htmlWrapper("login success!");
                    String prefixStr = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/html;charset=UTF-8; \r\n"
                            + "Content-Length: "+ msg.length() + "\r\n\r\n" + msg;
                    output.write(prefixStr.getBytes());
                } else {
                    String msg = htmlWrapper("login failed!");
                    String prefixStr = "HTTP/1.1 200 OK \r\n" + "Content-Type: text/html;charset=UTF-8; \r\n"
                            + "Content-Length: "+ msg.length() + "\r\n\r\n" + msg;
                    output.write(prefixStr.getBytes());
                }
            }
        } else {
            statusNum = 404;
            String msg = "0";
            String prefixStr = "HTTP/1.1 "+ statusNum +" Not Found\r\n" + "Content-Type: text/html;charset=UTF-8; \r\n"
                    + "Content-Length:"+ msg.length() + "\r\n\r\n" + msg;
            output.write(prefixStr.getBytes());
        }
        System.out.println("Statenum: "+statusNum+"\n");
    }

    public String htmlWrapper(String str) {
        return "<html><body>" +str+ "</body></html>";
    }
}


