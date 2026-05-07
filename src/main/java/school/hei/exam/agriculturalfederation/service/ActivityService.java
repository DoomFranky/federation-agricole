package school.hei.exam.agriculturalfederation.service;

import org.springframework.stereotype.Service;
import school.hei.exam.agriculturalfederation.dto.CollectivityActivityDTO;
import school.hei.exam.agriculturalfederation.dto.CreateActivityMemberAttendanceDTO;
import school.hei.exam.agriculturalfederation.dto.CreateCollectivityActivityDTO;
import school.hei.exam.agriculturalfederation.dto.ActivityMemberAttendanceDTO;
import school.hei.exam.agriculturalfederation.dto.MemberDescriptionDTO;
import school.hei.exam.agriculturalfederation.dto.MonthlyRecurrenceRuleDTO;
import school.hei.exam.agriculturalfederation.entity.*;
import school.hei.exam.agriculturalfederation.exception.BadRequestException;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;
import school.hei.exam.agriculturalfederation.repository.ActivityRepository;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;
import school.hei.exam.agriculturalfederation.repository.CollectivityRepository;
import school.hei.exam.agriculturalfederation.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public ActivityService(ActivityRepository activityRepository,
                           CollectivityRepository collectivityRepository,
                           MemberRepository memberRepository) {
        this.activityRepository = activityRepository;
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<CollectivityActivityDTO> getActivities(String collectivityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<CollectivityActivity> activities = activityRepository.findByCollectivityId(collectivityId);
        return activities.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<CollectivityActivityDTO> createActivities(String collectivityId,
                                                         List<CreateCollectivityActivityDTO> dtos) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<CollectivityActivity> activities = new ArrayList<>();
        for (CreateCollectivityActivityDTO dto : dtos) {
            validateActivityCreation(dto);
            activities.add(toEntity(dto));
        }

        List<CollectivityActivity> created = activityRepository.createActivities(collectivityId, activities);
        return created.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ActivityMemberAttendanceDTO> createAttendance(String collectivityId,
                                                               String activityId,
                                                               List<CreateActivityMemberAttendanceDTO> dtos) {
        CollectivityActivity activity = activityRepository.findById(activityId);
        if (activity == null) {
            throw new NotFoundException("Activity not found: " + activityId);
        }

        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        List<ActivityMemberAttendance> attendances = new ArrayList<>();
        for (CreateActivityMemberAttendanceDTO dto : dtos) {
            Member member = memberRepository.findById(dto.getMemberIdentifier());
            if (member == null) {
                throw new NotFoundException("Member not found: " + dto.getMemberIdentifier());
            }

            ActivityMemberAttendance attendance = ActivityMemberAttendance.builder()
                    .memberDescription(MemberDescription.builder()
                            .id(member.getId())
                            .firstName(member.getFirstName())
                            .lastName(member.getLastName())
                            .email(member.getEmail())
                            .occupation(member.getOccupation())
                            .build())
                    .attendanceStatus(AttendanceStatusEnum.valueOf(dto.getAttendanceStatus()))
                    .build();
            attendances.add(attendance);
        }

        try {
            List<ActivityMemberAttendance> created = activityRepository.createAttendance(activityId, attendances);
            return created.stream().map(this::toAttendanceDTO).collect(Collectors.toList());
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("already confirmed")) {
                throw new BadRequestException(e.getMessage());
            }
            throw e;
        }
    }

    public List<ActivityMemberAttendanceDTO> getAttendance(String collectivityId, String activityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new NotFoundException("Collectivity not found: " + collectivityId);
        }

        CollectivityActivity activity = activityRepository.findById(activityId);
        if (activity == null) {
            throw new NotFoundException("Activity not found: " + activityId);
        }

        return activityRepository.findAttendanceByActivityId(activityId).stream()
                .map(this::toAttendanceDTO)
                .collect(Collectors.toList());
    }

    private void validateActivityCreation(CreateCollectivityActivityDTO dto) {
        boolean hasRecurrenceRule = dto.getRecurrenceRule() != null;
        boolean hasExecutiveDate = dto.getExecutiveDate() != null;

        if (hasRecurrenceRule && hasExecutiveDate) {
            throw new BadRequestException("Either recurrence rule or executive date can be provided, not both");
        }

        if (dto.getLabel() == null || dto.getLabel().isBlank()) {
            throw new BadRequestException("Label is required");
        }

        if (dto.getActivityType() == null || dto.getActivityType().isBlank()) {
            throw new BadRequestException("Activity type is required");
        }

        if (hasRecurrenceRule) {
            if (dto.getRecurrenceRule().getWeekOrdinal() == null ||
                    dto.getRecurrenceRule().getWeekOrdinal() < 1 ||
                    dto.getRecurrenceRule().getWeekOrdinal() > 5) {
                throw new BadRequestException("Week ordinal must be between 1 and 5");
            }
            if (dto.getRecurrenceRule().getDayOfWeek() == null ||
                    dto.getRecurrenceRule().getDayOfWeek().isBlank()) {
                throw new BadRequestException("Day of week is required for recurrence rule");
            }
        }
    }

    private CollectivityActivity toEntity(CreateCollectivityActivityDTO dto) {
        return CollectivityActivity.builder()
                .label(dto.getLabel())
                .activityType(ActivityTypeEnum.valueOf(dto.getActivityType()))
                .executiveDate(dto.getExecutiveDate())
                .recurrenceRule(dto.getRecurrenceRule() != null ?
                        MonthlyRecurrenceRule.builder()
                                .weekOrdinal(dto.getRecurrenceRule().getWeekOrdinal())
                                .dayOfWeek(DayOfWeekEnum.valueOf(dto.getRecurrenceRule().getDayOfWeek()))
                                .build() : null)
                .memberOccupationConcerned(dto.getMemberOccupationConcerned() != null ?
                        dto.getMemberOccupationConcerned().stream()
                                .map(OccupationEnum::valueOf)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    private CollectivityActivityDTO toDTO(CollectivityActivity activity) {
        CollectivityActivityDTO dto = new CollectivityActivityDTO();
        dto.setId(activity.getId());
        dto.setLabel(activity.getLabel());
        dto.setActivityType(activity.getActivityType().name());
        dto.setExecutiveDate(activity.getExecutiveDate());
        if (activity.getRecurrenceRule() != null) {
            MonthlyRecurrenceRuleDTO ruleDTO = new MonthlyRecurrenceRuleDTO();
            ruleDTO.setWeekOrdinal(activity.getRecurrenceRule().getWeekOrdinal());
            ruleDTO.setDayOfWeek(activity.getRecurrenceRule().getDayOfWeek().name());
            dto.setRecurrenceRule(ruleDTO);
        }
        if (activity.getMemberOccupationConcerned() != null) {
            dto.setMemberOccupationConcerned(activity.getMemberOccupationConcerned().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private ActivityMemberAttendanceDTO toAttendanceDTO(ActivityMemberAttendance attendance) {
        MemberDescriptionDTO memberDescDTO = new MemberDescriptionDTO();
        memberDescDTO.setId(attendance.getMemberDescription().getId());
        memberDescDTO.setFirstName(attendance.getMemberDescription().getFirstName());
        memberDescDTO.setLastName(attendance.getMemberDescription().getLastName());
        memberDescDTO.setEmail(attendance.getMemberDescription().getEmail());
        if (attendance.getMemberDescription().getOccupation() != null) {
            memberDescDTO.setOccupation(attendance.getMemberDescription().getOccupation().name());
        }

        ActivityMemberAttendanceDTO dto = new ActivityMemberAttendanceDTO();
        dto.setId(attendance.getId());
        dto.setMemberDescription(memberDescDTO);
        dto.setAttendanceStatus(attendance.getAttendanceStatus().name());
        return dto;
    }
}
