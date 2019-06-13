package models.gui;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;

public class PlayButtonItem {
    final String title;
    final String description;
    final EventHandler<? super MouseEvent> event;
    ImageView imageView;

    public PlayButtonItem(String url, String title, String description, EventHandler<? super MouseEvent> event) {
        try {
            this.imageView = ImageLoader.loadImage(url, UIConstants.PLAY_MENU_BUTTON_WIDTH, UIConstants.PLAY_MENU_BUTTON_HEIGHT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.title = title;
        this.description = description;
        this.event = event;
    }
}