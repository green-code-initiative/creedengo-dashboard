package ut.org.greencodeinitiative.creedengo.confluence.plugin;

import org.junit.Test;
import org.greencodeinitiative.creedengo.confluence.plugin.api.MyPluginComponent;
import org.greencodeinitiative.creedengo.confluence.plugin.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}