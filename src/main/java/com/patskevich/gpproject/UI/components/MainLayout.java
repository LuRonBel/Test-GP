package com.patskevich.gpproject.UI.components;

import com.patskevich.gpproject.UI.view.RoomView;
import com.patskevich.gpproject.UI.view.UsersView;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class MainLayout extends  HorizontalLayout{
    private final Panel viewConteiner = new Panel();
    private final CssLayout menu = new CssLayout();
    private UI myUi;

    @Autowired
    private SecuredViewAccessControl accessControl;

    protected MainLayout() {
        Label tittle = new Label(LanguageMessage.getText("menu"));
        tittle.addStyleName(ValoTheme.MENU_ITEM);

        Button rooms = new Button(LanguageMessage.getText("rooms"), e-> {
            if (accessControl.isAccessGranted(myUi, RoomView.NAME)) {
                getUI().getNavigator().navigateTo(RoomView.NAME);
            } else {
                Notification.show(LanguageMessage.getText("access.denied"), Notification.Type.WARNING_MESSAGE);
            }
        });
        rooms.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.MENU_ITEM);
        Button users = new Button(LanguageMessage.getText("users"), e-> {
            if (accessControl.isAccessGranted(myUi, UsersView.NAME)) {
                getUI().getNavigator().navigateTo(UsersView.NAME);
            } else {
                Notification.show(LanguageMessage.getText("access.denied"), Notification.Type.WARNING_MESSAGE);
            }
        });
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

    public void setMyUi(final UI ui){
        this.myUi = ui;
        getUI().getNavigator().navigateTo(RoomView.NAME);
    }

    public void showView(com.vaadin.ui.Component component) {
        viewConteiner.setContent(component);
    }
}
