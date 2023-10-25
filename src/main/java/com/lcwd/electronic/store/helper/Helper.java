package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.dtoResponseHelper.PagebleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static<U , V> PagebleResponse<V> getPageableResponse(Page<U> page , Class<V> type){
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object ,type)).collect(Collectors.toList());

        PagebleResponse<V> response = new PagebleResponse<V>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPage(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}
