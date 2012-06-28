package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import org.apache.commons.httpclient.Cookie;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestStateStorage {

    private final File file;

    public RestStateStorage(File file) {
        this.file = file;
    }

    public void recoverTo(RestClient client) {
        try {
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String server = reader.readLine().substring(1);
            List<Cookie> cookies = new ArrayList<Cookie>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                String domain = fields[0].trim();
                String path = fields[2].trim();
                boolean secure = "TRUE".equalsIgnoreCase(fields[3].trim());
                long expiration = Long.parseLong(fields[4].trim());
                String name = fields[5];
                String value = fields[6];
                Cookie cookie = new Cookie(domain, name, value, path, getExpiration(expiration), secure);
                cookies.add(cookie);
            }

            RestState state = new RestState(cookies.toArray(new Cookie[cookies.size()]), server);
            client.setState(state);
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
    }

    private Date getExpiration(long expiration) {
        return expiration > 0 ? new Date(expiration * 1000) : null;
    }

    public void storeFrom(RestClient client) {
        try {
            file.getParentFile().mkdirs();
            RestState state = client.getState();

            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("#" + state.getServer());

            for (Cookie cookie : state.getCookies()) {
                writer.print(cookie.getDomain() + "\t");
                writer.print("TRUE\t");
                writer.print(cookie.getPath() + "\t");
                writer.print((cookie.getSecure() ? "TRUE" : "FALSE") + "\t");
                writer.print(getExpiration(cookie) + "\t");
                writer.print(cookie.getName() + "\t");
                writer.print(cookie.getValue());
                writer.print("\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private long getExpiration(Cookie cookie) {
        Date date = cookie.getExpiryDate();
        if (date == null) return 0;
        return date.getTime() / 1000;
    }
}
