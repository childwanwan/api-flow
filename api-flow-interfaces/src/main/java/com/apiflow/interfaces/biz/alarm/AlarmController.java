package com.apiflow.interfaces.biz.alarm;

import com.apiflow.application.alarm.AlarmService;
import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/rule/list")
    public Result<List<AlarmRuleIDTO>> listRules(
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) Boolean enabled) {
        List<AlarmRuleIDTO> rules = alarmService.listRules(alarmType, enabled);
        return Result.success(rules);
    }

    @PostMapping("/rule/create")
    public Result<AlarmRuleIDTO> createRule(@RequestBody Map<String, Object> request) {
        String ruleName = (String) request.get("ruleName");
        String alarmType = (String) request.get("alarmType");
        String triggerCondition = (String) request.get("triggerCondition");
        String level = (String) request.get("level");
        Boolean enabled = request.get("enabled") != null ? (Boolean) request.get("enabled") : true;
        if (ruleName == null || alarmType == null) {
            return Result.fail("PARAM_ERROR", "规则名称和告警类型不能为空", null);
        }
        AlarmRuleIDTO rule = alarmService.createRule(ruleName, alarmType, triggerCondition, level, enabled);
        return Result.success(rule);
    }

    @PutMapping("/rule/update")
    public Result<AlarmRuleIDTO> updateRule(@RequestBody Map<String, Object> request) {
        Long id = request.get("id") != null ? Long.valueOf(request.get("id").toString()) : null;
        if (id == null) {
            return Result.fail("PARAM_ERROR", "规则ID不能为空", null);
        }
        String ruleName = (String) request.get("ruleName");
        String alarmType = (String) request.get("alarmType");
        String triggerCondition = (String) request.get("triggerCondition");
        String level = (String) request.get("level");
        Boolean enabled = request.get("enabled") != null ? (Boolean) request.get("enabled") : null;
        AlarmRuleIDTO rule = alarmService.updateRule(id, ruleName, alarmType, triggerCondition, level, enabled);
        return Result.success(rule);
    }

    @DeleteMapping("/rule/{id}")
    public Result<String> deleteRule(@PathVariable Long id) {
        boolean deleted = alarmService.deleteRule(id);
        return deleted ? Result.success("删除成功") : Result.fail("DELETE_FAILED", "删除失败", null);
    }

    @GetMapping("/record/list")
    public Result<List<AlarmRecordIDTO>> listRecords(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Long startTimeMs,
            @RequestParam(required = false) Long endTimeMs,
            @RequestParam(required = false, defaultValue = "100") Integer limit) {
        List<AlarmRecordIDTO> records = alarmService.listRecords(eventType, level, startTimeMs, endTimeMs, limit);
        return Result.success(records);
    }
}
