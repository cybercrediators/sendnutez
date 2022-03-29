package send.nutez.model;

import androidx.room.Entity;

@Entity
public class NuteReferenceValue {
    private Nute nute;

    private String targetPopulationDescription;
    private int ageFrom; //months
    private int ageTo; //months
    private boolean gender; //true = female
}
