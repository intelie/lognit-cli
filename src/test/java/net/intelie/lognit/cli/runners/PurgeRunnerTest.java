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
        when(lognit.purge("abc", 42)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe")).thenReturn(
                new PurgeInfo(PurgeInfo.Status.RUNNING, "aaa", 1, 2, 3),
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3)
        );
        when(lognit.getServer()).thenReturn("server");
        
        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        InOrder orderly = inOrder(console, lognit, clock, runtime);
        orderly.verify(lognit).purge("abc", 42);
        orderly.verify(console).println(PurgeRunner.PURGE_ID, "server", "qwe");

        orderly.verify(runtime).addShutdownHook(any(Thread.class));

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe");
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.RUNNING, 2, 3, 200.0 / 3, 1);

        orderly.verify(clock).sleep(1000);
        orderly.verify(lognit).purgeInfo("qwe");
        orderly.verify(console).printStill(PurgeRunner.STATUS, PurgeInfo.Status.CANCELLED, 3, 3, 100.0, 1);

        orderly.verify(console).println("");

        orderly.verifyNoMoreInteractions();
    }

    @Test
    public void willGetUnpurgeIfItsNotPurge() throws Exception {
        when(lognit.unpurge()).thenReturn(new Purge("qwe"));
        when(lognit.getServer()).thenReturn("server");
        when(lognit.purgeInfo("qwe")).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--unpurge"))).isZero();

        verify(lognit).unpurge();
    }

    @Test
    public void willRegisterHookThatCancelsPurge() throws Exception {
        when(lognit.purge("abc", 42)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe")).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));

        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
        verify(runtime).addShutdownHook(captor.capture());

        captor.getValue().run();
        verify(lognit).cancelPurge("qwe");

    }

    @Test
    public void willRegisterHookThatCancelsPurgeEvenIfHookFails() throws Exception {
        when(lognit.purge("abc", 42)).thenReturn(new Purge("qwe"));
        when(lognit.purgeInfo("qwe")).thenReturn(
                new PurgeInfo(PurgeInfo.Status.CANCELLED, "bbb", 1, 3, 3));
        doThrow(new RuntimeException()).when(lognit).cancelPurge("qwe");
        
        assertThat(runner.run(new UserOptions("abc", "-n", "42", "--purge"))).isZero();

        ArgumentCaptor<Thread> captor = ArgumentCaptor.forClass(Thread.class);
        verify(runtime).addShutdownHook(captor.capture());

        captor.getValue().run();
        verify(lognit).cancelPurge("qwe");
    }
}
