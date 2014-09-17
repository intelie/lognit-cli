package net.intelie.lognit.cli;

import net.intelie.lognit.cli.http.UnsafeSSLSocketFactory;
import net.intelie.lognit.cli.runners.AuthenticatorRunner;
import net.intelie.lognit.cli.state.StateKeeper;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.OutputStreamWriter;

public class EntryPoint {

    private final UserConsole console;
    private final StateKeeper state;
    private final AuthenticatorRunner request;

    public EntryPoint(UserConsole console, StateKeeper state, AuthenticatorRunner runner) {
        this.console = console;
        this.state = state;
        this.request = runner;
    }

    public int run(String... args) {
        state.begin();

        UserOptions options = null;
        try {
            options = new UserOptions(args);
            configureLogger(options);
            if (options.isNoCheckCertificate())
                Protocol.registerProtocol("https", new Protocol("https", new UnsafeSSLSocketFactory(), 443));
            else
                Protocol.unregisterProtocol("https");

            return request.run(options);
        } catch (Exception ex) {
            console.fixCursor();
            console.println("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
            if (options != null && options.isVerbose())
                ex.printStackTrace();
            console.println("run the command with --help for usage help");
            return 3;
        } finally {
            state.end();
        }
    }

    private void configureLogger(UserOptions options) {
        Logger root = Logger.getRootLogger();
        ConsoleAppender appender = new ConsoleAppender(new PatternLayout("%r %-1p %c{1}: %m%n"));
        appender.setWriter(new OutputStreamWriter(System.err));
        root.removeAllAppenders();
        root.addAppender(appender);
        if (options.isVerbose())
            root.setLevel(Level.DEBUG);
        else
            root.setLevel(Level.OFF);
    }

}
