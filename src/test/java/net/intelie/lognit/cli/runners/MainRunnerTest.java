package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserOptions;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainRunnerTest {

    private WelcomeRunner welcome;
    private UsageRunner usage;
    private CompletionRunner completion;
    private InfoRunner info;
    private SearchRunner search;
    private MainRunner main;

    @Before
    public void setUp() throws Exception {
        welcome = mock(WelcomeRunner.class);
        usage = mock(UsageRunner.class);
        completion = mock(CompletionRunner.class);
        info = mock(InfoRunner.class);
        search = mock(SearchRunner.class);
        main = new MainRunner(search, info, completion, usage, welcome);
    }

    @Test
    public void usageWillBe1InPriority() throws Exception {
        UserOptions opts = new UserOptions("-i", "-c", "-?", "abc");
        main.run(opts);
        verify(usage).run(opts);
    }

    @Test
    public void infoWillBe2InPriority() throws Exception {
        UserOptions opts = new UserOptions("-i", "-c", "abc");
        main.run(opts);
        verify(info).run(opts);
    }

    @Test
    public void completeWillBe3InPriority() throws Exception {
        UserOptions opts = new UserOptions("-c", "abc");
        main.run(opts);
        verify(completion).run(opts);
    }

    @Test
    public void searchWillBe4InPriority() throws Exception {
        UserOptions opts = new UserOptions("abc");
        main.run(opts);
        verify(search).run(opts);
    }

    @Test
    public void welcomeWillBe5InPriority() throws Exception {
        UserOptions opts = new UserOptions();
        main.run(opts);
        verify(welcome).run(opts);
    }
}
