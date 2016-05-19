import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.lang.Thread;

public class Listener extends ServerSocket {
    public Listener(int port, int backlog, InetAddress bindAddr)
        throws IOException
    {
        super(port, backlog, bindAddr);
    }

    public void loop() {
        while (true) {
            try {
                Socket sock = this.accept();
                Connection conn = new Connection(sock);
                new Thread(conn).start();
            } catch (Exception err) {
                // Nothing to do here.
                continue;
            }
        }
    }
}
