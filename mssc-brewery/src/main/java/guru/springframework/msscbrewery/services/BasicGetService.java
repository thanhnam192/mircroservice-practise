package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.BasicGetDto;

import java.util.UUID;

public interface BasicGetService {
    BasicGetDto basicGet(String name, UUID uuid);
}
