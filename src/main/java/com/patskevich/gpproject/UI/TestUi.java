package com.patskevich.gpproject.UI;

import com.patskevich.gpproject.UI.view.RoomView;
import com.patskevich.gpproject.UI.view.UsersView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "vaadin")
@PushStateNavigation
@SpringViewDisplay
public class TestUi extends UI implements ViewDisplay {

    private final HorizontalLayout mainLayout = new HorizontalLayout();
    private final Panel viewConteiner = new Panel();
    private final CssLayout menu = new CssLayout();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Label tittle = new Label("Menu");
        tittle.addStyleName(ValoTheme.MENU_ITEM);

        Button rooms = new Button("Rooms", e->{
            getUI().getNavigator().navigateTo(RoomView.NAME);
        });
        rooms.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.MENU_ITEM);
        Button users = new Button("Users", e->{
            getUI().getNavigator().navigateTo(UsersView.NAME);
        });
        users.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.MENU_ITEM);

        menu.addComponents(tittle,rooms,users);
        menu.addStyleName(ValoTheme.MENU_ROOT);
        menu.setWidth("20%");
        menu.setHeight("100%");
        viewConteiner.setSizeFull();
        viewConteiner.setWidth("1220");
        mainLayout.setHeight("100%");
        mainLayout.setWidthUndefined();
        mainLayout.addComponents(menu, viewConteiner);
        mainLayout.setComponentAlignment(viewConteiner, Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
    }

    @Override
    public void showView(View view) {
        viewConteiner.setContent((Component)view);
    }
}
