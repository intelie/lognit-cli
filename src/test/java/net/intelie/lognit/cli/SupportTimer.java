package net.intelie.lognit.cli;

import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import static junit.framework.Assert.*;

public class SupportTimer extends Timer {
    private static class SupportTask implements Runnable, Comparable<SupportTask> {
        private final TimerTask task;
        private final Long next;
        private final Long period;

        public SupportTask(TimerTask task, Long next) {
            this(task, next, null);
        }

        public SupportTask(TimerTask task, Long next, Long period) {
            this.task = task;
            this.next = next;
            this.period = period;
        }

        @Override
        public int compareTo(SupportTask that) {
            return this.next.compareTo(that.next);
        }

        public void scheduleNextIfApplicable(Queue<SupportTask> queue) {
            if (period != null) {
                queue.add(new SupportTask(task, next + period, period));
            }
        }

        public boolean isCancelled() {
            //damn hack
            try {
                Field field = TimerTask.class.getDeclaredField("state");
                field.setAccessible(true);
                return field.get(task).equals(3);
            } catch (Exception e) {
                fail("Great bug: " + e);
                return true;
            }
        }

        @Override
        public void run() {
            task.run();
        }

        public TimerTask assertSchedule(Long delay) {
            return assertSchedule(delay, null);
        }

        public TimerTask assertSchedule(Long delay, Long period) {
            assertEquals(delay, this.next);
            assertEquals(period, this.period);
            assertFalse(isCancelled());
            return this.task;
        }
    }

    private long time = 0;
    private Queue<SupportTask> tasks = new PriorityQueue<SupportTask>();

    @Override
    public void schedule(TimerTask task, long delay) {
        tasks.add(new SupportTask(task, time+delay));
    }

    @Override
    public void schedule(TimerTask task, long delay, long period) {
        tasks.add(new SupportTask(task, time+delay, period));
    }

    public long getTime() {
        return time;
    }

    private void cleanUp() {
        while (!tasks.isEmpty() && tasks.peek().isCancelled())
            tasks.poll();
    }

    private SupportTask prepareToRun() {
        assertMoreTasks();
        SupportTask task = tasks.poll();
        advanceClockTo(task.next);
        task.scheduleNextIfApplicable(tasks);
        return task;
    }

    public void advanceClockTo(long time) {
        while (thereAreLateTasks(time))
            runNext();

        this.time = time;
    }

    private boolean thereAreLateTasks(long time) {
        cleanUp();
        return !tasks.isEmpty() && tasks.peek().next < time;
    }

    public void runNext() {
        prepareToRun().run();
    }

    public void runNextAt(long expectedTime) {
        SupportTask task = prepareToRun();
        assertEquals(expectedTime, this.time);
        task.run();
    }

    public TimerTask assertSchedule(Long delay) {
        assertMoreTasks();
        return tasks.peek().assertSchedule(delay);
    }

    public TimerTask assertSchedule(Long delay, Long period) {
        assertMoreTasks();
        return tasks.peek().assertSchedule(delay, period);
    }

    public void assertMoreTasks() {
        cleanUp();
        assertFalse("Should have more tasks scheduled", tasks.isEmpty());
    }

    public void assertNoMoreTasks() {
        cleanUp();
        assertTrue("Should have no more tasks scheduled", tasks.isEmpty());
    }


}
