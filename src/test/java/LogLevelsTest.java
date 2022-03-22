import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogLevelsTest {
    private static final Logger logger = LoggerFactory.getLogger(LogLevelsTest.class);
//    private static final Logger secretLogger = LoggerFactory.getLogger("com.stubbornjava.secrets.MySecretPasswordClass");

    public static void main(String args[]) {
        logger.trace("TRACE");
        logger.info("INFO");
        logger.debug("DEBUG");
        logger.warn("WARN");
        logger.error("ERROR");

//        secretLogger.trace("TRACE");
//        secretLogger.info("INFO");
//        secretLogger.debug("DEBUG");
//        secretLogger.warn("WARN");
//        secretLogger.error("ERROR");
    }
}
