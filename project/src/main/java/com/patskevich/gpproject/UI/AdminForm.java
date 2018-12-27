package com.patskevich.gpproject.UI;

import com.vaadin.ui.*;

public class AdminForm extends FormLayout {
    private MyUI myUI;
    private VerticalLayout verticalLayout = new VerticalLayout();

    public AdminForm(MyUI myUI){
        this.myUI = myUI;
        setSizeUndefined();
        ComboBox<String> comboBox = new ComboBox<>("Room:");
        comboBox.setItems("room1","room2","room3","room4","room5");
        Label label = new Label("kolvo komnat");
        TextField textField1 = new TextField("name");
        TextField textField2 = new TextField("description");
        Button button = new Button("delete");
        button.addClickListener(e-> setVisible(false));
        Button button1 = new Button("update");
        Button button2 = new Button("create");
        HorizontalLayout horizontalLayout = new HorizontalLayout(button,button1,button2);
        ComboBox<String> comboBox1 = new ComboBox<>("User:");
        comboBox1.setItems("user","user2","user3","user4","user5");
        Label label1 = new Label("Role");
        Button button3 = new Button("delete");
        Button button4 = new Button("change role");
        HorizontalLayout horizontalLayout1 = new HorizontalLayout(button3,button4);
        Label label2 = new Label("LOG:");
        verticalLayout.addComponents(comboBox,label,textField1,textField2,horizontalLayout,comboBox1,label1,horizontalLayout1,label2);
        Panel panel = new Panel("Admin Panel");
        panel.setHeight("400");
        panel.setContent(verticalLayout);

        this.addComponent(panel);

    }

    public void setV(){
        setVisible(true);
    }
}
