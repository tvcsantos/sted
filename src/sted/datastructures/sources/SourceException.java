/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures.sources;

/**
 *
 * @author tvcsantos
 */
public class SourceException extends Exception {

    public SourceException(Throwable cause) {
        super(cause);
    }

    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(String message) {
        super(message);
    }

    public SourceException() {
    }

}
