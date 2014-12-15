/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coldb;

import core.classes.InMemoryTable;
import core.common.Comparator;
import core.common.DataTypes;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.InvalidComparisionException;
import core.common.exceptions.InvalidFilterSpecificationException;
import core.common.exceptions.KeyNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Anupam
 */
public class ColDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidFilterSpecificationException, KeyNotFoundException, InvalidComparisionException, ParseException, DataTypeEnumMappingException {
        InMemoryTable imt = new InMemoryTable();
        HashMap<String, DataTypes> colDef = new HashMap<>();
        colDef.put("id", DataTypes.NUMBER);
        colDef.put("name", DataTypes.TEXT);
        colDef.put("jd", DataTypes.DATETIME);
        System.out.println(imt.create("cust", "ak", colDef));
        ArrayList<HashMap<String, String>> rows = new ArrayList<>();
        HashMap<String, String> row = new HashMap<>();
        row.put("id", "1");
        row.put("name", "anupam");
        row.put("jd", "2012-08-20 00:00:00");
        rows.add(row);
        row = new HashMap<>();
        row.put("id","2");
        row.put("name", "kumar");
        row.put("jd", "2013-08-20 00:00:00");
        rows.add(row);
        row = new HashMap<>();
        row.put("id","5");
        row.put("name", "zztop");        
        rows.add(row);
        row = new HashMap<>();
        row.put("id","3");
        row.put("name", "kumar");
        row.put("jd", "2013-08-20 00:00:00");
        rows.add(row);
        row = new HashMap<>();
        row.put("id","4");
        row.put("name", "xx");
        row.put("jd", "2014-08-20 00:00:00");
        rows.add(row);
        for(HashMap<String, String> r : rows) {
            System.out.println(imt.insert(r));
        }
//        ArrayList<String> n = new ArrayList<>();
//        n.add("jd");
//        ArrayList<String> v = new ArrayList<>();
//        v.add("2013-10-20 00:00:00");
//        ArrayList<Comparator> c = new ArrayList<>();
//        c.add(Comparator.GREATEREQUAL);
//        ArrayList<String> op = imt.selectAndGroup(n, v, c);
//        for(String l : op) {
//            System.out.println(l);
//        }
    }
    
}
