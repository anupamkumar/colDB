/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.classes;

import core.common.BinSearchKeyList;
import core.common.exceptions.KeyNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Anupam
 * @param <T>
 */
public class Column<T> {
    private Map<Long, T> keyvalue;
    private Map<T, Set<Long>> valuekey;
    private String colName;
    private String tableName;
    
    public Column(String colName, String tableName) {
        this.colName = colName;
        this.tableName = tableName;
        keyvalue = new HashMap<>();
        valuekey = new TreeMap<>();
        
    }
    
    public void insert(long key, T value) {
        keyvalue.put(key, value);
        Set<Long> t;
        if(valuekey.containsKey(value)) {
            t = valuekey.get(value);
            t.add(key);
        }
        else {
            t = new TreeSet<>();
            t.add(key);            
        }        
        valuekey.put(value, t);
    }
    
    public void delete(long key) throws KeyNotFoundException {
        if(keyvalue.containsKey(key)) {
            T s = keyvalue.get(key);        
            keyvalue.remove(key);
            if(valuekey.containsKey(s)) {
                Set<Long> keyList = valuekey.get(s);
                BinSearchKeyList so = new BinSearchKeyList(keyList);
                keyList.remove(so.getIndexForKey(key));
                valuekey.put(s, keyList);
            }
        }
        else {
            throw new KeyNotFoundException();
        }
    }
    
    public void update(long key,T newValue) throws KeyNotFoundException {
        delete(key);
        insert(key,newValue);
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public String getColumnName() {
        return colName;
    }
    
    public Set<Long> getRowKeysForValue(T value) throws KeyNotFoundException {
        if(valuekey.containsKey(value)) {
            return valuekey.get(value);
        }
        else {
            throw new KeyNotFoundException();
        }
    }
    
    public T getValue(long key) {
        return keyvalue.get(key);
    }
    
    public Map<T,Set<Long>> getValueKeyList() {
        return valuekey;
    } 
    
    public Set<Long> getKeyList() {
        return keyvalue.keySet();
    }
}
