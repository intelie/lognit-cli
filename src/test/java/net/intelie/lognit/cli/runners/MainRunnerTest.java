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
    private PauseRunner pause;
    private DownloadRunner download;

    @Before
    public void setUp() throws Exception {
        welcome = mock(WelcomeRunner.class);
        usage = mock(UsageRunner.class);
        completion = mock(CompletionRunner.class);
        info = mock(InfoRunner.class);
        search = mock(SearchRunner.class);
        purge = mock(PurgeRunner.class);
        pause = mock(PauseRunner.class);
        download = mock(DownloadRunner.class);
        main = new MainRunner(search, info, completion, usage, welcome, purge, pause, download);
    }

    @Test
    public void usageWillBe1InPriority() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--purge", "-i", "-c", "-?", "abc");
        main.run(opts);
        verify(usage).run(opts);
    }

    @Test
    public void infoWillBe2InPriority() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--purge", "-i", "-c", "abc");
        main.run(opts);
        verify(info).run(opts);
    }

    @Test
    public void completeWillBe3InPriority() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--purge", "-c", "abc");
        main.run(opts);
        verify(completion).run(opts);
    }


    @Test
    public void purgeWillBe4InPriority() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--purge", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }

    @Test
    public void unpurgeIsSameAsPurge() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--unpurge", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }

    @Test
    public void cancelPurgesIsSameAsPurge() throws Exception {
        UserOptions opts = new UserOptions("--pause", "--cancel-purges", "abc");
        main.run(opts);
        verify(purge).run(opts);
    }

    @Test
    public void pauseWillBe5InPriority() throws Exception {
        UserOptions opts = new UserOptions("--pause", "abc");
        main.run(opts);
        verify(pause).run(opts);
    }

    @Test
    public void resumeWillBe5InPriority() throws Exception {
        UserOptions opts = new UserOptions("--resume", "abc");
        main.run(opts);
        verify(pause).run(opts);
    }


    @Test
    public void downloadWillBe6InPriority() throws Exception {
        UserOptions opts = new UserOptions("abc", "--download");
        main.run(opts);
        verify(download).run(opts);
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
