package model;

import androidx.annotation.NonNull;

public class BankDetails {
    String perfiosBankId="";
    String perfiosBankName="";

    String isBanking="";
    String isStatement="";

    public String getPerfiosBankId() {
        return perfiosBankId;
    }

    public void setPerfiosBankId(String perfiosBankId) {
        this.perfiosBankId = perfiosBankId;
    }

    public String getPerfiosBankName() {
        return perfiosBankName;
    }

    public void setPerfiosBankName(String perfiosBankName) {
        this.perfiosBankName = perfiosBankName;
    }

    public String getIsBanking() {
        return isBanking;
    }

    public void setIsBanking(String isBanking) {
        this.isBanking = isBanking;
    }

    public String getIsStatement() {
        return isStatement;
    }

    public void setIsStatement(String isStatement) {
        this.isStatement = isStatement;
    }

    @NonNull
    @Override
    public String toString() {
        return perfiosBankName;
    }
}
