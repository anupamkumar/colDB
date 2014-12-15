/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package api;

import core.classes.FilterSpec;
import core.common.exceptions.InvalidSQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author anupam
 */
public class SelectQueryParser {
    private String query;
    private String[] selectColNames;    
    private String[] tableNames;
    private String[] tableAliases;
    private ArrayList<FilterSpec> filterSpecs;
    private boolean allColsRequested=false;
    private boolean filters=false;
    public SelectQueryParser(String selectQuery) throws InvalidSQLException {
        this.query = selectQuery;
        parseSelectClause();
        parseFromClause();
        filters = query.matches("(?s).*[Ww]{1}[Hh]{1}[Ee]{1}[rR]{1}[eE]{1}(?s).*");
        if(filters)
            parseWhereClause();
    }
    
    private void parseSelectClause() throws InvalidSQLException {
        
        if(query.matches("(?s).*[fF]{1}[Rr]{1}[oO]{1}[mM]{1}(?s).*")) {
           String[] s = query.split("[fF]{1}[Rr]{1}[oO]{1}[mM]{1}");
           if(s[0].matches("[sS]{1}[eE]{1}[lL]{1}[eE]{1}[cC]{1}[tT]{1}(?s).*")) {
               selectColNames = s[0].split("[sS]{1}[eE]{1}[lL]{1}[eE]{1}[cC]{1}[tT]{1}")[1].replaceAll(" ","").split(",");
               if(selectColNames.length == 1) {
                   allColsRequested = selectColNames[0].equals("*");   
               }
               if(selectColNames.length > 1) {
                   for(String colName : selectColNames) {
                       if(colName.isEmpty()) {
                           System.err.println("SQL Error in SELECT Clause");
                           throw new InvalidSQLException();
                       }
                   }
               }
           }
           else {
               System.err.println("SQL Error in SELECT Clause");
               throw new InvalidSQLException();
           }
        }
        else {
            System.err.println("SQL Error in SELECT Clause");
            throw new InvalidSQLException();
        }
    }
    
    private void parseFromClause() throws InvalidSQLException {
        if(query.matches("(?s).*[fF]{1}[Rr]{1}[oO]{1}[mM]{1}(?s).*")) {
            String[] s = query.split("[fF]{1}[Rr]{1}[oO]{1}[mM]{1}");
            String[] tablesAndAliases = s[1].split("[Ww]{1}[Hh]{1}[Ee]{1}[rR]{1}[eE]{1}")[0].split(",");
            tableNames = new String[tablesAndAliases.length];
            tableAliases = new String[tablesAndAliases.length];
            int i=0;            
            for(String t : tablesAndAliases) {
                if(t.matches("\\s*[a-zA-Z0-9]+\\s+(as)?\\s+[a-zA-Z0-9]+\\s*")) {
                    String[] t2 = t.split("\\s+");
                    tableNames[i] = t2[0].replaceAll(" ", "");
                    if(t2.length == 2) {
                        tableAliases[i] = t2[1].replaceAll(" ", "");
                    }
                    else {
                        tableAliases[i] = t2[2].replaceAll(" ", "");
                    }                        
                }
                else if(t.matches("\\s*[a-zA-Z0-9]+\\s*")) {
                    tableNames[i] = t.replaceAll(" ", "");
                }
                else {
                    System.err.println("SQL Error in FROM Clause");
                    throw new InvalidSQLException();
                }
                i++;
            }            
        }
        else {
            System.err.println("SQL Error in FROM Clause");
            throw new InvalidSQLException();
        }
    }
    
    private void parseWhereClause() throws InvalidSQLException {
        String whereClause = query.split("\\s+[Ww]{1}[Hh]{1}[Ee]{1}[rR]{1}[Ee]{1}\\s+")[1].split("\\s+[Oo]{1}[Rr]{1}[Dd]{1}[Ee]{1}[Rr]{1}\\s+")[0];
        ArrayList<String> colValues = getColValuesFromWhereClause(whereClause);        
        int i=0;
        for(String cv : colValues) {
            System.out.println(cv);
            whereClause = whereClause.replaceAll(cv, "<<"+(i++)+">>").replaceAll("'", "");
        }
        //debug purposes only. remove/comment the sysout 
        System.out.println(whereClause);
//        String[] ORs = whereClause.split("\\s+[Oo]{1}[Rr]{1}\\s+");
//        filterSpecs = new ArrayList<>();
//        for(String or : ORs) {
//            FilterSpec filter = new FilterSpec();
//            String[] ANDs = or.split("\\s+[aA]{1}[Nn]{1}[Dd]{1}");
//            for(String a : ANDs) {
//                
//            }
//        }
    }
    
    private ArrayList<String> getColValuesFromWhereClause(String whereClause) throws InvalidSQLException {
        ArrayList<String> colVals = new ArrayList<>();
        int idx = whereClause.indexOf("'");
        while(idx != -1) {
            try {
                int idxE = whereClause.substring(++idx).indexOf("'");
                idxE = idx+idxE;
                colVals.add(whereClause.substring(idx, idxE));
                whereClause = whereClause.substring(++idxE);
                idx = whereClause.indexOf("'");
            } catch(Exception e) {
                System.err.println("SQL Error in WHERE Clause");
                throw new InvalidSQLException();
            }
        }
        return colVals;
    }
    
    public ArrayList<String> getSelectColList() {
        ArrayList<String> s = new ArrayList<>();
        s.addAll(Arrays.asList(selectColNames));
        return s;
    }
    
    public String[] getTablesList() {
        return tableNames;
    }
    
    public String[] getTableAliases() {
        return tableAliases;
    }
    
    public boolean queryContainsFilters() {
        return filters;
    }
    
    public boolean allColsRequested() {
        return allColsRequested;
    }
}
