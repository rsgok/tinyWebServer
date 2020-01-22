import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Server myServer = new Server();
        myServer.await();
    }
}
