package com.apiflow.interfaces.biz.alarm;

import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.application.alarm.AlarmApplicationService;
import com.apiflow.application.alarm.param.CreateAlarmRuleParam;
import com.apiflow.application.alarm.param.DeleteAlarmRuleParam;
import com.apiflow.application.alarm.param.ListAlarmRecordParam;
import com.apiflow.application.alarm.param.ListAlarmRuleParam;
import com.apiflow.application.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.alarm.converter.AlarmConverter;
import com.apiflow.interfaces.biz.alarm.request.AlarmRuleCreateRequest;
import com.apiflow.interfaces.biz.alarm.request.AlarmRuleUpdateRequest;
import com.apiflow.interfaces.biz.alarm.vo.AlarmRecordVO;
import com.apiflow.interfaces.biz.alarm.vo.AlarmRuleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {

    private static final AlarmConverter ALARM_CONVERTER = AlarmConverter.INSTANCE;

    private final AlarmApplicationService alarmApplicationService;

    @GetMapping("/rule/list")
    public Result<List<AlarmRuleVO>> listRules(
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) Boolean enabled) {
        ListAlarmRuleParam param = ListAlarmRuleParam.builder()
                .alarmType(alarmType)
                .enabled(enabled)
                .build();
        List<AlarmRuleIDTO> rules = alarmApplicationService.listRules(param);
        return Result.success(ALARM_CONVERTER.toRuleVOList(rules));
    }

    @PostMapping("/rule/create")
    public Result<AlarmRuleVO> createRule(@RequestBody AlarmRuleCreateRequest request) {
        if (request.getRuleName() == null || request.getAlarmType() == null) {
            return Result.fail("PARAM_ERROR", "规则名称和告警类型不能为空", null);
        }
        CreateAlarmRuleParam param = CreateAlarmRuleParam.builder()
                .ruleName(request.getRuleName())
                .alarmType(request.getAlarmType())
                .triggerCondition(request.getTriggerCondition())
                .level(request.getLevel())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();
        AlarmRuleIDTO rule = alarmApplicationService.createRule(param);
        return Result.success(ALARM_CONVERTER.toRuleVO(rule));
    }

    @PutMapping("/rule/update")
    public Result<AlarmRuleVO> updateRule(@RequestBody AlarmRuleUpdateRequest request) {
        if (request.getId() == null) {
            return Result.fail("PARAM_ERROR", "规则ID不能为空", null);
        }
        UpdateAlarmRuleParam param = UpdateAlarmRuleParam.builder()
                .id(request.getId())
                .ruleName(request.getRuleName())
                .alarmType(request.getAlarmType())
                .triggerCondition(request.getTriggerCondition())
                .level(request.getLevel())
                .enabled(request.getEnabled())
                .build();
        AlarmRuleIDTO rule = alarmApplicationService.updateRule(param);
        return Result.success(ALARM_CONVERTER.toRuleVO(rule));
    }

    @DeleteMapping("/rule/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {
        DeleteAlarmRuleParam param = DeleteAlarmRuleParam.builder()
                .id(id)
                .build();
        alarmApplicationService.deleteRule(param);
        return Result.success();
    }

    @GetMapping("/record/list")
    public Result<List<AlarmRecordVO>> listRecords(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        ListAlarmRecordParam param = ListAlarmRecordParam.builder()
                .eventType(eventType)
                .level(level)
                .startTimeMs(startTimeMs)
                .endTimeMs(endTimeMs)
                .limit(limit)
                .build();
        List<AlarmRecordIDTO> records = alarmApplicationService.listRecords(param);
        return Result.success(ALARM_CONVERTER.toRecordVOList(records));
    }
}
