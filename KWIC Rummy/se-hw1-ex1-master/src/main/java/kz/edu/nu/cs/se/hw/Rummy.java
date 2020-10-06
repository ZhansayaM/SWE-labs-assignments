package kz.edu.nu.cs.se.hw;



import java.util.*;

/**
 * Starter code for a class that implements the <code>PlayableRummy</code>
 * interface. A constructor signature has been added, and method stubs have been
 * generated automatically in eclipse.
 *
 * Before coding you should verify that you are able to run the accompanying
 * JUnit test suite <code>TestRummyCode</code>. Most of the unit tests will fail
 * initially.
 *
 * @see PlayableRummy
 * @see TestRummyCode
 *
 */
public class Rummy implements PlayableRummy {

    private int curPlayer;
    private ArrayList<Player> players;
    private ArrayList<String> discard;
    private ArrayList<Meld> meld;
    private Deck deck;
    private Steps step;

    public Rummy(String... players) {
        if (players.length <= 1)
            throw new RummyException("NOT_ENOUGH_PLAYERS", 2);
        else if (players.length > 6)
            throw new RummyException("EXPECTED_FEWER_PLAYERS", 8);

        this.players = new ArrayList<>();
        this.discard = new ArrayList<>();
        this.meld = new ArrayList<>();
        this.deck = new Deck();
        this.curPlayer = -1;
        for (int i = 0; i < players.length; i++)
            this.players.add(new Player(players[i], i));
        step = Steps.WAITING;
    }

    private String[] convertToArray(ArrayList<String> list) {
        String[] converted = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            converted[i] = list.get(i);
        return converted;
    }

    @Override
    public String[] getPlayers() {
        ArrayList<String> playerName = new ArrayList<>();
        for (Player player : players) playerName.add(player.getName());
        return convertToArray(playerName);
    }

    @Override
    public int getNumPlayers() {return players.size();}

    @Override
    public int getCurrentPlayer() {return curPlayer;}

    @Override
    public int getNumCardsInDeck() {return deck.size();}

    @Override
    public int getNumCardsInDiscardPile() {return discard.size();}

    @Override
    public String getTopCardOfDiscardPile() {
        if (discard.size() == 0)
            throw new RummyException("NOT_VALID_DISCARD", 13);
        return discard.get(discard.size()-1);
    }

    @Override
    public String[] getHandOfPlayer(int player) {
        if (players.size() <= player || player < 0)
            throw new RummyException("NOT_VALID_INDEX_OF_PLAYER", 10);
        return convertToArray(players.get(player).getHand().get());
    }

    @Override
    public int getNumMelds() {return meld.size();}

    @Override
    public String[] getMeld(int i) {
        if (meld.size() <= i || i < 0)
            throw new RummyException("NOT_VALID_INDEX_OF_MELD", 11);
        return meld.get(i).get();
    }

    @Override
    public void rearrange(String card) {
        if (Steps.WAITING == step) {
            deck.remove(card);
            deck.push(card);
        }
        else throw new RummyException("EXPECTED_WAITING_STEP", 3);
    }

    @Override
    public void shuffle(Long l) {
        if (step != Steps.WAITING)
            throw new RummyException("EXPECTED_WAITING_STEP", 3);
        deck.shuffle(l);
    }

    @Override
    public Steps getCurrentStep() {return step;}

