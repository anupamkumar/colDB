/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.interfaces;

import core.classes.FilterSpec;
import core.classes.RecordDef;
import core.common.Comparator;
import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.InvalidComparisionException;
import core.common.exceptions.InvalidFilterSpecificationException;
import core.common.exceptions.KeyNotFoundException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Anupam
 */
public interface Table {
    public RecordDef getSchema();
    public boolean insert(HashMap<String, String> c) throws DataTypeEnumMappingException;
    public boolean delete();
    public boolean update();
    public boolean create(String n,String o, Map<String, DataTypes> defination) throws DataTypeEnumMappingException;
    public boolean drop();
    public ArrayList<String> select(ArrayList<FilterSpec> OrArrayOfandFilters, ArrayList<String> selectColNames)  throws InvalidFilterSpecificationException, KeyNotFoundException, InvalidComparisionException,ParseException;  
    public ArrayList<String> select(ArrayList<String> selectColNames) throws 
    KeyNotFoundException, ParseException;
}
