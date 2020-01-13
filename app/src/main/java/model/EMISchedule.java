package model;

public class EMISchedule {
    private String emiPeriod="";
    private String emiDate="";
    private String emiAmount="";
    private String interest="";
    private String serviceFees="";
    private String closingPrincipal="";


    public String getEmiPeriod() {
        return emiPeriod;
    }

    public void setEmiPeriod(String emiPeriod) {
        this.emiPeriod = emiPeriod;
    }

    public String getEmiDate() {
        return emiDate;
    }

    public void setEmiDate(String emiDate) {
        this.emiDate = emiDate;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(String serviceFees) {
        this.serviceFees = serviceFees;
    }

    public String getClosingPrincipal() {
        return closingPrincipal;
    }

    public void setClosingPrincipal(String closingPrincipal) {
        this.closingPrincipal = closingPrincipal;
    }
}
