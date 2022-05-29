package per.itachi.java.circuitbreak.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class SentinelTest {

    @Test
    public void testEntry() {
        try(Entry entry = SphU.entry("test-sentinel")) {
            log.info("executing protected block. ");
        }
        catch (BlockException e) {
            log.error("Error occurred. ", e);
        }
    }

}
