package models.menus;

public class BattleMenu extends Menu {
    private static final BattleMenu BATTLE_MENU = new BattleMenu();

    private BattleMenu() {

    }

    public static BattleMenu getInstance() {
        return BATTLE_MENU;
    }

    public void moveToMenu(String menuName) {

    }
}