package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.RoomDto;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.Room;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.mapper.RoomMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final QuestMapper questMapper;
    private final QuestRepository questRepository;

    public RoomDto createRoom(String title, Long id,String username) {
        Quest quest = questRepository.findById(id).orElseThrow(()->new RuntimeException("quest not found"));

        RoomDto roomDto = new RoomDto(null, title,false, questMapper.toDTO(quest), username);
        Room save = roomRepository.save(roomMapper.toEntity(roomDto, quest));
        roomDto.setId(save.getId());
        return roomDto;
    }

    public List<RoomDto> findAllById(Long id) {
        return roomRepository.findAllByQuest_Id(id).stream().map(roomMapper::toDTO).toList();
    }
}
