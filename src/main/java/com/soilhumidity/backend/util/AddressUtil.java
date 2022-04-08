package com.soilhumidity.backend.util;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class AddressUtil {

    private AddressUtil() {
    }

    public static Address of(String val) throws AddressException {
        return new InternetAddress(val);
    }
}
