package com.patskevich.gpproject.Service;
import com.patskevich.gpproject.converter.NicknameLogConverter;
import com.patskevich.gpproject.dto.NicknameLogDto;
import com.patskevich.gpproject.repository.NicknameLogRepository;
import com.patskevich.gpproject.service.NicknameLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class NicknameLogServiceTest {

    @InjectMocks
    private NicknameLogService ncknameLogService;

    @Mock
    private NicknameLogRepository nicknameLogRepository;

    @Mock
    private NicknameLogConverter nicknameLogConverter;

    @Test
    public void createLog(){

        final String login = new String("login");
        final String oldNickname = new String("old nickname");
        final String newNickname = new String("new nickname");

        final NicknameLogDto nicknameLogDto = ncknameLogService.createLog(login,oldNickname,newNickname);

        verify(nicknameLogRepository, times(1)).save(null);
        assertEquals(login, nicknameLogDto.getLogin());
        assertEquals(newNickname, nicknameLogDto.getNewNickname());
        assertEquals(oldNickname, nicknameLogDto.getOldNickname());
        assertEquals(new SimpleDateFormat("dd.MM.yyyy").format(new Date()), nicknameLogDto.getDate());
    }

    @Test
    public void getNicknameLog(){

        final List<NicknameLogDto> list = ncknameLogService.getNicknameLog();
        verify(nicknameLogRepository, times(1)).findAllByOrderByIdDesc();
    }
}
