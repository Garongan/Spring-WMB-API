package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.entity.TransType;
import com.enigma.wmb_api_next.repository.TransTypeRepository;
import com.enigma.wmb_api_next.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepository transTypeRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransType getById(String id) {
        return transTypeRepository.findTransTypeEnumById(id.toUpperCase()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trans Type Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TransType> getAll() {
        return transTypeRepository.findAll();
    }
}
