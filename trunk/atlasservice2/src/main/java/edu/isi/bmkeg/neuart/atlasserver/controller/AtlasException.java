package edu.isi.bmkeg.neuart.atlasserver.controller;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 * Exception thrown by our services.
 *
 */
@SoapFault(faultCode = FaultCode.CLIENT)
public class AtlasException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AtlasException(String message) {
        super(message);
    }

}
