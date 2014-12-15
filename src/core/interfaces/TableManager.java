/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.interfaces;

import core.classes.RecordDef;
import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.TableNotFoundException;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author anupam
 */
public interface TableManager {
    public boolean createTable(String tablename,Map<String, DataTypes> schema);
    public Set<String> ListTables();
    public boolean dropTable(String tablename) throws TableNotFoundException;
    public RecordDef getTableSchema(String table) throws TableNotFoundException;
    public Table getTableReference(String tablename) throws TableNotFoundException;
}
