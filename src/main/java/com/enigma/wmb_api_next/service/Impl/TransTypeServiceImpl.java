package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.response.TransTypeResponse;
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
    public TransTypeResponse getById(String id) {
        TransType transType = transTypeRepository.findTransTypeEnumById(id.toUpperCase()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trans Type Not Found"));
        return getResponse(transType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TransTypeResponse> getAll() {
        List<TransType> transTypes = transTypeRepository.findAll();
        return transTypes.stream().map(TransTypeServiceImpl::getResponse).toList();
    }

    private static TransTypeResponse getResponse(TransType transType) {
        return TransTypeResponse.builder()
                .id(transType.getId().name())
                .description(transType.getDescription())
                .build();
    }
}
