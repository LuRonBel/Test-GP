package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;

@ViewScope
public abstract class AbstractEditAddWindow<T> extends Window {

    protected T value;
    protected Binder<T> binder;
    protected final VerticalLayout mainLayout = new VerticalLayout();
    protected final FormLayout form = new FormLayout();
    protected final HorizontalLayout buttonsLayout = new HorizontalLayout();
    protected final Button saveButton = new Button(LanguageMessage.getText("save"));
    protected final Button cancelButton = new Button(LanguageMessage.getText("cancel"));

    public AbstractEditAddWindow(final String text){
        super(text);
        initWindow();
    }

    public AbstractEditAddWindow(){
        super();
        initWindow();
    }

    protected abstract void settingBinder();

    private void initWindow(){
        buttonsLayout.addComponents(saveButton, cancelButton);
        mainLayout.addComponents(form, buttonsLayout);
        saveButton.setIcon(VaadinIcons.SAFE);
        cancelButton.setIcon(VaadinIcons.CLOSE);
        this.setContent(mainLayout);
    }
}
