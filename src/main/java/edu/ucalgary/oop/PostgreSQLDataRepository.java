package edu.ucalgary.oop;

import java.util.ArrayList;

public class PostgreSQLDataRepository implements DataRepository{
    private DatabaseManager dbManager;

    public PostgreSQLDataRepository(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    @Override
    public ArrayList<DisasterVictim> loadVictims() {
        return null;
    }

    @Override
    public ArrayList<Location> loadLocations() {
        return null;
    }

    @Override
    public ArrayList<Supply> loadSupplies() {
        return null;
    }

    @Override
    public ArrayList<ReliefService> loadInquiries() {
        return null;
    }

    @Override
    public void saveVictim(DisasterVictim victim) {

    }

    @Override
    public void updateVictim(DisasterVictim victim) {

    }

    @Override
    public void softDeleteVictim(int victimID) {

    }

    @Override
    public void hardDeleteVictim(int victimID) {

    }

    @Override
    public void saveSupply(Supply supply) {

    }

    @Override
    public void updateSupply(Supply supply) {

    }

    @Override
    public void saveInquiry(ReliefService inquiry) {

    }

    @Override
    public void updateInquiry(ReliefService inquiry) {

    }

    @Override
    public void saveRequirement(VictimRequirement requirement) {

    }

    @Override
    public void deleteRequirement(int victimID, String requirementType) {

    }

    @Override
    public void saveSkill(Skill skill) {

    }

    @Override
    public void deleteSkill(int skillID) {

    }
}
