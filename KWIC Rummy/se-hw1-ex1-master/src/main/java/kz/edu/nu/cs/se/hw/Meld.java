package kz.edu.nu.cs.se.hw;

import java.util.*;

public class Meld {
    private int type;
    private ArrayList<String> cards;

    public Meld(String... cards) {
        this.cards = new ArrayList<>();
        if (cards.length < 3) throw new RummyException("NOT_VALID_MELD", 1);
        char suit = cards[0].charAt(cards[0].length() - 1);
        String rank = cards[0].substring(0, cards[0].length() - 1);
        boolean rankEqual = true, suitEqual = true;
        for (int i = 1; i < cards.length; i++) {
            char s = cards[i].charAt(cards[i].length() - 1);
            String r = cards[i].substring(0, cards[i].length() - 1);
            if (rankEqual && !r.equals(rank)) { rankEqual = false; }
            if (suitEqual && s != suit) { suitEqual = false; }
        }
        if (!suitEqual&& !rankEqual) {
            throw new RummyException("EXPECTED_CARDS", 7);
        }
        List<String> ranks =  new ArrayList<>(Arrays.asList("A","2","3","4","5","6","7","8","9","10","J","Q","K"));
        if (suitEqual) {
            int last = -1;
            for (int i = 0; i <cards.length; i++) {
                if (i==0) last = ranks.indexOf(cards[i].substring(0, cards[i].length() - 1));
                if (i!=0 && ranks.indexOf(cards[i].substring(0, cards[i].length() - 1)) != last+1) {
                    throw new RummyException("NOT_VALID_MELD", 1);
                } else {
                    last = ranks.indexOf(cards[i].substring(0, cards[i].length() - 1));
                }
            }
            type = 2;
        } else type = 1;
        Collections.addAll(this.cards, cards);
    }

    public String[] get() { return convertToArray(cards); }

    public void add(String... cards) {
        char suit = this.cards.get(0).charAt(this.cards.get(0).length() - 1);
        String rank = this.cards.get(0).substring(0, this.cards.get(0).length() - 1);
        for (int i = 0; i < cards.length; i++) {
            char s = cards[i].charAt(cards[i].length() - 1);
            String r = cards[i].substring(0, cards[i].length() - 1);
            if (type==1) {
                if (!r.equals(rank))throw new RummyException("NOT_VALID_MELD", 1);
            } else { if (s!=suit) { throw new RummyException("NOT_VALID_MELD", 1); }
            }
        }
        System.out.println(cards.length);
        this.cards.addAll(Arrays.asList(cards));
    }

    private String[] convertToArray(ArrayList<String> list) {
        String[] converted = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            converted[i] = list.get(i);
        return converted;
    }
}
