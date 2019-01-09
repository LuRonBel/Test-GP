package com.patskevich.gpproject.UI;

import com.patskevich.gpproject.dto.NicknameChangeHistoryDto.NicknameChangeHistoryDto;
import com.patskevich.gpproject.entity.NicknameChangeHistory;
import com.patskevich.gpproject.service.UiService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

public class AdminForm extends FormLayout {
    private final MyUI myUI;
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final HorizontalLayout horizontalLayout = new HorizontalLayout();
    private final ComboBox<String> roomSelected = new ComboBox<>("Room:");
    private final ComboBox<String> userSelected = new ComboBox<>("User:");
    private final Label roomCount = new Label("Room count: ");
    private final Label userCount = new Label("User count: ");
    private final Label userRole = new Label("Role: ");
    private final Label userNickname = new Label("Nickname: ");
    private final Label log = new Label("LOG: ");
    private final TextField roomName = new TextField("Name");
    private final TextField roomDescription = new TextField("Description");
    private final TextField userName = new TextField("Name");
    private final TextField userPassword = new TextField("Password");
    private final Button deleteRoomButton = new Button("delete");
    private final Button updateRoomButton = new Button("update");
    private final Button createRoomButton = new Button("create");
    private final Button userNewButton = new Button("new");
    private final Button userDeleteButton = new Button("delete");
    private final Button userCreateButton = new Button("create");
    private final Button userChangeRoleButton = new Button("change role");
    private final Panel adminPanel = new Panel("Admin Panel");
    private final Grid<NicknameChangeHistoryDto> logGrid = new Grid<>(NicknameChangeHistoryDto.class);

    private UiService uiService;

    public AdminForm(MyUI myUI){
        this.myUI = myUI;
        setSizeUndefined();
        addRoomAdminPanel();
        addUserAdminPanel();
        logGrid.setSizeFull();
        logGrid.setColumns("userId","date","oldNickname","newNickname");
        verticalLayout.addComponent(log);
        horizontalLayout.addComponent(logGrid);
        verticalLayout.addComponent(horizontalLayout);
        adminPanel.setHeight("400");
        adminPanel.setContent(verticalLayout);
        this.addComponent(adminPanel);

    }

    private void addRoomAdminPanel(){
        roomSelected.addValueChangeListener(e->{
            if (!roomSelected.isEmpty()) {
                uiService.updateRoomInfo(roomSelected.getSelectedItem().get(), roomName, roomDescription);
            } else{
                uiService.updateLog(log, "");
                roomName.setValue("");
                roomDescription.setValue("");
            }
        });
        createRoomButton.addClickListener(e->{
            if (!roomName.isEmpty() && !roomDescription.isEmpty()) {
                uiService.updateLog(log, uiService.createRoom(roomName.getValue(), roomDescription.getValue()));
                uiService.updateRoomSelectedAdminPanel(roomSelected, roomCount);
                uiService.updateRoomSelected(myUI.roomSelected, myUI.auth.getName());
            } else uiService.updateLog(log,"Введите данные комнаты!");
        });
        deleteRoomButton.addClickListener(e->{
            if (!roomName.isEmpty()) {
                uiService.updateLog(log, uiService.deleteRoom(roomName.getValue()));
                uiService.updateRoomSelectedAdminPanel(roomSelected, roomCount);
                uiService.updateRoomSelected(myUI.roomSelected, myUI.auth.getName());
                uiService.updateGrid(myUI.auth.getName(), myUI.messageGrid);
            } else uiService.updateLog(log,"Выберите комнату!");
        });
        updateRoomButton.addClickListener(e->{
            if (!roomName.isEmpty() && !roomDescription.isEmpty()) {
                uiService.updateLog(log,uiService.updateRoom(roomName.getValue(), roomDescription.getValue()));
                uiService.updateRoomDecription(myUI.description, myUI.auth.getName());
            } else uiService.updateLog(log,"Не введены данные комнаты");
        });
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteRoomButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
        createRoomButton.setIcon(VaadinIcons.PLUS_CIRCLE);
        updateRoomButton.setIcon(VaadinIcons.COG);
        horizontalLayout.addComponents(deleteRoomButton,createRoomButton,updateRoomButton);
        verticalLayout.addComponents(roomSelected,roomCount,roomName,roomDescription,horizontalLayout);
        this.horizontalLayout.addComponent(verticalLayout);
    }

    private void addUserAdminPanel(){
        userSelected.addValueChangeListener(e->{
            if (!userSelected.isEmpty()) {
                uiService.updateUserInfoAdminPanel(userSelected.getSelectedItem().get(), userNickname, userRole);
            } else{
                uiService.updateLog(log, "");
                userNickname.setValue("Nickname: ");
                userRole.setValue("Role");
            }
        });
        userNewButton.addClickListener(e->{
            userName.setValue("");
            userPassword.setValue("");
            changeVisible();
        });
        userCreateButton.addClickListener(e->{
            uiService.updateLog(log, uiService.createUser(userName.getValue(), userPassword.getValue()));
            uiService.updateUserSelected(userSelected, userCount);
        });
        userDeleteButton.addClickListener(e->{
            if (!userSelected.isEmpty()){
                uiService.updateLog(log,uiService.deleteUser(userSelected.getValue()));
                uiService.updateUserSelected(userSelected,userCount);
                userSelected.setSelectedItem("root");
            } else uiService.updateLog(log, "Выберите пользователя!");
        });
        userChangeRoleButton.addClickListener(e->{
            if (!userSelected.isEmpty()){
                uiService.updateLog(log, uiService.changeRole(userSelected.getValue()));
                uiService.updateUserInfoAdminPanel(userSelected.getValue(), userNickname, userRole);
            } else uiService.updateLog(log, "Выберите пользователя!");
        });
        userName.setVisible(false);
        userPassword.setVisible(false);
        userCreateButton.setVisible(false);
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        userDeleteButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
        userNewButton.setIcon(VaadinIcons.PLUS_CIRCLE);
        userChangeRoleButton.setIcon(VaadinIcons.COG);
        userCreateButton.setIcon(VaadinIcons.USER_CHECK);
        horizontalLayout.addComponents(userDeleteButton, userChangeRoleButton, userNewButton);
        verticalLayout.addComponents(userSelected,userCount,userName,userPassword,userNickname,userCreateButton,userRole,horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_CENTER);
        verticalLayout.setComponentAlignment(userCount, Alignment.MIDDLE_LEFT);
        verticalLayout.setHeight("100%");
        verticalLayout.setComponentAlignment(userCreateButton, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(userName, Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(userPassword, Alignment.MIDDLE_CENTER);
        this.horizontalLayout.addComponents(verticalLayout);
    }

    public void setUiService(UiService uiService){
        this.uiService = uiService;
    }

    public void setV(Boolean visible){
        uiService.updateRoomSelectedAdminPanel(roomSelected, roomCount);
        uiService.updateUserSelected(userSelected, userCount);
        uiService.updateLogGrid(logGrid);
        this.setVisible(visible);
    }

    private void changeVisible(){
        if (userName.isVisible() && userPassword.isVisible() && userCreateButton.isVisible()){
            userName.setVisible(false);
            userPassword.setVisible(false);
            userCreateButton.setVisible(false);
            userSelected.setVisible(true);
            userNickname.setVisible(true);
            userRole.setVisible(true);
            userCount.setVisible(true);

        } else {
            userName.setVisible(true);
            userPassword.setVisible(true);
            userCreateButton.setVisible(true);
            userSelected.setVisible(false);
            userNickname.setVisible(false);
            userRole.setVisible(false);
            userCount.setVisible(false);
        }
    }
}