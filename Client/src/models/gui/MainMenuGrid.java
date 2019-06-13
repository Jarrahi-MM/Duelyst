package models.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import view.PlayMenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class MainMenuGrid extends GridPane {
    private Image menuItemImage;
    private Image hoverRing;

    MainMenuGrid(MenuItem[] items) throws FileNotFoundException {
        setVgap(UIConstants.DEFAULT_SPACING * 3);
        setHgap(UIConstants.DEFAULT_SPACING * 3);

        menuItemImage = new Image(new FileInputStream("resources/ui/menu_item.png"));
        hoverRing = new Image(new FileInputStream("resources/ui/glow_ring.png"));

        for (MenuItem item : items) {
            Node[] row = makeRow(item);
            addRow(item.index, row);
        }
    }

    private Node[] makeRow(MenuItem item) {
        HintBox textWrapper = new HintBox(item.hint);
        ImageView menuView = ImageLoader.makeImageView(
                menuItemImage, UIConstants.MENU_ITEM_IMAGE_SIZE, UIConstants.MENU_ITEM_IMAGE_SIZE
        );
        ImageView ringView = ImageLoader.makeImageView(
                hoverRing, UIConstants.MENU_ITEM_IMAGE_SIZE, UIConstants.MENU_ITEM_IMAGE_SIZE
        );
        ringView.setVisible(false);
        RotateAnimation rotate = new RotateAnimation(ringView);
        MenuLabel label = new MenuLabel(item.title);

        label.setOnMouseClicked(item.event);

        label.setOnMouseEntered(event -> {
            menuView.setOpacity(0.6);
            ringView.setVisible(true);
            label.setCursor(UIConstants.SELECT_CURSOR);
            label.setEffect(UIConstants.WHITE_TEXT_SHADOW);
            rotate.play();
            textWrapper.setVisible(true);
        });

        label.setOnMouseExited(event -> {
            menuView.setOpacity(1);
            ringView.setVisible(false);
            label.setCursor(UIConstants.DEFAULT_CURSOR);
            label.setEffect(null);
            rotate.pause();
            textWrapper.setVisible(false);
        });

        return new Node[]{textWrapper, new StackPane(menuView, ringView), label};
    }
}