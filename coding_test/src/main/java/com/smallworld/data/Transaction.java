package com.smallworld.data;

import java.util.Objects;

public class Transaction {
    // Represent your transaction data here.
    private long mtn;
    private double amount;
    private String senderFullName; 
    private int senderAge; 
    private String beneficiaryFullName; 
    private int beneficiaryAge; 
    private int issueId; 
    private boolean issueSolved; 
    private String issueMessage; 

    public long getMtn(){ return mtn; }
    public void setMtn(long rMtn){ mtn = rMtn; }

    public double getAmount(){ return amount; }
    public void setAmount(double rAmount){ amount = rAmount; }

    public String getSenderFullName(){ return senderFullName; }
    public void setSenderFullName(String rSenderFullName){ senderFullName = rSenderFullName; }

    public int getSenderAge(){ return senderAge; }
    public void setSenderAge(int rSenderAge){ senderAge = rSenderAge; }

    public String getBeneficiaryFullName(){ return beneficiaryFullName; }
    public void setBeneficiaryFullName(String rBeneficiaryFullName){ beneficiaryFullName = rBeneficiaryFullName; }

    public int getBeneficiaryAge(){ return beneficiaryAge; }
    public void setBeneficiaryAge(int rBeneficiaryAge){ beneficiaryAge = rBeneficiaryAge; }

    public int getIssueId(){ return issueId; }
    public void setIssueId(int rIssueId){ issueId = rIssueId; }

    public boolean getIssueSolved(){ return issueSolved; }
    public void setIssueSolved(boolean rIssueSolved){ issueSolved = rIssueSolved; }

    public String getIssueMessage(){ return Objects.requireNonNullElse(issueMessage, ""); }
    public void setIssueMessage(String rIssueMessage){ issueMessage = rIssueMessage; } 
    
    @Override
    public boolean equals(Object o) {
        if (this == o) 
        	return true;
        if (o == null || getClass() != o.getClass()) 
        	return false;
        Transaction that = (Transaction) o;
        return Objects.equals(mtn, that.mtn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mtn);
    }
}
