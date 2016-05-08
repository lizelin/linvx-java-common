package net.linvx.java.libs.http;


import net.linvx.java.libs.utils.MyStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by lizelin on 16/3/20.
 */
public class DefaultHttpResponseStreamProcessor implements HttpResponseStreamProcessor {
    @Override
    public boolean consumeInputStream(HttpResponse response, InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            int ch;
            GZIPInputStream gzip = null;
            String encoding = response.getResponseHeaderFirstValue("Content-Encoding");
            if (!MyStringUtils.isEmpty(encoding) && encoding.contains("gzip")) {
                gzip = new GZIPInputStream(is);
                while ((ch = gzip.read()) != -1) {
                    baos.write(ch);
                }
            }
            if (gzip == null) {
                while ((ch = is.read()) != -1) {
                    baos.write(ch);
                }
            }

            response.setResponseBytes(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            response.setResponseBytes(null);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
