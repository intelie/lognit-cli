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
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.*;
import java.util.Timer;

public class Main {
    public static void main(String... args) throws Exception {
        //it's just because I couldn't find a faster IOC container
        System.exit(resolveEntryPoint().run(args));
    }

    public static EntryPoint resolveEntryPoint() throws IOException {
        File stateFile = new File(new File(System.getProperty("user.home"), ".lognit"), "state");

        Jsonizer jsonizer = new Jsonizer();

        UserConsole userConsole = makeUserConsole();

        RestStateStorage storage = new RestStateStorage(stateFile);

        HttpClient httpClient = new HttpClient();
        MethodFactory methodFactory = new MethodFactory();
        RestClient restClient = new RestClientImpl(httpClient, methodFactory, new BayeuxFactory(), jsonizer);

        StateKeeper stateKeeper = new StateKeeper(restClient, storage);

        FormatterSelector selector = makeFormatterSelector(jsonizer, userConsole);

        Lognit lognit = new Lognit(restClient);
        Clock clock = new Clock();
        Runtime runtime = Runtime.getRuntime();
        Timer timer = new Timer();
        MainRunner mainRunner = makeMainRunner(userConsole, lognit, clock, timer, runtime, selector);

        AuthenticatorRunner authenticatorRunner = new AuthenticatorRunner(userConsole, lognit, clock, mainRunner);

        stateKeeper.register(runtime);
        userConsole.registerFix(runtime);

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

    private static MainRunner makeMainRunner(UserConsole userConsole, Lognit lognit, Clock clock, Timer timer, Runtime runtime, FormatterSelector selector) {
        InfoRunner info = new InfoRunner(userConsole, lognit);
        SearchRunner search = new SearchRunner(userConsole, lognit, new BufferListenerFactory(selector), clock, runtime);
        CompletionRunner completion = new CompletionRunner(userConsole, lognit);
        UsageRunner usage = new UsageRunner(userConsole);
        WelcomeRunner welcome = new WelcomeRunner(userConsole, lognit);
        PurgeRunner purge = new PurgeRunner(userConsole, lognit, clock, runtime);
        PauseRunner pause = new PauseRunner(userConsole, lognit);
        DownloadRunner download = new DownloadRunner(userConsole, lognit, selector, timer);
        return new MainRunner(search, info, completion, usage, welcome, purge, pause, download);
    }
}
