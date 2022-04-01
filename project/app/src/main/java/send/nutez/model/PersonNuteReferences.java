package send.nutez.model;

import java.util.HashMap;
import java.util.Map;

import send.nutez.utils.StorageDatabaseUtils;

public class PersonNuteReferences {
    public Map<String, NuteReferenceValue> referenceValueMap;

    public PersonNuteReferences() {
        referenceValueMap = new HashMap<>();
    }

}
