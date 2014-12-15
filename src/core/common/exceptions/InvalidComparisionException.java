/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.common.exceptions;

import core.common.Comparator;
import core.common.DataTypes;

/**
 *
 * @author Anupam
 */
public class InvalidComparisionException extends Exception {
    public InvalidComparisionException() {
        System.err.println("Such a comparision is not allowed.");
    }
    
    public InvalidComparisionException(Comparator c,DataTypes d) {
        System.err.println("Comparing Comparator type "+c+" is not allowed for datatype "+d);
    }
}
