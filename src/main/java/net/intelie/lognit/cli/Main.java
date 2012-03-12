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
        File stateFile = new File(new File(System.getProperty("user.home"), ".lognit"), "state");

        Jsonizer jsonizer = new Jsonizer();

        UserConsole userConsole = makeUserConsole();

        RestStateStorage storage = new RestStateStorage(stateFile, jsonizer);

        HttpClient httpClient = new HttpClient();
        MethodFactory methodFactory = new MethodFactory();
        RestClient restClient = new RestClientImpl(httpClient, methodFactory, new BayeuxFactory(), jsonizer);

        StateKeeper stateKeeper = new StateKeeper(restClient, storage);

        FormatterSelector selector = makeFormatterSelector(jsonizer, userConsole);

        BufferListenerFactory bufferListenerFactory = new BufferListenerFactory(selector);

        Lognit lognit = new Lognit(restClient);
        Clock clock = new Clock();
        MainRunner mainRunner = makeMainRunner(userConsole, bufferListenerFactory, lognit, clock);

        AuthenticatorRunner authenticatorRunner = new AuthenticatorRunner(userConsole, lognit, mainRunner);

        stateKeeper.register(Runtime.getRuntime());

        return new EntryPoint(userConsole, stateKeeper, authenticatorRunner);
    }

    private static UserConsole makeUserConsole() throws IOException {
        final ConsoleReader consoleReader = new ConsoleReader(
                new FileInputStream(FileDescriptor.in),
                new PrintWriter(System.err));

        return new UserConsole(consoleReader, new PrintWriter(System.out));
    }

    private static FormatterSelector makeFormatterSelector(Jsonizer jsonizer, UserConsole userConsole) {
        PlainFormatter plainFormatter = new PlainFormatter(userConsole);
        ColoredFormatter coloredFormatter = new ColoredFormatter(userConsole);
        JsonFormatter jsonFormatter = new JsonFormatter(userConsole, jsonizer);
        FlatJsonFormatter flatJsonFormatter = new FlatJsonFormatter(userConsole, jsonizer);
        return new FormatterSelector(userConsole, coloredFormatter, plainFormatter, jsonFormatter, flatJsonFormatter);
    }

    private static MainRunner makeMainRunner(UserConsole userConsole, BufferListenerFactory bufferListenerFactory, Lognit lognit, Clock clock) {
        InfoRunner info = new InfoRunner(userConsole, lognit);
        SearchRunner search = new SearchRunner(userConsole, lognit, bufferListenerFactory, clock);
        CompletionRunner completion = new CompletionRunner(userConsole, lognit);
        UsageRunner usage = new UsageRunner(userConsole);
        WelcomeRunner welcome = new WelcomeRunner(userConsole, lognit);
        PurgeRunner purge = new PurgeRunner();
        return new MainRunner(search, info, completion, usage, welcome, purge);
    }
}
