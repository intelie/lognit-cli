package net.intelie.lognit.cli;

import jline.ConsoleReader;
import jline.UnsupportedTerminal;
import net.intelie.lognit.cli.formatters.*;
import net.intelie.lognit.cli.formatters.iem.IEMSenderFactory;
import net.intelie.lognit.cli.formatters.iem.StompClientFactory;
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
import java.util.Timer;

public class AppContext {
    public EntryPoint resolveEntryPoint() throws IOException {
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

    private UserConsole makeUserConsole() throws IOException {
        FileInputStream in = new FileInputStream(FileDescriptor.in);
        OutputStreamWriter out = new OutputStreamWriter(System.err, "UTF-8");
        final ConsoleReader consoleReader = new ConsoleReader(in, out, null, new UnsupportedTerminal());
        return new UserConsole(consoleReader, new PrintWriter(new OutputStreamWriter(System.out, "UTF-8")));
    }

    private FormatterSelector makeFormatterSelector(Jsonizer jsonizer, UserConsole userConsole) {
        PlainFormatter plainFormatter = new PlainFormatter(userConsole);
        ColoredFormatter coloredFormatter = new ColoredFormatter(userConsole);
        JsonFormatter jsonFormatter = new JsonFormatter(userConsole, jsonizer);
        FlatJsonFormatter flatJsonFormatter = new FlatJsonFormatter(userConsole, jsonizer);
        IEMSenderFactory iemFormatter = new IEMSenderFactory(coloredFormatter, jsonizer, new StompClientFactory());
        return new FormatterSelector(userConsole, coloredFormatter, plainFormatter, jsonFormatter, flatJsonFormatter, iemFormatter);
    }

    private MainRunner makeMainRunner(UserConsole userConsole, Lognit lognit, Clock clock, Timer timer, Runtime runtime, FormatterSelector selector) {
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
