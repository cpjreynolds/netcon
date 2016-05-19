import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.lang.Thread;

public class NetCon {
    private Options opts;

    public static void main(String argv[]) {
        Options opts = new Options(argv);

        if (opts.help()) {
            String help = NetCon.help_string();
            System.out.println(help);
            return;
        }

        NetCon application = new NetCon(opts);

        try {
            application.start();
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public NetCon(Options opts) {
        this.opts = opts;
    }

    public void start() {
        if (this.opts.listening()) {
            this.run_rx();
        } else {
            this.run_tx();
        }
    }

    // Transmitter.
    public void run_tx() {
        try(
            Socket socket = new Socket(this.opts.addr(), this.opts.port());
            OutputStream sockout = socket.getOutputStream();
        ) {
            Outgoing outgoing = new Outgoing(System.in, sockout);
            Thread out_thread = new Thread(outgoing);
            out_thread.start();
            out_thread.join();
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    // Reciever.
    public void run_rx() {
        try (
            Listener server = new Listener(
                this.opts.port(),
                0,
                this.opts.addr());
        ) {
            server.loop();
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public static String help_string() {
        return "Usage: netcon [options]\n\n"
            + "\t-p <port>\tport number\n"
            + "\t-a <addr>\taddress\n"
            + "\t-l\t\tlisten mode\n";
    }

    private static class Options {
        private int port = 8088;
        private InetAddress addr = InetAddress.getLoopbackAddress();
        private boolean listening = false;
        private boolean help = false;

        public Options(String argv[]) {
            for (int i = 0; i < argv.length; i++) {
                if (argv[i].compareTo("-p") == 0) {
                    i += 1;
                    this.port = Integer.parseInt(argv[i]);
                } else if (argv[i].compareTo("-a") == 0) {
                    i += 1;
                    try {
                        this.addr = InetAddress.getByName(argv[i]);
                    } catch (UnknownHostException err) {
                        this.addr = InetAddress.getLoopbackAddress();
                    }
                } else if (argv[i].compareTo("-l") == 0) {
                    this.listening = true;
                } else if (argv[i].compareTo("-h") == 0) {
                    this.help = true;
                }
            }
        }

        public int port() {
            return this.port;
        }

        public InetAddress addr() {
            return this.addr;
        }

        public boolean listening() {
            return this.listening;
        }

        public boolean help() {
            return this.help;
        }

        public String toString() {
            return this.addr.toString() + ":" + this.port;
        }
    }
}
