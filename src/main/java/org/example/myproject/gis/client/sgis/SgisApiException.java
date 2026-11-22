package org.example.myproject.gis.client.sgis;

/** Contains only a safe summary, never the credential-bearing request URI. */
public class SgisApiException extends RuntimeException {
    public SgisApiException(String message) {
        super(message);
    }
}

