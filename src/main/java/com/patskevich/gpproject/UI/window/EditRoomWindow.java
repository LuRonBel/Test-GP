package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.RoomDto;
import com.patskevich.gpproject.service.RoomService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component("EditRoomWindow")
@Secured("ROLE_ADMIN")
@UIScope
public class EditRoomWindow extends AbstractEditAddWindow<RoomDto> {

    private final TextField roomNameField = new TextField(LanguageMessage.getText("name"));
    private final TextField roomDescriptionField = new TextField(LanguageMessage.getText("new.description"));

    public EditRoomWindow(final RoomService roomService){
        super(LanguageMessage.getText("edit.room"));
        binder = new Binder<>();
        roomNameField.setEnabled(false);
        form.addComponents(roomNameField, roomDescriptionField);
        settingBinder();
        addEventListener(roomService);
    }

    @Override
    protected void settingBinder() {
        binder.forField(roomNameField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                1, 10))
                .bind(RoomDto::getName, RoomDto::setName);
        binder.forField(roomDescriptionField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 20))
                .bind(RoomDto::getDescription, RoomDto::setDescription);
        binder.readBean(value);
    }

    public void setBean(final RoomDto roomDto){
        this.value = roomDto;
        binder.readBean(value);
    }

    protected void addEventListener(final RoomService roomService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.isValid()) {
                    binder.writeBean(value);
                    Notification.show(roomService.updateRoom(value),
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
