import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GameClient {

    public void connect(){
        try{
            Socket srv = new Socket("localhost", 12345);
            InputStream is = srv.getInputStream();
            OutputStream os = srv.getOutputStream();

            int b = 22;
            os.write(b);       // send a byte to the server
            b = is.read();     // read a byte sent by the server

            System.out.println(b);

            srv.close();
            is.close();
            os.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
