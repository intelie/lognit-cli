package net.intelie.lognit.cli.runners;

import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.UserOptions;
import net.intelie.lognit.cli.model.Lognit;
import net.intelie.lognit.cli.model.Purge;
import net.intelie.lognit.cli.model.PurgeInfo;
import net.intelie.lognit.cli.state.Clock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PurgeRunnerTest {

    private UserConsole console;
    private Lognit lognit;
    private Clock clock;
    private Runtime runtime;
    private PurgeRunner runner;

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        lognit = mock(Lognit.class);
        clock = mock(Clock.class);
        runtime = mock(Runtime.class);
        runner = new PurgeRunner(console, lognit, clock, runtime);
    }

    @Test
    public void willRunUntilItsFinished() throws Exception {
        when(lognit.purge("abc", 42, false)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe", false)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.RUNNING, "aaa", 1, 2, 3),
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3)
        );
        when(lognit.getServer()).thenReturn("server");
        
        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        InOrder orderly = inOrder(console, lognit, clock, runtime);
        orderly.verify(lognit).purge("abc", 42, false);
        orderly.verify(console).println(PurgeRunner.PURGE_ID, "server", "qwe");

        orderly.verify(runtime).addShutdownHook(any(Thread.class));

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", false);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.RUNNING, 2, 3, 200.0 / 3, 1, 1);

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", false);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.CANCELLED, 3, 3, 100.0, 1, 0);

        orderly.verify(console).println("");

        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willRunUntilItsFinishedToAllCluster() throws Exception {
        when(lognit.purge("abc", 42, true)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe", true)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.RUNNING, "aaa", 1, 2, 3),
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3)
        );
        when(lognit.getServer()).thenReturn("server");

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge", "--all"))).isZero();

        InOrder orderly = inOrder(console, lognit, clock, runtime);
        orderly.verify(lognit).purge("abc", 42, true);
        orderly.verify(console).println(PurgeRunner.PURGE_ID, "server", "qwe");

        orderly.verify(runtime).addShutdownHook(any(Thread.class));

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", true);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.RUNNING, 2, 3, 200.0 / 3, 1, 1);

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", true);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.CANCELLED, 3, 3, 100.0, 1, 0);

        orderly.verify(console).println("");

        orderly.verifyNoMoreInteractions();
    }


    @Test
    public void willCalculateETA() throws Exception {
        when(lognit.purge("abc", 42, false)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe", false)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.RUNNING, "aaa", 0, 10, 100),
                new PurgeInfo(PurgeInfo.Status.RUNNING, "aaa", 0, 50, 100),
                new PurgeInfo(PurgeInfo.Status.COMPLETED, "bbb", 0, 100, 100)
        );
        when(lognit.getServer()).thenReturn("server");

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        InOrder orderly = inOrder(console, lognit, clock, runtime);
        orderly.verify(lognit).purge("abc", 42, false);
        orderly.verify(console).println(PurgeRunner.PURGE_ID, "server", "qwe");

        orderly.verify(runtime).addShutdownHook(any(Thread.class));

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", false);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.RUNNING, 10, 100, 10.0, 0, 9);

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", false);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.RUNNING, 50, 100, 50.0, 0, 2);

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe", false);
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.COMPLETED, 100, 100, 100.0, 0, 0);

        orderly.verify(console).println("");

        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willGetUnpurgeIfItsNotPurge() throws Exception {
        when(lognit.unpurge(false)).thenReturn(new Purge("qwe"));
        when(lognit.getServer()).thenReturn("server");
        when(lognit.purgeInfo("qwe", false)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--unpurge"))).isZero();

        verify(lognit).unpurge(false);
    }

    @Test
    public void cancelAllHasHighestPriority() throws Exception {
        assertThat(runner.run(new UserOptions("--purge", "--unpurge", "--cancel-purges"))).isZero();

        verify(lognit).cancelAllPurges(false);
    }

    @Test
    public void willRegisterHookThatCancelsPurge() throws Exception {
        when(lognit.purge("abc", 42, false)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe", false)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
        verify(runtime).addShutdownHook(captor.capture());

        captor.getValue().run();
        verify(lognit).cancelPurge("qwe", false);

    }

    @Test
    public void willRegisterHookThatCancelsPurgeEvenIfHookFails() throws Exception {
        when(lognit.purge("abc", 42, false)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe", false)).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));
        doThrow(new RuntimeException()).when(lognit).cancelPurge("qwe", false);
        
        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
        verify(runtime).addShutdownHook(captor.capture());

        captor.getValue().run();
        verify(lognit).cancelPurge("qwe", false);
    }
}
