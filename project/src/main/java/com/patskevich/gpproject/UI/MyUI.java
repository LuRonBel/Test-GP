package com.patskevich.gpproject.UI;

import com.patskevich.gpproject.dto.MessageDto.MessageOutputDto;
import com.patskevich.gpproject.service.LogoutService;
import com.patskevich.gpproject.service.UiService;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.spring.annotation.SpringUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringUI
@Theme("mytheme")
public class MyUI extends UI {
    private AdminForm adminForm = new AdminForm(this);
    private UserForm userForm = new UserForm(this);
    public Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final VerticalLayout mainLayout = new VerticalLayout();
    final Label userInfo = new Label();
    final Label log = new Label("LOG: ");
    final Label description = new Label();
    final ComboBox<String> roomSelected = new ComboBox<>("Room:");
    final Grid<MessageOutputDto> messageGrid = new Grid<>(MessageOutputDto.class);
    final TextField inputTextField = new TextField();

    @Autowired
    private UiService uiService;

    @Autowired
    private LogoutService logoutService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout.setWidth("100%");
        setContent(mainLayout);
        adminForm.setVisible(false);
        userForm.setVisible(false);
        setHeightUndefined();
        addMainMenu();
        addSelectedRoom();
        addInputTextLayout();
        addGridPanel();
    }

    private void addMainMenu() {
        final MenuBar menuBar = new MenuBar();
        final  MenuBar.MenuItem admin = menuBar.addItem("admin");
        final  MenuBar.MenuItem settings = menuBar.addItem("settings");
        final MenuBar.MenuItem logout = menuBar.addItem("logout");
        admin.setIcon(VaadinIcons.NURSE);
        settings.setIcon(VaadinIcons.OPTIONS);
        logout.setIcon(VaadinIcons.EXIT);
        admin.setCommand(e -> {
            if (adminForm.isVisible()) {
                adminForm.setVisible(false);
                userForm.setVisible(false);
                messageGrid.setVisible(true);
            } else {
                adminForm.setUiService(uiService);
                adminForm.setV(true);
                messageGrid.setVisible(false);
                userForm.setVisible(false);
            }
        });
        logout.setCommand(e -> {
            uiService.updateLog(log, "На данный момент эта функция недоступна");
        });
        settings.setCommand(e -> {
            if (userForm.isVisible()) {
                userForm.setVisible(false);
                messageGrid.setVisible(true);
                adminForm.setVisible(false);
            } else {
                userForm.setUiService(uiService);
                userForm.setV(true);
                messageGrid.setVisible(true);
                adminForm.setVisible(false);
            }
        });
        if (uiService.getUserRole(auth.getName()).equals("ROLE_ADMIN"))
            admin.setVisible(true);
        else admin.setVisible(false);
        mainLayout.addComponent(menuBar);
        mainLayout.setComponentAlignment(menuBar, Alignment.BOTTOM_RIGHT);
    }

    private void addSelectedRoom() {
        final  HorizontalLayout infoAndLog = new HorizontalLayout();
        final  HorizontalLayout selectedRoom = new HorizontalLayout();
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        uiService.updateUserInfo(userInfo, auth.getName());
        uiService.updateRoomSelected(roomSelected, auth.getName());
        roomSelected.addValueChangeListener(e -> {
            if (!roomSelected.isEmpty()) {
                uiService.updateLog(log, uiService.changeRoom(roomSelected.getSelectedItem().get(), auth.getName()));
                uiService.updateGrid(auth.getName(), messageGrid);
                uiService.updateRoomDecription(description, auth.getName());
            } else uiService.updateLog(log, "");
        });
        uiService.updateRoomDecription(description, auth.getName());
        infoAndLog.addComponents(userInfo, log);
        selectedRoom.addComponents(roomSelected, description);
        selectedRoom.setComponentAlignment(description, Alignment.BOTTOM_LEFT);
        verticalLayout.addComponents(infoAndLog, selectedRoom);
        mainLayout.addComponents(verticalLayout);
    }

    private void addInputTextLayout() {
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        final Button inputTextButton = new Button("Отправить");
        inputTextButton.setIcon(VaadinIcons.ENVELOPE);
        inputTextButton.addClickListener(e -> {
            if (!inputTextField.getValue().equals("")) {
                uiService.updateLog(log, uiService.addMessage(inputTextField.getValue(), auth.getName()));
                uiService.updateGrid(auth.getName(), messageGrid);
            } else uiService.updateLog(log, "Введите текст!");
        });
        inputTextButton.setWidth("100%");
        inputTextField.setWidth("1143");
        horizontalLayout.addComponents(inputTextButton, inputTextField);
        horizontalLayout.setExpandRatio(inputTextButton, 1);
        horizontalLayout.setWidthUndefined();
        mainLayout.addComponents(horizontalLayout);
    }

    private void addGridPanel() {
        messageGrid.setColumns("date","author", "message");
        messageGrid.setFrozenColumnCount(1);
        messageGrid.getColumn("author").setMinimumWidth(150);
        messageGrid.getColumn("author").setWidth(150);
        uiService.updateGrid(auth.getName(), messageGrid);
        adminForm.setMargin(false);
        userForm.setMargin(false);
        final HorizontalLayout gridPanel = new HorizontalLayout(messageGrid, adminForm, userForm);
        gridPanel.setSizeFull();
        messageGrid.setSizeFull();
        gridPanel.setExpandRatio(messageGrid, 1);
        gridPanel.setExpandRatio(adminForm, 1);
        gridPanel.setComponentAlignment(adminForm, Alignment.MIDDLE_CENTER);
        mainLayout.addComponents(gridPanel);
    }

}
