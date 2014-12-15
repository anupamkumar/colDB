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
public class ColumnDefinationAlreadyExistsException extends Exception {
    public ColumnDefinationAlreadyExistsException() {
        System.err.println("Column defination already exists");
    }
    public ColumnDefinationAlreadyExistsException(String newCol) {
        System.err.println("Column defination "+newCol+" already exists");
    }
}
