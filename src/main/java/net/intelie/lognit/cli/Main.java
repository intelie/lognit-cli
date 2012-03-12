package net.intelie.lognit.cli;

import jline.ConsoleReader;
import net.intelie.lognit.cli.formatters.*;
import net.intelie.lognit.cli.http.BayeuxFactory;
import net.intelie.lognit.cli.http.MethodFactory;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestClientImpl;
import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.runners.*;
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

        final Jsonizer jsonizer = new Jsonizer();

        final UserConsole userConsole = makeUserConsole();

        final RestStateStorage storage = new RestStateStorage(stateFile, jsonizer);

        final HttpClient httpClient = new HttpClient();
        final MethodFactory methodFactory = new MethodFactory();
        final RestClient restClient = new RestClientImpl(httpClient, methodFactory, new BayeuxFactory(), jsonizer);

        final StateKeeper stateKeeper = new StateKeeper(restClient, storage);

        final FormatterSelector selector = makeFormatterSelector(jsonizer, userConsole);

        final BufferListenerFactory bufferListenerFactory = new BufferListenerFactory(selector);

        final Lognit lognit = new Lognit(restClient);
        final Clock clock = new Clock();
        final MainRunner mainRunner = makeMainRunner(userConsole, bufferListenerFactory, lognit, clock);

        final AuthenticatorRunner authenticatorRunner = new AuthenticatorRunner(userConsole, lognit, mainRunner);


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                stateKeeper.end();
            }
        }));

        return new EntryPoint(userConsole, stateKeeper, authenticatorRunner);
    }

    private static UserConsole makeUserConsole() throws IOException {
        final ConsoleReader consoleReader = new ConsoleReader(
                new FileInputStream(FileDescriptor.in),
                new PrintWriter(System.err));

        return new UserConsole(consoleReader, new PrintWriter(System.out));
    }

    private static FormatterSelector makeFormatterSelector(Jsonizer jsonizer, UserConsole userConsole) {
        final PlainFormatter plainFormatter = new PlainFormatter(userConsole);
        final ColoredFormatter coloredFormatter = new ColoredFormatter(userConsole);
        final JsonFormatter jsonFormatter = new JsonFormatter(userConsole, jsonizer);
        final FlatJsonFormatter flatJsonFormatter = new FlatJsonFormatter(userConsole, jsonizer);
        return new FormatterSelector(userConsole, coloredFormatter, plainFormatter, jsonFormatter, flatJsonFormatter);
    }

    private static MainRunner makeMainRunner(UserConsole userConsole, BufferListenerFactory bufferListenerFactory, Lognit lognit, Clock clock) {
        final InfoRunner info = new InfoRunner(userConsole, lognit);
        final SearchRunner search = new SearchRunner(userConsole, lognit, bufferListenerFactory, clock);
        final CompletionRunner completion = new CompletionRunner(userConsole, lognit);
        final UsageRunner usage = new UsageRunner(userConsole);
        final WelcomeRunner welcome = new WelcomeRunner(userConsole, lognit);
        return new MainRunner(search, info, completion, usage, welcome);
    }
}
