package client.models.account;

import client.models.card.Card;
import client.view.request.InputException;

import java.util.ArrayList;

public class Collection {
    private ArrayList<Card> heroes;
    private ArrayList<Card> minions;
    private ArrayList<Card> spells;
    private ArrayList<Card> items;

    public ArrayList<Card> getHeroes() {
        return this.heroes;
    }

    public ArrayList<Card> getMinions() {
        return this.minions;
    }

    public ArrayList<Card> getSpells() {
        return this.spells;
    }

    public ArrayList<Card> getItems() {
        return this.items;
    }

    public ArrayList<Card> searchCollection(String cardName) throws InputException {
        ArrayList<Card> results = new ArrayList<>();
        searchInList(heroes, results, cardName);
        searchInList(minions, results, cardName);
        searchInList(spells, results, cardName);
        searchInList(items, results, cardName);
        if (results.size() == 0) {
            throw new InputException("Such card not found!");
        }
        return results;
    }

    private void searchInList(ArrayList<Card> list, ArrayList<Card> results, String cardName) {
        for (Card card : list) {
            if (card.areSame(cardName)) {
                results.add(card);
            }
        }
    }
}