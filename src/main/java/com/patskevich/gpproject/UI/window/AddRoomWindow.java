package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import org.springframework.stereotype.Component;

@Component
public class AddRoomWindow extends  AbstractEditAddWindow<RoomDto> {

    private final TextField roomNameField = new TextField(LanguageMessage.getText("name"));
    private final TextField roomDescriptionFiled = new TextField(LanguageMessage.getText("description"));

    public AddRoomWindow(final RoomService roomService){
        super(LanguageMessage.getText("add.room"));
        binder = new Binder<>();
        value = new RoomDto();
        form.addComponent(roomNameField);
        form.addComponent(roomDescriptionFiled);
        settingBinder();
        addEventListener(roomService);
    }

    @Override
    protected void settingBinder() {
        binder.readBean(value);
        binder.forField(roomNameField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                       1, 10))
                .bind(RoomDto::getName, RoomDto::setName);
        binder.forField(roomDescriptionFiled)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 20))
                .bind(RoomDto::getDescription, RoomDto::setDescription);
    }

    private void addEventListener(final RoomService roomService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.isValid()) {
                    binder.writeBean(value);
                    Notification.show(roomService.createRoom(value),
                            Notification.Type.HUMANIZED_MESSAGE);
                    this.close();
                } else {
                    Notification.show(LanguageMessage.getText("valid.error"),
                            Notification.Type.ERROR_MESSAGE);
                }
            } catch (ValidationException e) {
                Notification.show(LanguageMessage.getText("wrong.value"),
                        Notification.Type.ERROR_MESSAGE);
            }
        });
        cancelButton.addClickListener(clickEvent -> {
            binder.readBean(value);
            Notification.show(LanguageMessage.getText("cancel"),
                    Notification.Type.WARNING_MESSAGE);
            this.close();
        });
    }
}
