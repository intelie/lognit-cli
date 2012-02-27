package net.intelie.lognit.cli;

import com.google.gson.Gson;
import jline.ConsoleReader;
import net.intelie.lognit.cli.formatters.ColoredFormatter;
import net.intelie.lognit.cli.formatters.PlainFormatter;
import net.intelie.lognit.cli.http.*;
import net.intelie.lognit.cli.input.*;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.state.Clock;
import net.intelie.lognit.cli.state.RestStateStorage;
import net.intelie.lognit.cli.state.StateKeeper;
import org.apache.commons.httpclient.HttpClient;

import java.io.*;

public class Main {
    public static void main(String... args) throws Exception {
        //it's just because I couldn't find a faster IOC container
        System.exit(resolveEntryPoint().run(args));
    }

    public static EntryPoint resolveEntryPoint() throws IOException {
        final File stateFile = new File(new File(System.getProperty("user.home"), ".lognit"), "state");

        final Jsonizer jsonizer = new Jsonizer(new Gson());

        final ConsoleReader consoleReader = new ConsoleReader(
                new FileInputStream(FileDescriptor.in),
                new PrintWriter(System.err));

        final UserConsole userConsole = new UserConsole(consoleReader, new PrintWriter(System.out));

        final RestStateStorage storage = new RestStateStorage(stateFile, jsonizer);

        final HttpClient httpClient = new HttpClient();
        final MethodFactory methodFactory = new MethodFactory();
        final RestClient restClient = new RestClientImpl(httpClient, methodFactory, new BayeuxFactory(), jsonizer);

        final StateKeeper stateKeeper = new StateKeeper(restClient, storage);

        final PlainFormatter plainFormatter = new PlainFormatter(userConsole);
        final ColoredFormatter coloredFormatter = new ColoredFormatter(userConsole);
        final BufferListenerFactory bufferListenerFactory = new BufferListenerFactory(userConsole, coloredFormatter, plainFormatter);

        final Lognit lognit = new Lognit(restClient);
        final Clock clock = new Clock();
        final InfoRunner runner = new InfoRunner(userConsole);
        final RequestRunner requestRunner = new RequestRunner(userConsole, lognit, runner, bufferListenerFactory, clock);

        final UsageRunner usageRunner = new UsageRunner(userConsole);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stateKeeper.end();
            }
        }));

        return new EntryPoint(userConsole, stateKeeper, requestRunner, usageRunner);
    }
}
