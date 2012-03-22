package net.intelie.lognit.cli.http;

import com.google.common.base.Objects;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class Entity {
    private NameValuePair current;
    private Entity previous;

    public Entity() {
        this(null, null);
    }

    private Entity(Entity previous, NameValuePair current) {
        this.current = current;
        this.previous = previous;
    }

    public Entity add(String name, Object value) {
        if (value == null)
            return this;
        return new Entity(this, new NameValuePair(name, value.toString()));
    }

    public void executeOn(PostMethod post) {
        if (current != null)
            post.addParameter(current);
        if (previous != null)
            previous.executeOn(post);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity that = (Entity) o;

        return Objects.equal(this.current, that.current) &&
                Objects.equal(this.previous, that.previous);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(current, previous);
    }
}
