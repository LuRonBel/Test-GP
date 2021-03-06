package com.patskevich.gpproject.UI.window;

import com.patskevich.gpproject.UI.customField.RoomField;
import com.patskevich.gpproject.configuration.LanguageMessage;
import com.patskevich.gpproject.dto.CreateUserDtoUi;
import com.patskevich.gpproject.dto.UserDto;
import com.patskevich.gpproject.entity.RoleEnum;
import com.patskevich.gpproject.service.RoomService;
import com.patskevich.gpproject.service.UserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("EditUserWindow")
@Secured("ROLE_ADMIN")
@UIScope
public class EditUserWindow extends AbstractEditAddWindow<CreateUserDtoUi> {

    private final List<String> rooms = new ArrayList<>();
    private final TextField userLoginField = new TextField(LanguageMessage.getText("login"));
    private final TextField userNewLoginField = new TextField(LanguageMessage.getText("new.login"));
    private final TextField userNicknameField = new TextField(LanguageMessage.getText("nickname"));
    private final TextField userPasswordField = new TextField(LanguageMessage.getText("password"));
    private final RoomField roomField;
    private final RadioButtonGroup<String> roleRadioButton = new RadioButtonGroup<>(LanguageMessage.getText("role.select"));

    public EditUserWindow(final UserService userService,final RoomService roomService){
        super(LanguageMessage.getText("edit.user"));
        binder = new Binder<>();
        roleRadioButton.setItems(RoleEnum.ROLE_ADMIN.toString(),RoleEnum.ROLE_USER.toString());
        fillRoomsList(roomService);
        roomField = new RoomField(roomService,LanguageMessage.getText("rooms"), rooms);
        userLoginField.setEnabled(false);
        form.addComponents(userLoginField,
                userNewLoginField,
                userNicknameField,
                userPasswordField,
                roomField,
                roleRadioButton);
        settingBinder();
        addEventListener(roomService, userService);
    }

    @Override
    protected void settingBinder() {
        binder.forField(userNewLoginField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 10))
                .bind(CreateUserDtoUi::getLogin, CreateUserDtoUi::setLogin);
        binder.forField(userNicknameField)
                .withValidator(new StringLengthValidator(
                        LanguageMessage.getText("not.null"),
                        1, 10))
                .bind(CreateUserDtoUi::getNickname, CreateUserDtoUi::setNickname);
        binder.forField(userPasswordField)
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
        binder.readBean(value);
    }

    private void addEventListener(final RoomService roomService, final UserService userService) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.isValid()) {
                    binder.writeBean(value);
                    Notification.show(userService.updateUserUi(value, userLoginField.getValue()),
                            Notification.Type.HUMANIZED_MESSAGE);
                    this.close();
                } else{
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

    public void setBean(final UserDto userDto, final UserService userService){
        this.value = createUserDtoUi(userDto, userService);
        userLoginField.setValue(userDto.getLogin());
        binder.readBean(value);
    }

    private void fillRoomsList(final RoomService roomService) {
        roomService.getRoomList(null)
                .forEach(roomDto -> rooms.add(roomDto.getName()));
    }

    private CreateUserDtoUi createUserDtoUi(final UserDto userDto, final UserService userService){
        final CreateUserDtoUi user = new CreateUserDtoUi();
        user.setLogin(userDto.getLogin());
        user.setNickname(userDto.getNickname());
        user.setPassword("");
        user.setRoom(userService.getUserRepository().findByLogin(userDto.getLogin()).getRoom());
        user.setRole(userDto.getRole());
        return user;
    }
}
