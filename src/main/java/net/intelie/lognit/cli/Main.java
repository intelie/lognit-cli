package net.intelie.lognit.cli;

import com.google.gson.Gson;
import jline.ConsoleReader;
import net.intelie.lognit.cli.http.*;
import net.intelie.lognit.cli.input.*;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.RestStateStorage;
import net.intelie.lognit.cli.state.StateKeeper;
import org.apache.commons.httpclient.HttpClient;

import java.io.*;

public class Main {
    public static int main(String... args) throws Exception {
        //it's just because I couldn't find a faster IOC container
        return resolveEntryPoint().run(args);
    }

    private static EntryPoint resolveEntryPoint() throws IOException {
        File stateFile = new File(new File(System.getProperty("user.home"), ".lognit"), "state");

        Jsonizer jsonizer = new Jsonizer(new Gson());

        ConsoleReader consoleReader = new ConsoleReader(
                new FileInputStream(FileDescriptor.in),
                new PrintWriter(System.err));

        UserConsole userConsole = new UserConsole(consoleReader, new PrintWriter(System.out));

        RestStateStorage storage = new RestStateStorage(stateFile, jsonizer);

        HttpClient httpClient = new HttpClient();
        MethodFactory methodFactory = new MethodFactory();
        RestClient restClient = new RestClientImpl(httpClient, methodFactory, new BayeuxFactory(), jsonizer);

        StateKeeper stateKeeper = new StateKeeper(restClient, storage);

        DefaultMessagePrinter defaultPrinter = new DefaultMessagePrinter(userConsole);
        ColoredMessagePrinter coloredPrinter = new ColoredMessagePrinter(userConsole);
        BufferListenerFactory bufferListenerFactory = new BufferListenerFactory(userConsole, coloredPrinter, defaultPrinter);

        Lognit lognit = new Lognit(restClient);
        RequestRunner requestRunner = new RequestRunner(userConsole, lognit, bufferListenerFactory);

        UsageRunner usageRunner = new UsageRunner(userConsole);
        return new EntryPoint(userConsole, stateKeeper, requestRunner, usageRunner);
    }
}
