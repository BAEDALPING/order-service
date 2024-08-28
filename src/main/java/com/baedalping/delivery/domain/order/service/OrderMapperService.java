package com.baedalping.delivery.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapperService {

    private final ModelMapper modelMapper;

    public <S, T> T convert(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
            .map(source -> modelMapper.map(source, targetClass))
            .collect(Collectors.toList());
    }
}

