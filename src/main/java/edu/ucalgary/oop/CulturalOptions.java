package edu.ucalgary.oop;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class CulturalOptions implements Serializable {
    static final long serialVersionUID = 1L;
    private HashMap<String, Set<String>> accommodations;

    public HashMap<String, Set<String>> getAccommodations() {
        return accommodations;
    }
}