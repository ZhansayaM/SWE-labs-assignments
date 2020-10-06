package kz.edu.nu.cs.se;

public class PaidState extends State {
    VendingMachine vendingMachine;

    public PaidState(VendingMachine vend) {
        vendingMachine = vend;
        //super();
    }
    @Override
    public void insertCoin(int coin){
        if (coin == 50 || coin == 100){
            vendingMachine.balance += coin;
        }
        else {
            vendingMachine.balance += coin;
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int refund(){
        int refunded = vendingMachine.balance;
        vendingMachine.balance = 0;
        vendingMachine.setCurrentState(vendingMachine.idle);
        return refunded;
    }

    @Override
    public int vend(){
        int bal;
        vendingMachine.balance -= 200;
        vendingMachine.setCurrentState(vendingMachine.idle);
        bal = vendingMachine.balance;
        vendingMachine.balance = 0;
        return bal;
    }
}
