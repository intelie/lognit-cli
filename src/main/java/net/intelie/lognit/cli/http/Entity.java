package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class Entity {
    public Entity add(String name, String value) {
        return new NameValueEntity(new NameValuePair(name, value));
    }

    public void executeOn(PostMethod post) {
    }

    private class NameValueEntity extends Entity {
        private final NameValuePair pair;

        public NameValueEntity(NameValuePair pair) {
            this.pair = pair;
        }

        @Override
        public void executeOn(PostMethod post) {
            post.addParameter(pair);
            Entity.this.executeOn(post);
        }
    }
}
