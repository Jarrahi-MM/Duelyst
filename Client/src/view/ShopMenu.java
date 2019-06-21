package view;

import controller.ShopController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.account.Collection;
import models.gui.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.*;

public class ShopMenu extends Show implements PropertyChangeListener {
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> new MainMenu().show();
    private static final Font TITLE_FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 45 * SCALE);
    private static final double SCROLL_WIDTH = 2350 * SCALE;
    private static final double SCROLL_HEIGHT = SCENE_HEIGHT - DEFAULT_SPACING * 13;
    private VBox cardsBox;
    private Collection showingCards;

    ShopMenu() {
        setOriginalCards();

        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);

            BorderPane background = BackgroundMaker.getMenuBackground();
            BackButton backButton = new BackButton(BACK_EVENT);

            VBox shopPane = new VBox(UIConstants.DEFAULT_SPACING * 4);
            shopPane.setPadding(new Insets(UIConstants.DEFAULT_SPACING * 3));
            shopPane.setAlignment(Pos.CENTER);
            shopPane.setMinSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);
            shopPane.setMaxSize(UIConstants.SCENE_WIDTH, UIConstants.SCENE_HEIGHT);

            HBox searchBox = new SearchBox();

            DefaultLabel heroesLabel = new DefaultLabel("HEROES", TITLE_FONT, Color.WHITE);
            CardsGrid heroesGrid = new CardsGrid(showingCards.getHeroes(), true);

            DefaultLabel minionsLabel = new DefaultLabel("MINIONS", TITLE_FONT, Color.WHITE);
            CardsGrid minionsGrid = new CardsGrid(showingCards.getMinions(), true);

            DefaultLabel spellsLabel = new DefaultLabel("SPELLS", TITLE_FONT, Color.WHITE);
            CardsGrid spellsGrid = new CardsGrid(showingCards.getSpells(), true);

            DefaultLabel itemsLabel = new DefaultLabel("ITEMS", TITLE_FONT, Color.WHITE);
            CardsGrid itemsGrid = new CardsGrid(showingCards.getItems(), true);

            cardsBox = new VBox(UIConstants.DEFAULT_SPACING * 4,
                    heroesLabel, heroesGrid, minionsLabel, minionsGrid, spellsLabel, spellsGrid, itemsLabel, itemsGrid
            );
            cardsBox.setMinSize(SCROLL_WIDTH * 0.95, SCROLL_HEIGHT * 0.95);
            cardsBox.setAlignment(Pos.TOP_CENTER);

            ScrollPane cardsScroll = new ScrollPane(cardsBox);
            cardsScroll.setMinSize(SCROLL_WIDTH, SCROLL_HEIGHT);
            cardsScroll.setMaxWidth(SCROLL_WIDTH);
            cardsScroll.setId("background_transparent");

            shopPane.getChildren().addAll(searchBox, cardsScroll);

            AnchorPane sceneContents = new AnchorPane(background, shopPane, backButton);

            root.getChildren().addAll(sceneContents);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setOriginalCards() {
        ShopController.getInstance().addPropertyChangeListener(this);
        synchronized (ShopController.getInstance()) {
            if (ShopController.getInstance().getShowingCards() == null) {
                try {
                    ShopController.getInstance().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        showingCards = ShopController.getInstance().getShowingCards();
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("search_result")) {
            showingCards = (Collection) evt.getNewValue();
            try {
                cardsBox.getChildren().set(1, new CardsGrid(showingCards.getHeroes(), true));
                cardsBox.getChildren().set(3, new CardsGrid(showingCards.getMinions(), true));
                cardsBox.getChildren().set(5, new CardsGrid(showingCards.getSpells(), true));
                cardsBox.getChildren().set(7, new CardsGrid(showingCards.getItems(), true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
