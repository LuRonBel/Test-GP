package com.patskevich.gpproject.UI.components;

import com.patskevich.gpproject.UI.view.RoomView;
import com.patskevich.gpproject.UI.view.UsersView;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

@Component
public class MainLayout extends  HorizontalLayout{
    private final Panel viewConteiner = new Panel();
    private final CssLayout menu = new CssLayout();

    protected MainLayout() {
        Label tittle = new Label(LanguageMessage.getText("menu"));
        tittle.addStyleName(ValoTheme.MENU_ITEM);

        Button rooms = new Button(LanguageMessage.getText("rooms"), e-> getUI().getNavigator().navigateTo(RoomView.NAME));
        rooms.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.MENU_ITEM);
        Button users = new Button(LanguageMessage.getText("users"), e-> getUI().getNavigator().navigateTo(UsersView.NAME));
        users.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.MENU_ITEM);

        menu.addComponents(tittle,rooms,users);
        menu.addStyleName(ValoTheme.MENU_ROOT);
        menu.setWidth("20%");
        menu.setHeight("100%");
        viewConteiner.setSizeFull();
        viewConteiner.setWidth("1220");
        this.setHeight("100%");
        this.setWidthUndefined();
        this.addComponents(menu, viewConteiner);
        this.setComponentAlignment(viewConteiner, Alignment.MIDDLE_CENTER);
    }

    public void showView(com.vaadin.ui.Component component) {
        viewConteiner.setContent(component);
    }
}
