package net.intelie.lognit.cli.http;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class EntityTest {
    @Test
    public void willExecuteOnHttpPost() {
        Entity entity = new Entity().add("A", "B").add("C", "D");
        
        PostMethod post = mock(PostMethod.class);
        entity.executeOn(post);

        verify(post).addParameter(new NameValuePair("A", "B"));
        verify(post).addParameter(new NameValuePair("C", "D"));
        verifyNoMoreInteractions(post);
    }

    @Test
    public void isImmutable() {
        Entity empty = new Entity();
        Entity entity1 = empty.add("A", "B");
        Entity entity2 = entity1.add("C", "D");

        assertThat(empty).isNotSameAs(entity1);
        assertThat(empty).isNotSameAs(entity2);
        assertThat(entity1).isNotSameAs(entity2);
    }
}
