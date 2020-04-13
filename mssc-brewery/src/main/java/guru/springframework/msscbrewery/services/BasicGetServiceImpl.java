package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.BasicGetDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasicGetServiceImpl implements BasicGetService {
    @Override
    public BasicGetDto basicGet(String name, UUID uuid) {
        return BasicGetDto.builder().id(uuid).name(name).build();
    }
}
