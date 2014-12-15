/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.classes;

import core.common.Comparator;
import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.InvalidComparisionException;
import core.common.exceptions.InvalidFilterSpecificationException;
import core.common.exceptions.KeyNotFoundException;
import core.interfaces.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Anupam
 */
public class InMemoryTable implements Table {    
    RecordDef tableRecordDef=new RecordDef();
    String name;
    long createTime;
    long lastModifyTime;
    String owner;
    ArrayList<Column> rowDef = new ArrayList<>();
    Map<String, Long> autoNumbers = new HashMap<>();
    
    @Override
    public RecordDef getSchema() {
        return tableRecordDef;
    }
    
    @Override
    public boolean insert(HashMap<String, String> colNamesAndTheirValues) throws DataTypeEnumMappingException {
        long rowkey = System.nanoTime();
        for(String colnames : colNamesAndTheirValues.keySet()) {
            int colIndex = indexOfColumn(colnames);
            if(colIndex==-1)
                return false;
            DataTypes dt = tableRecordDef.getDataType(colnames);
            switch(dt) {
                case NUMBER:
                    Column<Double> t = rowDef.get(colIndex);
                    t.insert(rowkey, Double.parseDouble(colNamesAndTheirValues.get(colnames)));
                    break;
                case TEXT:
                case JSON:
                case XML:
                    Column<String> tt = rowDef.get(colIndex);
                    tt.insert(rowkey, colNamesAndTheirValues.get(colnames));
                    break;
                case DATETIME:
                    Column<Date> ttd = rowDef.get(colIndex);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                ttd.insert(rowkey, sdf.parse(colNamesAndTheirValues.get(colnames)));
            } catch (ParseException ex) {
                Logger.getLogger(InMemoryTable.class.getName()).log(Level.SEVERE, null, ex);
            }
                    break;
                default:
                    throw new DataTypeEnumMappingException();
            }
        }
        return true;
    }

    @Override
    public boolean delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean create(String n,String o, Map<String, DataTypes> defination) throws DataTypeEnumMappingException {  
        name = n;
        owner = o;
        for(String cols : defination.keySet()) {
            switch(defination.get(cols)) {
                case NUMBER:
                    Column<Double> c = new Column<>(cols,name);   
                    rowDef.add(c);
                    tableRecordDef.add(cols, DataTypes.NUMBER);
                    break;
                case TEXT:
                case XML:
                case JSON:
                    Column<String> c2 = new Column<>(cols,name);
                    rowDef.add(c2);
                    tableRecordDef.add(cols, DataTypes.TEXT);
                    break;
                case DATETIME:
                    Column<Date> c3 = new Column<>(cols,name);
                    rowDef.add(c3);
                    tableRecordDef.add(cols, DataTypes.DATETIME);
                    break;
                case BINARY:
                    Column<Byte> c4 = new Column<>(cols,name);
                    rowDef.add(c4);
                    tableRecordDef.add(cols, DataTypes.BINARY);
                    break;
                case BOOLEAN:
                    Column<Boolean> c5 = new Column<>(cols,name);
                    rowDef.add(c5);
                    tableRecordDef.add(cols, DataTypes.BOOLEAN);
                    break; 
                default:
                    throw new DataTypeEnumMappingException();
            }            
        }
        return true;
    }

    @Override
    public boolean drop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Set<Long> filterAndGroup(ArrayList<String> colNames, ArrayList<String> colValues, ArrayList<Comparator> comp) throws InvalidFilterSpecificationException, KeyNotFoundException, InvalidComparisionException,ParseException {
        if(colNames.size() != colValues.size() || colNames.size() != comp.size()) {
            throw new InvalidFilterSpecificationException();
        }
        Set<Long> keyList = new TreeSet<>();
        for(int i=0;i<colNames.size();i++) {
           DataTypes dt = tableRecordDef.getDataType(colNames.get(i));
           switch(dt) {
               case NUMBER:
                   Column<Double> cd = rowDef.get(indexOfColumn(colNames.get(i)));
                   Filter filter = new Filter(cd, Double.parseDouble(colValues.get(i)), comp.get(i));
                   keyList.addAll(filter.getFilteredKeyList());
                   break;
               case TEXT:
               case JSON:
               case XML:
                   Column<String> cs = rowDef.get(indexOfColumn(colNames.get(i)));
                   Filter filter2 = new Filter(cs,colValues.get(i), comp.get(i));
                   keyList.addAll(filter2.getFilteredKeyList());
                   break;
               case DATETIME:
                   Column<Date> cdt = rowDef.get(indexOfColumn(colNames.get(i)));
                   SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   
                   Filter filter3 = new Filter(cdt, dtf.parse(colValues.get(i)), comp.get(i));
                   keyList.addAll(filter3.getFilteredKeyList());
                   break;
               case BINARY:
//                   Column<Byte> cb = rowDef.get(indexOfColumn(name));
//                   Filter filter4 = new Filter(cb, Byte.parseByte(colValues.get(i)), comp.get(i));
//                   keyList.addAll(filter4.getFilteredKeyList());
                   break;
               case BOOLEAN:
                   Column<Boolean> cbo = rowDef.get(indexOfColumn(colNames.get(i)));
                   Filter filter5 = new Filter(cbo, Boolean.parseBoolean(colValues.get(i)), comp.get(i));
                   keyList.addAll(filter5.getFilteredKeyList());
                   break;
           }
        }
        //for testing/debugging what values are associated with the key. Delete this later.
//        ArrayList<String> rows = new ArrayList<>();
//        for(long key : keyList) {
//            StringBuilder row = new StringBuilder();
//            for(String col : tableRecordDef.getColumnNames()) {
//                row.append(rowDef.get(indexOfColumn(col)).getValue(key)).append(",");
//            }
//            rows.add(row.substring(0, row.length()-1));
//        }
        return keyList;
    }    
    
    private int indexOfColumn(String colname) {
        for(int i=0;i<rowDef.size();i++) {
            if(colname.equals(rowDef.get(i).getColumnName()))
                return i;                    
        }
        return -1;
    }

    

    @Override
    public ArrayList<String> select(ArrayList<FilterSpec> OrArrayOfAndFilters, ArrayList<String> selectColNames) throws InvalidFilterSpecificationException, KeyNotFoundException, InvalidComparisionException, ParseException {
        Set<Long> keyList = new HashSet<>();
        for(FilterSpec andFilter : OrArrayOfAndFilters) {
            keyList.addAll(filterAndGroup(andFilter.colnames, andFilter.colValues, andFilter.comp));
        }
        ArrayList<String> rows = new ArrayList<>();
        for(long key : keyList) {
            StringBuilder sb = new StringBuilder();
            for(String col : selectColNames) {
                sb.append(rowDef.get(indexOfColumn(col)).getValue(key)).append(",");
            }
            rows.add(sb.substring(0, sb.length()-1));
        }
        return rows;
    }
    
    @Override
    public ArrayList<String> select(ArrayList<String> selectColNames) throws 
    KeyNotFoundException, ParseException {
        Set<Long> keyList = rowDef.get(0).getKeyList();
        ArrayList<String> rows = new ArrayList<>();
        for(long key : keyList) {
            StringBuilder sb = new StringBuilder();
            for(String col : selectColNames) {
                String colVal = rowDef.get(indexOfColumn(col)).getValue(key).toString();
                if(colVal.equalsIgnoreCase("NaN") || colVal.equalsIgnoreCase("") || colVal.equalsIgnoreCase("Thu Jan 01 00:00:00 PST 1"))
                    colVal = "NULL";
                sb.append(colVal).append(",");
            }
            rows.add(sb.substring(0, sb.length()-1));
        }
        return rows;
    }
}
