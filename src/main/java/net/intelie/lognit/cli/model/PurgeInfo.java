package net.intelie.lognit.cli.model;

public class PurgeInfo {
    public enum Status {
        WAITING(false),
        RUNNING(false),
        CANCELLED(true),
        FAILURE(true),
        COMPLETED(true);

        public boolean isFinished() {
            return isFinished;
        }

        private final boolean isFinished;
        private Status(boolean isFinished) {

            this.isFinished = isFinished;
        }
    }

    private final Status status;
    private final String message;
    private final int purged;
    private final int failed;
    private final int expected;

    public PurgeInfo(Status status, String message, int failed, int purged, int expected) {
        this.status = status;
        this.message = message;
        this.purged = purged;
        this.failed = failed;
        this.expected = expected;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getPurged() {
        return purged;
    }

    public int getFailed() {
        return failed;
    }

    public int getExpected() {
        return expected;
    }
}
