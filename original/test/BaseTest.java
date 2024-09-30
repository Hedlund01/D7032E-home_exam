import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    PointSalad pointSalad;

    @BeforeEach
    public void setUp() {
        pointSalad = new PointSalad(new String[]{"1", "1"});
    }
}
