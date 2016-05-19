import java.net.Socket;
import java.io.InputStream;
import java.lang.Runnable;

public class Connection implements Runnable {
    private Socket socket = null;

    public Connection(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            InputStream istream = this.socket.getInputStream();
        ) {
            byte[] buf = new byte[4096];
            int nread = 0;
            while ((nread = istream.read(buf)) > 0) {
                System.out.write(buf, 0, nread);
            }
        } catch (Exception err) {
        }

        try {
            this.socket.close();
        } catch (Exception err) {
            // Not the end of the world.
        }
    }
}
