package com.patskevich.gpproject.UI;

import com.patskevich.gpproject.UI.components.MainLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import lombok.AllArgsConstructor;

@SpringUI(path = "vaadin")
@PushStateNavigation
@SpringViewDisplay
@Theme("mytheme")
@UIScope
@AllArgsConstructor
public class TestUi extends UI implements ViewDisplay {

    private MainLayout mainLayout;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(mainLayout);
    }

    @Override
    public void showView(View view) {
        mainLayout.showView((Component)view);
    }
}
