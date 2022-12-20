package entity;


import java.util.ArrayList;
import java.util.List;

public class Field {
    private List<FieldCell> fieldCells = new ArrayList<>();

    public List<FieldCell> getFieldCells(){
        return fieldCells;
    }
}
