package ferry.booking;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProgramTest {

    private Program program;
    public static final Logger logger = Logger.getLogger(ProgramTest.class.getName());

    @BeforeEach
    public void setUp() {
        program = new Program();
    }

    @Test
    public void testDoCommand() {
        program.doCommand("list ports");
        String expectedOutput = "Ports:\n------\n"; // Adjust this according to the expected output
        logger.info(expectedOutput);
    }

    @Test
    public void testBook() {
        program.book("book 1 1");
        String expectedOutput = "Booked\n";
        logger.info(expectedOutput);
    }

    @Test
    public void testSearch() {
        program.search("search 1 2 00:00");
        String expectedOutput = "";
        logger.info(expectedOutput);
    }
}