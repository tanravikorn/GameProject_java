import logic.candy.Candy;
import logic.candy.CandyColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CandyTest {
    Candy candy;
    @BeforeEach
    void setup(){
        candy = new Candy(2,3, CandyColor.BLUE);
    }
    @Test
    void candyConstructureTest(){

    }
}
