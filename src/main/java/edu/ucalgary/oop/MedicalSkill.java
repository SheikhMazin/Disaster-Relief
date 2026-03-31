package edu.ucalgary.oop;

import java.time.LocalDate;

public class MedicalSkill extends Skill{
    private String certificationType;
    private LocalDate certificationExpiryDate;

    public MedicalSkill(int skillID, int victimID, String proficiencyLevel, String certificationType, LocalDate certificationExpiryDate) {
        super(skillID, victimID, "Medical", proficiencyLevel);

        if (certificationType == null || certificationType.trim().isEmpty()) {
            throw new IllegalArgumentException("certificationType cannot be null or empty.");
        }
        if (certificationExpiryDate == null || certificationExpiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("certificationExpiryDate cannot be null or in the past.");
        }

        this.certificationType = certificationType;
        this.certificationExpiryDate = certificationExpiryDate;
    }

    public String getCertificationType() { return certificationType; }
    public LocalDate getCertificationExpiryDate() { return certificationExpiryDate; }

    public void setCertificationType(String certificationType){
        if (certificationType == null || certificationType.trim().isEmpty()) {
            throw new IllegalArgumentException("certificationType cannot be null or empty.");
        }

        this.certificationType = certificationType;
    }

    public void setCertificationExpiryDate(LocalDate certificationExpiryDate) {
        if (certificationExpiryDate == null || certificationExpiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("certificationExpiryDate cannot be null or in the past.");
        }

        this.certificationExpiryDate = certificationExpiryDate;
    }
}