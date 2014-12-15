/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.classes;

import core.common.DataTypes;
import core.common.exceptions.ColumnDefinationAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Anupam
 */
public class RecordDef {
    private Map<String, DataTypes> columnNameAndDataType = new HashMap<>();
    public void add(String columnName, DataTypes datatype) {
        columnNameAndDataType.put(columnName, datatype);
    }
    
    public boolean remove(String columnName) {
        if(columnNameAndDataType.containsKey(columnName)) {
            columnNameAndDataType.remove(columnName);
            return true;
        }
        else
            return false;
    }
    
    public boolean updateColumnName(String currentName, String newName) throws ColumnDefinationAlreadyExistsException {
        if(columnNameAndDataType.containsKey(currentName)) {
            if(columnNameAndDataType.containsKey(newName)) {
                throw new ColumnDefinationAlreadyExistsException(newName);
            }
            else {
                DataTypes dt = columnNameAndDataType.get(currentName);
                columnNameAndDataType.remove(currentName);
                columnNameAndDataType.put(newName, dt);
                return true;
            }
        }
        else
            return false;
    }
    
    public Set<String> getColumnNames() {
        return columnNameAndDataType.keySet();
    }
    
    public DataTypes getDataType(String col) {
        return columnNameAndDataType.get(col);
    }
}
