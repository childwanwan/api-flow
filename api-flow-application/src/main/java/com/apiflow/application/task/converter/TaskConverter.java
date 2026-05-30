package com.apiflow.application.task.converter;

import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.application.task.dto.*;
import com.apiflow.domain.task.model.ExecInfo;
import com.apiflow.domain.task.model.ReceiptConfig;
import com.apiflow.domain.task.model.ReceiptInfo;
import com.apiflow.domain.task.model.RequestContext;
import com.apiflow.domain.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskConverter {

    TaskConverter INSTANCE = Mappers.getMapper(TaskConverter.class);

    @Mapping(target = "requestContext", qualifiedByName = "toRequestContextDTO")
    @Mapping(target = "execInfo", qualifiedByName = "toExecInfoDTO")
    @Mapping(target = "receiptConfig", qualifiedByName = "toReceiptConfigDTO")
    @Mapping(target = "receiptInfo", qualifiedByName = "toReceiptInfoDTO")
    TaskDTO toDTO(Task task);

    @Named("toRequestContextDTO")
    default RequestContextDTO toRequestContextDTO(RequestContext context) {
        if (context == null) return null;
        return RequestContextDTO.builder()
                .params(context.getParams())
                .customData(context.getCustomData())
                .build();
    }

    @Named("toExecInfoDTO")
    default ExecInfoDTO toExecInfoDTO(ExecInfo info) {
        if (info == null) return null;
        return ExecInfoDTO.builder()
                .totalCostTimeMs(info.getTotalCostTimeMs())
                .retryCount(info.getRetryCount())
                .build();
    }

    @Named("toReceiptConfigDTO")
    default ReceiptConfigDTO toReceiptConfigDTO(ReceiptConfig config) {
        if (config == null) return null;
        return ReceiptConfigDTO.builder()
                .receiptTypes(config.getReceiptTypes())
                .build();
    }

    @Named("toReceiptInfoDTO")
    default ReceiptInfoDTO toReceiptInfoDTO(ReceiptInfo info) {
        if (info == null) return null;
        return ReceiptInfoDTO.builder().build();
    }

    TaskDTO toDTOFromIDTO(TaskIDTO idto);

    List<TaskDTO> toDTOListFromIDTO(List<TaskIDTO> list);

    default ReceiptConfig toReceiptConfig(ReceiptConfigDTO dto) {
        if (dto == null) return null;
        return new ReceiptConfig(dto.getReceiptTypes(), null, null);
    }
}
