package com.soilhumidity.backend.util;

public class SoilBackendRegex {

    public static final String PHONE = "^[+]*[0-9]*[ ]{0,1}[(]{0,1}[ ]{0,1}[0-9]{1,3}[ ]{0,1}[)]{0,1}[ ]{0,1}[0-9]{1,3}[ ]{0,1}[0-9]{2}[ ]{0,1}[0-9]{2}[ ]{0,1}[-\\.\\/]{0,1}[ ]{0,1}[0-9]{1,5}$";

    public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
}
