/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coldb;

import api.CRUDQueryParser;
import core.classes.InMemoryTableManager;
import core.common.Comparator;
import core.common.exceptions.DataTypeEnumMappingException;
import core.common.exceptions.InvalidSQLException;
import core.common.exceptions.KeyNotFoundException;
import core.common.exceptions.TableNotFoundException;
import core.interfaces.TableManager;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author anupam
 */
public class TestQueryParser {
    public static void main(String[] args) throws TableNotFoundException, InvalidSQLException, DataTypeEnumMappingException, KeyNotFoundException, ParseException {
        String ctr = "create table cust (id number,name text,jd datetime);";
        String str = "insert into cust ( name ) values ( anupam );";
        String sqstr = "select * from cust where id='1' and name='anupam'";
        TableManager tm = new InMemoryTableManager();
        CRUDQueryParser iqr = new CRUDQueryParser();
        iqr.createStatement(tm, ctr);
        iqr.insertStatement(tm, str);
        iqr.selectStatement(tm, sqstr);

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
