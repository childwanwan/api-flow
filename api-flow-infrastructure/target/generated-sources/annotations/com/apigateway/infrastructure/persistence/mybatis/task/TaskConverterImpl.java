package com.apigateway.infrastructure.persistence.mybatis.task;

import com.apigateway.domain.task.model.TaskEntity;
import com.apigateway.infrastructure.persistence.mybatis.task.entity.TaskDO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-03T22:34:16+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class TaskConverterImpl implements TaskConverter {

    @Override
    public TaskEntity toEntity(TaskDO taskDO) {
        if ( taskDO == null ) {
            return null;
        }

        TaskEntity.TaskEntityBuilder taskEntity = TaskEntity.builder();

        taskEntity.actionType( stringToActionType( taskDO.getActionType() ) );
        taskEntity.status( stringToTaskStatus( taskDO.getStatus() ) );
        taskEntity.compensateStatus( stringToCompensateStatus( taskDO.getCompensateStatus() ) );
        taskEntity.requestContext( stringToRequestContext( taskDO.getRequestContext() ) );
        taskEntity.execInfo( stringToExecInfo( taskDO.getExecInfo() ) );
        taskEntity.responseData( stringToResponseData( taskDO.getResponseData() ) );
        taskEntity.id( taskDO.getId() );
        taskEntity.taskNo( taskDO.getTaskNo() );
        taskEntity.source( taskDO.getSource() );
        taskEntity.groupNo( taskDO.getGroupNo() );
        taskEntity.apiCode( taskDO.getApiCode() );
        taskEntity.apiName( taskDO.getApiName() );
        taskEntity.interruptFlag( taskDO.getInterruptFlag() );
        taskEntity.compensateRetryCount( taskDO.getCompensateRetryCount() );
        taskEntity.compensateNextRetryTimeMs( taskDO.getCompensateNextRetryTimeMs() );
        taskEntity.priority( taskDO.getPriority() );
        taskEntity.expireTimeMs( taskDO.getExpireTimeMs() );
        taskEntity.receiptConfig( taskDO.getReceiptConfig() );
        taskEntity.receiptInfo( taskDO.getReceiptInfo() );
        taskEntity.retryCount( taskDO.getRetryCount() );
        taskEntity.maxRetryCount( taskDO.getMaxRetryCount() );
        taskEntity.nextRetryTimeMs( taskDO.getNextRetryTimeMs() );
        taskEntity.createTimeMs( taskDO.getCreateTimeMs() );
        taskEntity.startExecuteTimeMs( taskDO.getStartExecuteTimeMs() );
        taskEntity.endExecuteTimeMs( taskDO.getEndExecuteTimeMs() );
        taskEntity.executeDurationMs( taskDO.getExecuteDurationMs() );
        taskEntity.cancelTimeMs( taskDO.getCancelTimeMs() );
        taskEntity.cancelReason( taskDO.getCancelReason() );
        taskEntity.canceledBy( taskDO.getCanceledBy() );
        taskEntity.updateTimeMs( taskDO.getUpdateTimeMs() );
        taskEntity.deleted( taskDO.getDeleted() );
        taskEntity.version( taskDO.getVersion() );

        return taskEntity.build();
    }

    @Override
    public TaskDO toDO(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }

        TaskDO.TaskDOBuilder taskDO = TaskDO.builder();

        taskDO.actionType( actionTypeToString( taskEntity.getActionType() ) );
        taskDO.status( taskStatusToString( taskEntity.getStatus() ) );
        taskDO.compensateStatus( compensateStatusToString( taskEntity.getCompensateStatus() ) );
        taskDO.requestContext( requestContextToString( taskEntity.getRequestContext() ) );
        taskDO.execInfo( execInfoToString( taskEntity.getExecInfo() ) );
        taskDO.responseData( responseDataToString( taskEntity.getResponseData() ) );
        taskDO.id( taskEntity.getId() );
        taskDO.taskNo( taskEntity.getTaskNo() );
        taskDO.source( taskEntity.getSource() );
        taskDO.groupNo( taskEntity.getGroupNo() );
        taskDO.apiCode( taskEntity.getApiCode() );
        taskDO.apiName( taskEntity.getApiName() );
        taskDO.interruptFlag( taskEntity.getInterruptFlag() );
        taskDO.compensateRetryCount( taskEntity.getCompensateRetryCount() );
        taskDO.compensateNextRetryTimeMs( taskEntity.getCompensateNextRetryTimeMs() );
        taskDO.priority( taskEntity.getPriority() );
        taskDO.expireTimeMs( taskEntity.getExpireTimeMs() );
        taskDO.receiptConfig( taskEntity.getReceiptConfig() );
        taskDO.receiptInfo( taskEntity.getReceiptInfo() );
        taskDO.retryCount( taskEntity.getRetryCount() );
        taskDO.maxRetryCount( taskEntity.getMaxRetryCount() );
        taskDO.nextRetryTimeMs( taskEntity.getNextRetryTimeMs() );
        taskDO.createTimeMs( taskEntity.getCreateTimeMs() );
        taskDO.startExecuteTimeMs( taskEntity.getStartExecuteTimeMs() );
        taskDO.endExecuteTimeMs( taskEntity.getEndExecuteTimeMs() );
        taskDO.executeDurationMs( taskEntity.getExecuteDurationMs() );
        taskDO.cancelTimeMs( taskEntity.getCancelTimeMs() );
        taskDO.cancelReason( taskEntity.getCancelReason() );
        taskDO.canceledBy( taskEntity.getCanceledBy() );
        taskDO.updateTimeMs( taskEntity.getUpdateTimeMs() );
        taskDO.deleted( taskEntity.getDeleted() );
        taskDO.version( taskEntity.getVersion() );

        return taskDO.build();
    }
}
