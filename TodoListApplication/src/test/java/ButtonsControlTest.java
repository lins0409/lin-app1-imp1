import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ButtonsControlTest {
    @Test
    void addTest(){
        ButtonsControl bc = new ButtonsControl();
        String expected = "added";
        String actual = bc.addTask();
    }

    @Test
    void testExport(){

    }

    @Test
    void testImport(){

    }

}