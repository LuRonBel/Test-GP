package com.patskevich.gpproject.UI;

import com.vaadin.ui.*;

public class UserForm extends FormLayout {
    private MyUI myUI;
    private VerticalLayout verticalLayout = new VerticalLayout();

    public UserForm(MyUI myUI){
        this.myUI = myUI;
        setSizeUndefined();
        TextField textField1 = new TextField("name");
        Label label = new Label("Role");
        TextField textField2 = new TextField("password");
        TextField textField3 = new TextField("new password");
        Button button = new Button("update");
        button.addClickListener(e-> setVisible(false));
        Label label2 = new Label("LOG:");
        verticalLayout.addComponents(textField1,label,textField2,textField3,button,label2);
        Panel panel = new Panel("User Panel");
        panel.setHeight("400");
        panel.setContent(verticalLayout);
        this.addComponent(panel);
    }

    public void setV(){
        setVisible(true);
    }
}

