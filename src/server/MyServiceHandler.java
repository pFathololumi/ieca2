package server;

import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.ServiceHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by Hamed Ara on 2/19/2016.
 */
public abstract class MyServiceHandler extends ServiceHandler {


    @Override
    public void execute(PrintWriter printWriter) throws IOException {

    }

    public abstract int executeByStatus(PrintWriter printWriter) throws IOException;

    @Override
    public void handle(HttpExchange t) throws IOException {
        extractParams(t.getRequestURI().getQuery());

        StringWriter sw = new StringWriter();
        int status = executeByStatus(new PrintWriter(sw, true /*autoflush*/));
        byte[] result = sw.toString().getBytes();

        t.sendResponseHeaders(status, result.length);
        Headers headers = t.getResponseHeaders();
        headers.add("Date", Calendar.getInstance().getTime().toString());
        headers.add("Content-Type", "text/html");
        OutputStream os = t.getResponseBody();
        os.write(result);
        os.close();
    }
}
