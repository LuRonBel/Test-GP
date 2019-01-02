package com.patskevich.gpproject.service;

import com.patskevich.gpproject.converter.NicknameLogConverter;
import com.patskevich.gpproject.dto.NicknameLogDto;
import com.patskevich.gpproject.repository.NicknameLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NicknameLogService {
    private  final NicknameLogRepository nicknameLogRepository;
    private  final NicknameLogConverter nicknameLogConverter;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public NicknameLogDto createLog(final String login, final String oldNickname, final String newNickname){
        final NicknameLogDto nicknameLogDto = new NicknameLogDto();
        nicknameLogDto.setLogin(login);
        nicknameLogDto.setNewNickname(newNickname);
        nicknameLogDto.setOldNickname(oldNickname);
        nicknameLogDto.setDate(simpleDateFormat.format(new Date()));
        nicknameLogRepository.save(nicknameLogConverter.convertToDbo(nicknameLogDto));
        return nicknameLogDto;
    }

    public List<NicknameLogDto> getNicknameLog(){
        return nicknameLogRepository.findAllByOrderByIdDesc().stream().map(nicknameLogConverter::convertToDto).collect(Collectors.toList());
    }
}
