/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.classes;

import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.TableNotFoundException;
import core.interfaces.Table;
import core.interfaces.TableManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anupam
 */
public class InMemoryTableManager implements TableManager {
    
    Map<String,Table> inMemTable = new HashMap<>();
        
    @Override
    public boolean createTable(String tablename, Map<String, DataTypes> schema) {
        boolean flag = false;
        Table tbl = new InMemoryTable();
        try {
            flag = tbl.create(tablename, "in-memory.manager", schema);
        } catch (DataTypeEnumMappingException ex) {
            Logger.getLogger(InMemoryTableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        inMemTable.put(tablename, tbl);
        return flag;
    }

    @Override
    public Set<String> ListTables() {
        return inMemTable.keySet();
    }
    
    @Override
    public Table getTableReference(String tablename) throws TableNotFoundException {
        if(inMemTable.containsKey(tablename))
            return inMemTable.get(tablename);
        else
            throw new TableNotFoundException();
    }

    @Override
    public boolean dropTable(String tablename) throws TableNotFoundException {
        if(inMemTable.containsKey(tablename)) {
            inMemTable.remove(tablename);
            return true;
        }
        else 
            return false;
    }

    @Override
    public RecordDef getTableSchema(String tablename) throws TableNotFoundException {
        if(inMemTable.containsKey(tablename))
            return inMemTable.get(tablename).getSchema();
        else
            throw new TableNotFoundException();
    }    
}
