package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.UI.customField.RoomField;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ViewScope
public class AddUserWindow extends AbstractEditAddWindow<CreateUserDtoUi> {

    private final List<String> rooms = new ArrayList<>();
    private final TextField userLoginField = new TextField(LanguageMessage.getText("login"));
    private final TextField userNicknameField = new TextField(LanguageMessage.getText("nickname"));
    private final TextField userPasswordFiled = new TextField(LanguageMessage.getText("password"));
    private final RoomField roomField;
    private final RadioButtonGroup<String> roleRadioButton = new RadioButtonGroup<>(LanguageMessage.getText("role.select"));

    public AddUserWindow(final UserService userService, final RoomService roomService){
        super(LanguageMessage.getText("add.user"));
        binder = new Binder<>();
        value = new CreateUserDtoUi();
        fillRoomsList(roomService);
        roomField = new RoomField(roomService,LanguageMessage.getText("rooms"), rooms);
        roleRadioButton.setItems(RoleEnum.ROLE_ADMIN.toString(),RoleEnum.ROLE_USER.toString());
        form.addComponents(userLoginField, userNicknameField, userPasswordFiled, roomField, roleRadioButton);
        settingBinder();
        addEventListener(userService);
    }

    @Override
    protected void settingBinder() {
        binder.readBean(value);
        binder.forField(userLoginField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 10))
                .bind(CreateUserDtoUi::getLogin, CreateUserDtoUi::setLogin);
        binder.forField(userNicknameField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 10))
                .bind(CreateUserDtoUi::getNickname, CreateUserDtoUi::setNickname);
        binder.forField(userPasswordFiled)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 10))
                .bind(CreateUserDtoUi::getPassword, CreateUserDtoUi::setPassword);
        binder.forField(roomField)
                .withValidator(room -> !(room==null), LanguageMessage.getText("not.null"))
                .bind(CreateUserDtoUi::getRoom, CreateUserDtoUi::setRoom);
        binder.forField(roleRadioButton)
                .withValidator(role-> !(role==null), LanguageMessage.getText("not.null"))
                .bind(CreateUserDtoUi::getRole, CreateUserDtoUi::setRole);
    }

    private void addEventListener(final UserService userService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.isValid()) {
                    binder.writeBean(value);
                    Notification.show(userService.createUser(value),
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
            Notification.show(LanguageMessage.getText("cancel"),
                    Notification.Type.WARNING_MESSAGE);
            this.close();
        });
    }

    private void fillRoomsList(final RoomService roomService) {
        roomService.getRoomList(null)
                .forEach(roomDto -> rooms.add(roomDto.getName()));
    }
}
