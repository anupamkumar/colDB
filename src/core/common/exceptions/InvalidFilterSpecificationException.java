/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.common.exceptions;

/**
 *
 * @author Anupam
 */
public class InvalidFilterSpecificationException extends Exception{
    public InvalidFilterSpecificationException() {
        System.err.println("Invalid Filter Specification");
    }
}
