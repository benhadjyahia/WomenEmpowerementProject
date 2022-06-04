package tn.esprit.spring.entities;



public class Payment {
	 public enum Currency{
	        usd, eur;
	    }
	    
	   
	    private int amount;
	    private Currency currency;


	    public String getDescription() {
	        return "aaa";
	    }

	  

	    

	   

	    public int getAmount() {
			return amount;
		}







		public void setAmount(int amount) {
			this.amount = amount;
		}







		public Currency getCurrency() {
	        return currency;
	    }

	    public void setCurrency(Currency currency) {
	        this.currency = currency;
	    }

}
