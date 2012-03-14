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
    private PurgeRunner purge;

    @Before
    public void setUp() throws Exception {
        welcome = mock(WelcomeRunner.class);
        usage = mock(UsageRunner.class);
        completion = mock(CompletionRunner.class);
        info = mock(InfoRunner.class);
        search = mock(SearchRunner.class);
        purge = mock(PurgeRunner.class);
        main = new MainRunner(search, info, completion, usage, welcome, purge);
    }

    @Test
    public void usageWillBe1InPriority() throws Exception {
        UserOptions opts = new UserOptions("--purge", "-i", "-c", "-?", "abc");
        main.run(opts);
        verify(usage).run(opts);
    }

    @Test
    public void infoWillBe2InPriority() throws Exception {
        UserOptions opts = new UserOptions("--purge", "-i", "-c", "abc");
        main.run(opts);
        verify(info).run(opts);
    }

    @Test
    public void completeWillBe3InPriority() throws Exception {
        UserOptions opts = new UserOptions("--purge", "-c", "abc");
        main.run(opts);
        verify(completion).run(opts);
    }


    @Test
    public void purgeWillBe4InPriority() throws Exception {
        UserOptions opts = new UserOptions("--purge", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }

    @Test
    public void unpurgeIsSameAsPurge() throws Exception {
        UserOptions opts = new UserOptions("--unpurge", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }

    @Test
    public void cancelPurgesIsSameAsPurge() throws Exception {
        UserOptions opts = new UserOptions("--cancel-purges", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }


    @Test
    public void searchWillBeAlmostLastInPriority() throws Exception {
        UserOptions opts = new UserOptions("abc");
        main.run(opts);
        verify(search).run(opts);
    }

    @Test
    public void welcomeWillBeLastInPriority() throws Exception {
        UserOptions opts = new UserOptions();
        main.run(opts);
        verify(welcome).run(opts);
    }
}
