import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.Runnable;

public class Outgoing implements Runnable {
    private InputStream input;
    private OutputStream output;

    public Outgoing(InputStream in, OutputStream out) {
        this.input = in;
        this.output = out;
    }

    public void run() {
        byte[] buf = new byte[4096];

        try {
            int nread = 0;
            while ((nread = this.input.read(buf)) > 0) {
                output.write(buf, 0, nread);
            }
        } catch (IOException err) {
            System.out.println(err);
        }
    }
}
