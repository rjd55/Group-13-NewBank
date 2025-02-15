package newbank.server;


import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

public class Account {

	//private String accountName;
	private int accountId;
	private double openingBalance;
	enum AccountType {CHECKING, SAVINGS, MONEYMARKET}
	private Account.AccountType accountType;
	enum InstructionType {WITHDRAW, DEPOSIT}
	private Account.InstructionType instructionType;
	private ArrayList<Transaction> transactions;
	private double interest;

    public Account(int accountId, double openingBalance, AccountType type) {
		this.accountId = accountId;
        this.openingBalance = openingBalance;
        this.accountType = type;
        this.interest = 0;
    }

	public Account(double openingBalance, AccountType type) {
		this.accountId = setAccountNumber(type);
        this.openingBalance = openingBalance;
        this.accountType = type;
        this.interest = 0;
    }

	public Account(int accountId){
		this.accountId = accountId;
	}

    public void modifyBalance(double amount, InstructionType type) {
        if (type.equals(InstructionType.DEPOSIT)) {
            openingBalance += amount;
        } else if (type.equals(InstructionType.WITHDRAW)) {
            // do overdraft check
            if (openingBalance - amount >= 0) {
                openingBalance -= amount;
            }

        }
    }

    public Integer setAccountNumber(AccountType type) {

        Random rand = new Random();


        //7 random digits form the base of the account number
        String baseAccountNumber = String.format("%07d", rand.nextInt(10000000));

        //the first digit of the account number relates to account type: 0 for children;
        //1 for seniors; 2 for checking; 3 for saving; 4 for money market; 5 for overdraft.
        String prefix = "";

        String accountNumber;

            switch (type) {
                case CHECKING:
                    prefix = "0";
                    break;
                case SAVINGS:
                    prefix = "1";
                    break;
                case MONEYMARKET:
                    prefix = "2";
                    break;
            }

            accountNumber = prefix + baseAccountNumber;
            NewBank bank = new NewBank();
            HashMap<String, Customer> customerAccounts = bank.getName2CustomersMapping();
            for(Customer customer: customerAccounts.values()){
                for(Account account: customer.getAccounts()){
                    if(account.getAccountId() !=Integer.parseInt(accountNumber)){
                        return Integer.parseInt(accountNumber);
                    } else {
                        return setAccountNumber(type);
                    }
                }
            }
            return null;
    }


	public int getAccountId(){
		return accountId;
	}

	public double getOpeningBalance(){
		return openingBalance;
	}

	public Account.AccountType getAccountType(){
		return accountType;
	}

	public Account.InstructionType getInstructionType(){
		return instructionType;
	}

	public ArrayList<Transaction> getTransactions(){
		return transactions;
	}

	public double getInterest() { return interest; }

	//setters
	public void setTransaction(Transaction transactions) {
        this.transactions.add(transactions);
	}

	public void setInterest(double interest) { this.interest = interest; }

	//print functions
	public String toString() {
		return (accountType + ", " + accountId + ": " + openingBalance);
	}

	public String transactionsToString() {
		String s = "";
		for(int i = 0; i < transactions.size(); i++){
			if(i != transactions.size() - 1){
				s += transactions.get(i).toString() + "\n";
			}
			else{
				s += transactions.get(i).toString();
			}
		}
		return s;
	}
}
