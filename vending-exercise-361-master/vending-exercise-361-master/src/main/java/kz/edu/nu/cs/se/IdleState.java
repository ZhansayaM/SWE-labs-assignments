package kz.edu.nu.cs.se;

public class IdleState extends State {
    VendingMachine vendingMachine;

    public IdleState(VendingMachine vend) {
        vendingMachine = vend;
        //super();
    }

    @Override
    public void insertCoin(int coin){
        if (coin == 50 || coin == 100){
            vendingMachine.balance += coin;
            vendingMachine.setCurrentState(vendingMachine.enteringCoins);
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
        throw new IllegalStateException();
    }
}
