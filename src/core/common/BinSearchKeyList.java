/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.common;

import java.util.Set;

/**
 *
 * @author Anupam
 */
public class BinSearchKeyList {
    private long[] keylist;
    long val;
    
    public BinSearchKeyList(Set<Long> keyList) {
        Long[] la = new Long[keyList.size()];
        la = keyList.toArray(la);
        for(int i=0;i<keylist.length;i++) {
            keylist[i] = la[i].longValue();
        }
    }
    
    private int binSearch(int startpos, int endpos) {
        int mid = keylist.length/2;
        if(startpos >= endpos) 
            return -1;
        if(val==keylist[mid])
            return mid;
        else if(val < keylist[mid]) 
            return binSearch(0, mid);
        else
            return binSearch(mid, endpos);
    }
    
    public int getIndexForKey(long key) {
        val = key;
        return binSearch(0,keylist.length-1);
    }
}

