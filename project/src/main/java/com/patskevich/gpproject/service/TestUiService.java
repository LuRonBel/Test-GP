package com.patskevich.gpproject.service;

import com.patskevich.gpproject.entity.Room;
import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.repository.RoomRepository;
import com.patskevich.gpproject.repository.UserRepository;
import com.vaadin.ui.Grid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TestUiService {

    private RoomRepository roomRepository;
    private UserRepository userRepository;

    public List<Room> updateRoomGriid(){
        return roomRepository.findAll();
    }

    public List<User> updateUserGriid(){
        return userRepository.findAll();
    }

}
