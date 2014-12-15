/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package api;

import core.classes.FilterSpec;
import core.classes.RecordDef;
import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.InvalidSQLException;
import core.common.exceptions.KeyNotFoundException;
import core.common.exceptions.TableNotFoundException;
import core.interfaces.TableManager;
import core.interfaces.Table;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author anupam
 */
public class CRUDQueryParser {    
    public void createStatement(TableManager manager, String createQuery) throws InvalidSQLException, DataTypeEnumMappingException {
        String tableName;
        String[] tokens = createQuery.split("\\s+");
        if(tokens[0].equalsIgnoreCase("create") && tokens[1].equalsIgnoreCase("table") && tokens[2].matches("[A-Za-z0-9_]+")) {
            tableName = tokens[2];
            HashMap<String, DataTypes> cols = new HashMap<>();
            String[] columns = createQuery.split("\\(")[1].split("\\)")[0].split(",");
            for(String column : columns) {
                String colName = column.split(" ")[0];
                String colDataType = column.split(" ")[1];
                if(colDataType.equalsIgnoreCase("number")) {
                    cols.put(colName, DataTypes.NUMBER);
                }
                else if(colDataType.equalsIgnoreCase("autonumber")) {
                    System.out.println("adding new autonumber...");
                    cols.put(colName, DataTypes.AUTONUMBER);
                }
                else if(colDataType.equalsIgnoreCase("text")) {
                    cols.put(colName, DataTypes.TEXT);
                }
                else if(colDataType.equalsIgnoreCase("xml")) {
                    cols.put(colName,DataTypes.XML);
                }
                else if(colDataType.equalsIgnoreCase("json")) {
                    cols.put(colName,DataTypes.JSON);
                }
                else if(colDataType.equalsIgnoreCase("boolean")) {
                    cols.put(colName,DataTypes.BOOLEAN);
                }
                else if(colDataType.equalsIgnoreCase("binary")) {
                    cols.put(colName,DataTypes.BINARY);
                }
                else if(colDataType.equalsIgnoreCase("datetime")) {
                    cols.put(colName,DataTypes.DATETIME);
                }
                else if(colDataType.equalsIgnoreCase("timestamp")) {
                    cols.put(colName,DataTypes.TIMESTAMP);
                }
                else {
                    throw new InvalidSQLException();
                }
            }
            System.out.println("Result: "+manager.createTable(tableName, cols));
        }
        else {
            throw new InvalidSQLException();
        }
    }
    
    public void insertStatement(TableManager manager, String insertQuery) throws TableNotFoundException, InvalidSQLException, DataTypeEnumMappingException {
        String tableName;
        String[] tokens = insertQuery.split("\\s+");
        if(tokens[0].equalsIgnoreCase("insert") && tokens[1].equalsIgnoreCase("into") && tokens[2].matches("[A-Za-z0-9_]+")) {
            tableName = tokens[2];
            if(tokens[3].equalsIgnoreCase("values")) {
                String[] tokenCol = insertQuery.split("(");
                String[] valList = tokenCol[1].split(")")[0].split(",");
                RecordDef rd = manager.getTableSchema(tableName);
                HashMap<String, String> row = new HashMap<>();
                Set<String> cn = rd.getColumnNames();
                if(cn.size() == valList.length) {
                    int i=0;
                    for(String col : cn) {
                        row.put(col, valList[i]);
                    }
                    System.out.println("Result: "+manager.getTableReference(tableName).insert(row));
                }
                else {
                    throw new InvalidSQLException();
                }
            }
            else {
                String[] tokenCol = insertQuery.split("\\(");
                String[] colList = tokenCol[1].replaceAll(" ", "").split("\\)")[0].split(",");
                String[] valList = tokenCol[2].split("\\)")[0].split(",");
                if(colList.length == valList.length) {
                    RecordDef rd = manager.getTableSchema(tableName);
                    LinkedHashMap<String, String> row = new LinkedHashMap<>();
                    for(int i=0;i<colList.length;i++) {
                        row.put(colList[i], valList[i]);
                    }
                    for(String col : rd.getColumnNames()) {
                        if(row.containsKey(col))
                            continue;
                        else {
                            if(rd.getDataType(col) == DataTypes.TEXT)
                                row.put(col, "");
                            else if(rd.getDataType(col) == DataTypes.NUMBER)
                                row.put(col, Double.NaN+"");
                            else if(rd.getDataType(col) == DataTypes.DATETIME)
                                row.put(col, "0000-01-01 00:00:00");                            
                        }                            
                    }
                    System.out.println("Result: "+manager.getTableReference(tableName).insert(row));
                }
                else {
                    throw new InvalidSQLException();
                }
            }
        }
        else {
            throw new InvalidSQLException();
        }
    }
    
    public void selectStatement(TableManager manager, String selectQuery) throws InvalidSQLException, TableNotFoundException, KeyNotFoundException, ParseException {
        SelectQueryParser qp = new SelectQueryParser(selectQuery);
        String[] tableList = qp.getTablesList();
        ArrayList<String> colList =null; 
        ArrayList<FilterSpec> filterspecs;
        if(!qp.allColsRequested()) {
           colList = qp.getSelectColList();
        }
        else {
            //get all columns in the table. ask table manager to supply the info
            RecordDef rd = manager.getTableSchema(tableList[0]);
            Set<String> cl = rd.getColumnNames();            
            colList = new ArrayList<>();
            colList.addAll(cl);            
        }
        if(qp.queryContainsFilters()) {
            
        }
        else {
            Table tbl = manager.getTableReference(tableList[0]);
            ArrayList<String> rows = tbl.select(colList);
            for(String row : rows ) {
                System.out.println(row);
            }
        }
    }
}
