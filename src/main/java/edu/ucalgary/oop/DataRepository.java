package edu.ucalgary.oop;
import java.util.ArrayList;
public interface DataRepository {
    ArrayList<DisasterVictim> loadVictims();
    ArrayList<Location> loadLocations();
    ArrayList<Supply> loadSupplies();
    ArrayList<ReliefService> loadInquiries();

    void saveVictim(DisasterVictim victim);
    void updateVictim(DisasterVictim victim);
    void softDeleteVictim(int victimID);
    void hardDeleteVictim(int victimID);

    void saveSupply(Supply supply);
    void updateSupply(Supply supply);

    void saveInquiry(ReliefService inquiry);
    void updateInquiry(ReliefService inquiry);

    void saveRequirement(VictimRequirement requirement);
    void deleteRequirement(int victimID, String requirementType);

    void saveSkill(Skill skill);
    void deleteSkill(int skillID);
}
