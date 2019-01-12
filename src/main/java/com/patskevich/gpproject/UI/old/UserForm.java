package com.patskevich.gpproject.UI.old;

import com.patskevich.gpproject.service.UiService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

public class UserForm extends FormLayout {

    private final MyUI myUI;
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final TextField name = new TextField("Nickname");
    private final Button update = new Button("Изменить");
    private final Label nickname = new Label("Nickname: ");
    private final Label log = new Label("LOG: ");
    private UiService uiService;

    public UserForm(final MyUI myUI){
        this.myUI = myUI;
        setSizeUndefined();
        update.addClickListener(e->{
           if (!name.isEmpty()){
               uiService.updateLog(log, uiService.changeNickname(myUI.auth.getName(), name.getValue()));
               uiService.updateGrid(myUI.auth.getName(), myUI.messageGrid);
               nickname.setValue("Nickname: "+uiService.getNickname(myUI.auth.getName()));
           } else uiService.updateLog(log,"Введите данные!");
        });
        update.setIcon(VaadinIcons.COG);
        verticalLayout.addComponents(name,nickname,update,log);
        verticalLayout.setComponentAlignment(update, Alignment.MIDDLE_CENTER);
        final Panel panel = new Panel("User settings :");
        panel.setHeight("400");
        panel.setContent(verticalLayout);
        this.addComponent(panel);
    }

    public void setUiService(final UiService uiService){
        this.uiService = uiService;
    }

    public void setV(final Boolean visible){
        uiService.updateLog(log,"");
        nickname.setValue("Nickname: "+uiService.getNickname(myUI.auth.getName()));
        name.setValue("");
        this.setVisible(visible);
    }
}