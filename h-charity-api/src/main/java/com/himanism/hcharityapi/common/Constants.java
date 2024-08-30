package com.himanism.hcharityapi.common;

import java.util.HashMap;
import java.util.Set;

public class Constants {

    public static final Set<String> ROLES_CAN_DELETE_INSTITUTE = Set.of("ADMIN", "ORGANISATION_VOLUNTEER");
    public static final Set<String> ROLES_CAN_EDIT_INSTITUTE = Set.of("ADMIN", "ORGANISATION_VOLUNTEER",
            "INSTITUTE_OWNER");

    // Example Hashmap for key value pairs
    public static final HashMap<String, String> INSTITUTE_TYPE = new HashMap<>() {
        private static final long serialVersionUID = 134L;
        {
            put("MOSQUE", "MOSQUE");
            put("ORPHANAGE", "ORPHANAGE");
            put("SCHOOL", "SCHOOL");
        }
    };
}
