package edu.ucalgary.oop;

import java.util.ArrayList;

/**
 * MockDataRepository
 *
 * A no-op implementation of DataRepository used exclusively in unit tests.
 * All methods do nothing or return empty lists, allowing service classes
 * to be tested without a real database connection.
 */
public class MockDataRepository implements DataRepository {

    @Override public ArrayList<DisasterVictim> loadVictims()    { return new ArrayList<>(); }
    @Override public ArrayList<Location>       loadLocations()  { return new ArrayList<>(); }
    @Override public ArrayList<Supply>         loadSupplies()   { return new ArrayList<>(); }
    @Override public ArrayList<ReliefService>  loadInquiries()  { return new ArrayList<>(); }

    @Override public void saveVictim(DisasterVictim victim)     {}
    @Override public void updateVictim(DisasterVictim victim)   {}
    @Override public void softDeleteVictim(int victimID)        {}
    @Override public void hardDeleteVictim(int victimID)        {}
    @Override public int  getNextVictimID()                     { return 1; }

    @Override public void saveSupply(Supply supply)             {}
    @Override public void updateSupply(Supply supply)           {}
    @Override public int  getNextSupplyID()                     { return 1; }

    @Override public void saveInquiry(ReliefService inquiry)    {}
    @Override public void updateInquiry(ReliefService inquiry)  {}
    @Override public int  saveInquirer(Inquirer inquirer)       { return 1; }
    @Override public int  getNextInquiryID()                    { return 1; }

    @Override public void saveRequirement(VictimRequirement req)              {}
    @Override public void deleteRequirement(int victimID, String type)        {}

    @Override public void saveSkill(Skill skill)                {}
    @Override public void deleteSkill(int skillID)              {}
    @Override public int  getNextSkillID()                      { return 1; }

    @Override public void saveMedicalRecord(MedicalRecord record, int victimID) {}
    @Override public void saveFamilyConnection(FamilyRelation relation)          {}
}