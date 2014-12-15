/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.classes;

import core.classes.Column;
import core.common.Comparator;
import core.common.DataTypes;
import core.common.exceptions.InvalidComparisionException;
import core.common.exceptions.KeyNotFoundException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Anupam
 */
public class Filter {
    Column column;
    double valueD;
    String valueS;
    boolean valueB;
    byte[] valueb;
    Date valueDt;
    DataTypes datatype;
    Comparator comparator;
    public Filter(Column<String> col, String colValue,Comparator c) throws InvalidComparisionException {
        if(c.equals(Comparator.EQUAL) || c.equals(Comparator.NOTEQUAL)) {
            column = col;
            valueS = colValue;
            datatype = DataTypes.TEXT;
            comparator = c;
        }            
        else {
            throw new InvalidComparisionException();
        }
    }
    
    public Filter(Column<Byte> col, byte[] bytes, Comparator c) throws InvalidComparisionException {
         if(c.equals(Comparator.EQUAL) || c.equals(Comparator.NOTEQUAL)) {
            column = col;
            valueb = bytes;
            datatype = DataTypes.BINARY;
            comparator = c;
        }            
        else {
            throw new InvalidComparisionException();
        }
    }
    
     public Filter(Column<Boolean> col, boolean b, Comparator c) throws InvalidComparisionException {
         if(c.equals(Comparator.EQUAL) || c.equals(Comparator.NOTEQUAL)) {
            column = col;
            valueB = b;
            datatype = DataTypes.BOOLEAN;
            comparator = c;
        }            
        else {
            throw new InvalidComparisionException();
        }
    }
    
    public Filter(Column<Double> col, double val, Comparator c) {
        column = col;
        valueD = val;
        comparator = c;
        datatype = DataTypes.NUMBER;
    }
    
    public Filter(Column<Date> col, Date dt, Comparator c) {
        column = col;
        valueDt = dt;
        comparator = c;
        datatype = DataTypes.DATETIME;
    }
    
    public Set<Long> getFilteredKeyList() throws KeyNotFoundException {
        Set<Long> keyList=null;
        switch(comparator) {
            case EQUAL:
               try {
                    keyList = getFilteredKeyListForEqualValues();   
                } catch(KeyNotFoundException e) {
                    keyList = new TreeSet<>();
                }
               break;
            case NOTEQUAL:
                keyList = getFilteredKeyListForNOTEqualValues();
                break;
            case LESSER:
                keyList = getFilterListForLesserValues();
                break;
            case GREATER:
                keyList = getFilterListForGreaterValues();
                break;
            case LESSEREQUAL:    
                try {
                    keyList = getFilteredKeyListForEqualValues();   
                } catch(KeyNotFoundException e) {
                    keyList = new TreeSet<>();
                }
                keyList.addAll(getFilterListForLesserValues());
                break;
            case GREATEREQUAL:
                try {
                    keyList = getFilteredKeyListForEqualValues();   
                } catch(KeyNotFoundException e) {
                    keyList = new TreeSet<>();
                }
                keyList.addAll(getFilterListForGreaterValues());
                break;
        }
        return keyList;
    }
    
    private Set<Long> getFilteredKeyListForEqualValues() throws KeyNotFoundException {
        switch(datatype) {
            case TEXT:
                return column.getRowKeysForValue(valueS);
            case NUMBER:
                return column.getRowKeysForValue(valueD);
            case BINARY:
                return column.getRowKeysForValue(valueb);
            case DATETIME:
                return column.getRowKeysForValue(valueDt);
            case BOOLEAN:
                return column.getRowKeysForValue(valueB);
            default:
                return null;
        }
    }
    
    private Set<Long> getFilteredKeyListForNOTEqualValues() throws KeyNotFoundException {    
        Set<Long> fl = new TreeSet<>();         
        switch(datatype) {
            case TEXT:                
                Map<String, Set<Long>> ls = column.getValueKeyList();                
                for(String le : ls.keySet()) {
                    if(valueS.equals(le)) {
                        Set<Long> temp = ls.get(le);
                        fl.addAll(temp);
                    }
                }
                return fl;
            case NUMBER:
                Map<Double, Set<Long>> ld = column.getValueKeyList();
                for(Double le : ld.keySet()) {
                    if(valueD != le) {
                        Set<Long> temp = ld.get(le);
                        fl.addAll(temp);
                    }
                }
                return fl;
            case BINARY:
                return null;
            case DATETIME:
                Map<Date, Set<Long>> ldt = column.getValueKeyList();
                for(Date le : ldt.keySet()) {
                    if(valueDt != le) {
                        Set<Long> temp = ldt.get(le);
                        fl.addAll(temp);
                    }
                }
                return fl;
            case BOOLEAN:
                Map<Boolean, Set<Long>> lb = column.getValueKeyList();
                for(Boolean le : lb.keySet()) {
                    if(valueB != le) {
                        Set<Long> temp = lb.get(le);
                        fl.addAll(temp);
                    }
                }
                return fl;
            default:
                return null;
        }
    }
    
    private Set<Long> getFilterListForGreaterValues() throws KeyNotFoundException {
        Set<Long> fl = new TreeSet<>();         
        switch(datatype) {            
            case NUMBER:
                Map<Double, Set<Long>> ld = column.getValueKeyList();
                Double[] da = new Double[ld.size()];
                int j=0;
                for(Double d : ld.keySet()) {
                    da[j++] = d;
                }
                for(int i=da.length-1;i>=0;i--) {
                    if(valueD < da[i]) {
                         Set<Long> temp = ld.get(da[i]);
                         fl.addAll(temp);
                    }
                    else {
                        break;
                    }
                }                
                return fl;
            case DATETIME:
                Map<Date, Set<Long>> ldt = column.getValueKeyList();
                Date[] dat = new Date[ldt.size()];
                ldt.keySet().toArray(dat);
                for(int i=dat.length-1;i>=0;i--) {
                    if(valueDt.compareTo(dat[i]) > 0) 
                        break;
                    else {
                        Set<Long> temp = ldt.get(dat[i]);
                        fl.addAll(temp);
                    }
                }                
                return fl;            
            default:
                return null;
        }
    }
    
    private Set<Long> getFilterListForLesserValues() throws KeyNotFoundException {
        Set<Long> fl = new TreeSet<>();         
        switch(datatype) {            
            case NUMBER:
                Map<Double, Set<Long>> ld = column.getValueKeyList();
                Double[] da = new Double[ld.size()];
                da = ld.keySet().toArray(da);
                for(int i=0;i<da.length;i++) {
                    if(valueD > da[i]) {
                         Set<Long> temp = ld.get(da[i]);
                         fl.addAll(temp);
                    }
                    else {
                        break;
                    }
                }                
                return fl;
            case DATETIME:
                Map<Date, Set<Long>> ldt = column.getValueKeyList();
                Date[] dat = new Date[ldt.size()];
                dat = ldt.keySet().toArray(dat);
                for(int i=0;i<dat.length;i++) {
                    if(valueDt.compareTo(dat[i]) < 0) 
                        break;
                    else {
                        Set<Long> temp = ldt.get(dat[i]);
                        fl.addAll(temp);
                    }
                }                
                return fl;            
            default:
                return null;
        }
    }
}
