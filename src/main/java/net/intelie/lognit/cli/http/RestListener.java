package net.intelie.lognit.cli.http;

public interface RestListener<T> {
    void receive(T message);
}
