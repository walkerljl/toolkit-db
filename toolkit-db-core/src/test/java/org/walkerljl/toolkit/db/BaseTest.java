package org.walkerljl.toolkit.db;

import org.junit.Test;
import org.walkerljl.toolkit.logging.Logger;
import org.walkerljl.toolkit.logging.LoggerFactory;

/**
 *
 * BaseTest
 *
 * @author lijunlin
 */
public class BaseTest {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Test
	public void run() {
		try {
			doTest();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void doTest() {}
}
