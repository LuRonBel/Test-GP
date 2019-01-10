package com.patskevich.gpproject.UI.old;


import com.patskevich.gpproject.service.UiService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
public class UserForm extends FormLayout {

    private MyUI myUI;
    private VerticalLayout verticalLayout = new VerticalLayout();
    private TextField name = new TextField("Nickname");
    private Button update = new Button("Изменить");
    private Label nickname = new Label("Nickname: ");
    private Label log = new Label("LOG: ");

    private UiService uiService;

    public UserForm(MyUI myUI){
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

    public void setUiService(UiService uiService){
        this.uiService = uiService;
    }

    public void setV(final Boolean visible){
        uiService.updateLog(log,"");
        nickname.setValue("Nickname: "+uiService.getNickname(myUI.auth.getName()));
        name.setValue("");
        this.setVisible(visible);
    }

}