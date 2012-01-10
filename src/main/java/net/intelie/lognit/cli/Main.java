package net.intelie.lognit.cli;

import com.google.gson.Gson;
import net.intelie.lognit.cli.http.HttpWrapper;
import net.intelie.lognit.cli.http.MethodFactory;
import net.intelie.lognit.cli.model.Welcome;
import org.apache.commons.httpclient.HttpClient;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpWrapper http = new HttpWrapper(new HttpClient(), new MethodFactory(), new Gson());
        http.authenticate(args[0], args[1]);
        Welcome welcome = http.request("http://localhost:9006/rest/users/welcome", Welcome.class);
        System.out.println(welcome.getMessage());
    }
}
