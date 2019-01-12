package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

public abstract class AbstractEditAddWindow<T> extends Window {

    protected  T value;
    protected Binder<T> binder;
    protected final VerticalLayout mainLayout = new VerticalLayout();
    protected final FormLayout form = new FormLayout();
    protected final HorizontalLayout buttonsLayout = new HorizontalLayout();
    protected final Button saveButton = new Button(LanguageMessage.getText("save"));
    protected final Button cancelButton = new Button(LanguageMessage.getText("cancel"));

    public AbstractEditAddWindow(final String text){
        super(text);
        buttonsLayout.addComponents(saveButton, cancelButton);
        mainLayout.addComponents(form, buttonsLayout);
        this.setContent(mainLayout);
    }

    public AbstractEditAddWindow(){
        super();
        buttonsLayout.addComponents(saveButton, cancelButton);
        mainLayout.addComponents(form, buttonsLayout);
        this.setContent(mainLayout);
    }

    protected abstract void settingBinder();
}
