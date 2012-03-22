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
    public void willExecuteOnHttpPostWithToString() {
        Entity entity = new Entity().add("A", 1).add("C", true);

        PostMethod post = mock(PostMethod.class);
        entity.executeOn(post);

        verify(post).addParameter(new NameValuePair("A", "1"));
        verify(post).addParameter(new NameValuePair("C", "true"));
        verifyNoMoreInteractions(post);
    }

    @Test
    public void willExecuteOnHttpPostWithNull() {
        Entity entity = new Entity().add("A", null).add("C", null);

        PostMethod post = mock(PostMethod.class);
        entity.executeOn(post);

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

    @Test
    public void whenAreEqual() {
        Entity entity1 = new Entity().add("A", "B");
        Entity entity2 = new Entity().add("A", "B");

        assertThat(entity1).isEqualTo(entity1);
        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity1.hashCode());
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    public void whenAreDifferent() {
        Entity entity1 = new Entity().add("A", "B");
        Entity entity2 = new Entity().add("A", "C");
        Entity entity3 = new Entity().add("B", "B");

        assertThat(entity1).isNotEqualTo(entity2);
        assertThat(entity1).isNotEqualTo(entity3);
        assertThat(entity1).isNotEqualTo(new Object());

        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
        assertThat(entity1.hashCode()).isNotEqualTo(entity3.hashCode());
        assertThat(entity1.hashCode()).isNotEqualTo(new Object().hashCode());
    }
}
