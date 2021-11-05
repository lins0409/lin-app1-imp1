import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ButtonsControlTest {

    @Test
    void addTask() {
        ButtonsControl bc = new ButtonsControl();
        String name = "cry";
        String descript = "have a breakdown";
        LocalDate date;
        TaskListItems tli = new TaskListItems(name, descript, date);
    }

    @Test
    void deleteTask() {
    }
}