    @Override
    public int isFinished() {
        if (Steps.FINISHED != step)
            return -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getHand().get().size() == 0 || (players.get(i).isFinished() && players.get(i).getHand().get().size() == 1))
                return i;
        }
        return -1;
    }

    @Override
    public void initialDeal() {
        if (Steps.WAITING != step)
            throw new RummyException("EXPECTED_WAITING_STEP", 3);
        else {
            int number;
            if (players.size() <= 1) throw new RummyException("NOT_ENOUGH_PLAYERS", 2);
            else if (players.size() == 2) number = 10;
            else if (players.size() < 5) number = 7;
            else if (players.size() < 7) number = 6;
            else throw new RummyException("EXPECTED_FEWER_PLAYERS", 8);
            for (int i = 0; i < number; i++)
                for (Player player : players) player.addCard(deck.pop(), false);
            curPlayer = 0;
            discard.add(deck.pop());
            step = Steps.DRAW;
        }
    }

    @Override
    public void drawFromDiscard() {
        if (Steps.DRAW != step)
            throw new RummyException("EXPECTED_DRAW_STEP", 4);
        players.get(curPlayer).addCard(discard.remove(discard.size()-1), true);
        step = Steps.MELD;
    }

    @Override
    public void drawFromDeck() {
        if (Steps.DRAW != step)
            throw new RummyException("EXPECTED_DRAW_STEP", 4);
        if (deck.size() == 0) {
            while (!discard.isEmpty())
                deck.push(discard.remove(discard.size()-1));
            discard.add(deck.pop());
        }
        players.get(curPlayer).addCard(deck.pop(), false);
        step = Steps.MELD;
    }

    @Override
    public void meld(String... cards) {
        if (Steps.MELD != step && Steps.RUMMY != step)
            throw new RummyException("EXPECTED_MELD_STEP_OR_RUMMY_STEP", 15);
        if (players.get(curPlayer).getHand().size() == 0)
            throw new RummyException("EXPECTED_CARDS", 7);
        meld.add(new Meld(cards));
        players.get(curPlayer).remove(cards);

        if (players.get(curPlayer).getHand().size() == 0) {
            step = Steps.FINISHED;
            players.get(curPlayer).setFinished(true);
        }
        if (players.get(curPlayer).getHand().size() == 1 && Steps.RUMMY == step) {
            step = Steps.FINISHED;
            players.get(curPlayer).setFinished(true);
        }
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        if (Steps.RUMMY != step && Steps.MELD != step)
            throw new RummyException("EXPECTED_MELD_STEP_OR_RUMMY_STEP", 15);
        else if (Steps.MELD == step) {
            if (meldIndex < 0 || meldIndex >= meld.size())
                throw new RummyException("NOT_VALID_INDEX_OF_MELD", 11);
            meld.get(meldIndex).add(cards);
            players.get(curPlayer).remove(cards);
            if (players.get(curPlayer).getHand().size() == 0) {
                step = Steps.FINISHED;
                players.get(curPlayer).setFinished(true);
            }
        } else {
            if (meldIndex >= meld.size() || meldIndex < 0)
                throw new RummyException("NOT_VALID_INDEX_OF_MELD", 11);
            meld.get(meldIndex).add(cards);
            players.get(curPlayer).remove(cards);
            if (players.get(curPlayer).getHand().size() == 1 || players.get(curPlayer).getHand().size() == 0) {
                step = Steps.FINISHED;
                players.get(curPlayer).setFinished(true);
            }
        }
    }

    @Override
    public void declareRummy() {
        if (step!= Steps.MELD)
            throw new RummyException("EXPECTED_MELD_STEP", 5);
        step = Steps.RUMMY;
    }

    @Override
    public void finishMeld() {
        if (Steps.RUMMY != step && Steps.MELD != step)
            throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
        if (players.get(curPlayer).getHand().size() > 1 && step == Steps.RUMMY) {
            step = Steps.DISCARD;
            throw new RummyException("", RummyException.RUMMY_NOT_DEMONSTRATED);
        }
        step = Steps.DISCARD;
    }

    @Override
    public void discard(String card) {
        if (Steps.DISCARD != step)
            throw new RummyException("EXPECTED_DISCARD_STEP", 6);
        players.get(curPlayer).remove(card);
        discard.add(card);
        if (players.get(curPlayer).getHand().size()==0) step = Steps.FINISHED;
        else {
            curPlayer = (curPlayer+1)%players.size();
            step = Steps.DRAW;
        }
    }

    @Override
    public ArrayList<String> discard() {return discard;}

    @Override
    public ArrayList<String> deck() {return deck.get();}


}
