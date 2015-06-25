import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FirstTest {

    @Test
    public void hello() throws Exception {
        assertThat(true, is(true));
    }
}
