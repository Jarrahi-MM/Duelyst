package server.dataCenter.models.account;

import server.Server;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;
import server.dataCenter.models.card.Deck;
import server.dataCenter.models.card.ExportedDeck;
import server.exceptions.ClientException;
import server.exceptions.LogicException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Collection {
    private List<Card> heroes = new ArrayList<>();
    private List<Card> minions = new ArrayList<>();
    private List<Card> spells = new ArrayList<>();
    private List<Card> items = new ArrayList<>();

    boolean hasCard(String cardId) {
        return hasCard(cardId, heroes) || hasCard(cardId, minions) || hasCard(cardId, spells) || hasCard(cardId, items);
    }

    private boolean hasCard(String cardId, List<Card> cards) {
        if (cardId == null || cards == null)
            return false;
        for (Card card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return true;
        }
        return false;
    }

    private ArrayList<Card> getCardsWithName(String cardName, List<Card> cards) {
        ArrayList<Card> cards1 = new ArrayList<>();
        if (cardName == null || cards == null)
            return cards1;
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(cardName))
                cards1.add(card);
        }
        return cards1;
    }

    public Card getCard(String cardId) {
        if (hasCard(cardId, heroes))
            return getCard(cardId, heroes);
        if (hasCard(cardId, minions))
            return getCard(cardId, minions);
        if (hasCard(cardId, spells))
            return getCard(cardId, spells);
        if (hasCard(cardId, items))
            return getCard(cardId, items);
        return null;
    }

    private Card getCard(String cardId, List<Card> cards) {
        for (Card card : cards) {
            if (card.getCardId().equalsIgnoreCase(cardId))
                return card;
        }
        return null;
    }

    void addCard(String cardName, Collection originalCards, String username) throws ClientException {//for account collections
        if (!originalCards.hasCard(cardName) || originalCards.getCard(cardName).getType() == CardType.COLLECTIBLE_ITEM) {
            Server.getInstance().serverPrint("Invalid CardName!");
            return;
        }
        int number = 1;
        String cardId = (username + "_" + cardName + "_").replaceAll(" ", "");
        while (hasCard(cardId + number))
            number++;
        Card newCard = new Card(originalCards.getCard(cardName), username, number);
        if (newCard.getType() == CardType.USABLE_ITEM && items.size() >= 3) {
            throw new ClientException("you can't have more than 3 items");
        }
        addCard(newCard);
    }

    public void addCard(Card card) {//for shop
        if (card == null) {
            Server.getInstance().serverPrint("Error!");
            return;
        }
        if (hasCard(card.getCardId())) {
            Server.getInstance().serverPrint("Error!");
            return;
        }
        switch (card.getType()) {
            case HERO:
                heroes.add(card);
                break;
            case MINION:
                minions.add(card);
                break;
            case SPELL:
                spells.add(card);
                break;
            case USABLE_ITEM:
            case COLLECTIBLE_ITEM:
                items.add(card);
                break;
            case FLAG:
                Server.getInstance().serverPrint("Error");
                break;
        }
    }

    public void removeCard(Card card) {
        heroes.remove(card);
        minions.remove(card);
        spells.remove(card);
        items.remove(card);
    }

    public Deck extractDeck(ExportedDeck exportedDeck) throws LogicException {
        Deck deck = new Deck(exportedDeck.getName());
        ArrayList<Card> hero = getCardsWithName(exportedDeck.getHeroName(), heroes);
        if (hero.isEmpty())
            throw new ClientException("you have not the hero");
        deck.addCard(hero.get(0));
        ArrayList<Card> item = getCardsWithName(exportedDeck.getItemName(), items);
        if (item.isEmpty())
            throw new ClientException("you have not the item");
        deck.addCard(item.get(0));
        for (Map.Entry<String, Integer> entry :
                exportedDeck.getOtherCards().entrySet()) {
            ArrayList<Card> cards = getCardsWithName(entry.getKey(), minions);
            cards.addAll(getCardsWithName(entry.getKey(), spells));
            if (cards.size() < entry.getValue())
                throw new ClientException("you have not enough cards (buy " + entry.getKey() + " from shop");
            for (int i = 0; i < entry.getValue(); i++) {
                deck.addCard(cards.get(i));
            }
        }
        return deck;
    }

    public List<Card> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    public List<Card> getMinions() {
        return Collections.unmodifiableList(minions);
    }

    public List<Card> getSpells() {
        return Collections.unmodifiableList(spells);
    }

    public List<Card> getItems() {
        return Collections.unmodifiableList(items);
    }
}