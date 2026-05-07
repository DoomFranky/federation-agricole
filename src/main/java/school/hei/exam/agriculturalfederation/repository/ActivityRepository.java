package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.*;
import school.hei.exam.agriculturalfederation.exception.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ActivityRepository {
    private final Connection connection;

    public ActivityRepository(Connection connection) {
        this.connection = connection;
    }

    public List<CollectivityActivity> findByCollectivityId(String collectivityId) {
        List<CollectivityActivity> activities = new ArrayList<>();
        String sql = "SELECT id, title, activity_type, scheduled_at, recurrence_rule " +
                "FROM activity WHERE collectivity_id = ? AND scope = 'COLLECTIVITY'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activities.add(mapResultSetToActivity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return activities;
    }

    public CollectivityActivity findById(String activityId) {
        String sql = "SELECT id, title, activity_type, scheduled_at, recurrence_rule " +
                "FROM activity WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActivity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<CollectivityActivity> createActivities(String collectivityId, List<CollectivityActivity> activities) {
        List<CollectivityActivity> created = new ArrayList<>();
        String sql = "INSERT INTO activity (id, collectivity_id, scope, activity_type, title, scheduled_at, recurrence_rule) " +
                "VALUES (?, ?, 'COLLECTIVITY', ?::activity_type, ?, ?, ?)";

        for (CollectivityActivity activity : activities) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                String id = UUID.randomUUID().toString();
                activity.setId(id);
                ps.setString(1, id);
                ps.setString(2, collectivityId);
                ps.setString(3, activity.getActivityType().name());
                ps.setString(4, activity.getLabel());
                ps.setDate(5, java.sql.Date.valueOf(activity.getExecutiveDate()));
                ps.setString(6, activity.getRecurrenceRule() != null ?
                        buildRecurrenceRuleString(activity.getRecurrenceRule()) : null);
                ps.executeUpdate();

                if (activity.getMemberOccupationConcerned() != null) {
                    saveTargetOccupations(id, activity.getMemberOccupationConcerned());
                }

                created.add(activity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return created;
    }

    public List<ActivityMemberAttendance> createAttendance(String activityId, List<ActivityMemberAttendance> attendances) {
        List<ActivityMemberAttendance> created = new ArrayList<>();
        String checkSql = "SELECT attendance_status FROM attendance WHERE activity_id = ? AND member_id = ?";
        String insertSql = "INSERT INTO attendance (id, activity_id, member_id, attendance_status, is_guest) " +
                "VALUES (?, ?, ?, ?::attendance_status, FALSE)";

        for (ActivityMemberAttendance attendance : attendances) {
            try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
                checkPs.setString(1, activityId);
                checkPs.setString(2, attendance.getMemberDescription().getId());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        String existingStatus = rs.getString("attendance_status");
                        if (!"UNDEFINED".equals(existingStatus)) {
                            throw new RuntimeException("Attendance already confirmed for member: " +
                                    attendance.getMemberDescription().getId());
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                String id = UUID.randomUUID().toString();
                attendance.setId(id);
                ps.setString(1, id);
                ps.setString(2, activityId);
                ps.setString(3, attendance.getMemberDescription().getId());
                ps.setString(4, attendance.getAttendanceStatus().name());
                ps.executeUpdate();
                created.add(attendance);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return created;
    }

    public List<ActivityMemberAttendance> findAttendanceByActivityId(String activityId) {
        List<ActivityMemberAttendance> attendances = new ArrayList<>();

        String activitySql = "SELECT collectivity_id FROM activity WHERE id = ?";
        String targetOccSql = "SELECT occupation FROM activity_target_occupation WHERE activity_id = ?";
        String attendanceSql = "SELECT a.id, a.member_id, a.attendance_status, m.first_name, m.last_name, m.email, cm.occupation " +
                "FROM attendance a " +
                "JOIN member m ON a.member_id = m.id " +
                "LEFT JOIN collectivity_membership cm ON cm.member_id = m.id AND cm.left_at IS NULL " +
                "WHERE a.activity_id = ?";
        String membersSql = "SELECT m.id, m.first_name, m.last_name, m.email, cm.occupation " +
                "FROM collectivity_membership cm " +
                "JOIN member m ON cm.member_id = m.id " +
                "WHERE cm.collectivity_id = ? AND cm.left_at IS NULL";

        try {
            String collectivityId = null;
            List<OccupationEnum> targetOccupations = new ArrayList<>();

            try (PreparedStatement ps = connection.prepareStatement(activitySql)) {
                ps.setString(1, activityId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        collectivityId = rs.getString("collectivity_id");
                    }
                }
            }

            if (collectivityId == null) return attendances;

            try (PreparedStatement ps = connection.prepareStatement(targetOccSql)) {
                ps.setString(1, activityId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        targetOccupations.add(OccupationEnum.valueOf(rs.getString("occupation")));
                    }
                }
            }

            List<String> membersWithAttendance = new ArrayList<>();

            try (PreparedStatement ps = connection.prepareStatement(attendanceSql)) {
                ps.setString(1, activityId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        membersWithAttendance.add(rs.getString("member_id"));
                        MemberDescription memberDesc = MemberDescription.builder()
                                .id(rs.getString("member_id"))
                                .firstName(rs.getString("first_name"))
                                .lastName(rs.getString("last_name"))
                                .email(rs.getString("email"))
                                .occupation(rs.getString("occupation") != null ?
                                        OccupationEnum.valueOf(rs.getString("occupation")) : null)
                                .build();
                        attendances.add(ActivityMemberAttendance.builder()
                                .id(rs.getString("id"))
                                .memberDescription(memberDesc)
                                .attendanceStatus(AttendanceStatusEnum.valueOf(rs.getString("attendance_status")))
                                .build());
                    }
                }
            }

            if (!targetOccupations.isEmpty()) {
                try (PreparedStatement ps = connection.prepareStatement(membersSql)) {
                    ps.setString(1, collectivityId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String memberId = rs.getString("id");
                            OccupationEnum memberOcc = rs.getString("occupation") != null ?
                                    OccupationEnum.valueOf(rs.getString("occupation")) : null;

                            if (targetOccupations.contains(memberOcc) && !membersWithAttendance.contains(memberId)) {
                                MemberDescription memberDesc = MemberDescription.builder()
                                        .id(memberId)
                                        .firstName(rs.getString("first_name"))
                                        .lastName(rs.getString("last_name"))
                                        .email(rs.getString("email"))
                                        .occupation(memberOcc)
                                        .build();
                                attendances.add(ActivityMemberAttendance.builder()
                                        .memberDescription(memberDesc)
                                        .attendanceStatus(AttendanceStatusEnum.UNDEFINED)
                                        .build());
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return attendances;
    }

    private CollectivityActivity mapResultSetToActivity(ResultSet rs) throws SQLException {
        CollectivityActivity activity = CollectivityActivity.builder()
                .id(rs.getString("id"))
                .label(rs.getString("title"))
                .activityType(ActivityTypeEnum.valueOf(rs.getString("activity_type")))
                .executiveDate(rs.getDate("scheduled_at") != null ?
                        rs.getDate("scheduled_at").toLocalDate() : null)
                .build();

        String recurrenceRule = rs.getString("recurrence_rule");
        if (recurrenceRule != null) {
            activity.setRecurrenceRule(parseRecurrenceRule(recurrenceRule));
        }

        activity.setMemberOccupationConcerned(findTargetOccupations(activity.getId()));
        return activity;
    }

    private void saveTargetOccupations(String activityId, List<OccupationEnum> occupations) {
        String sql = "INSERT INTO activity_target_occupation (activity_id, occupation) VALUES (?, ?::member_occupation)";
        for (OccupationEnum occupation : occupations) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, activityId);
                ps.setString(2, occupation.name());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<OccupationEnum> findTargetOccupations(String activityId) {
        List<OccupationEnum> occupations = new ArrayList<>();
        String sql = "SELECT occupation FROM activity_target_occupation WHERE activity_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, activityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    occupations.add(OccupationEnum.valueOf(rs.getString("occupation")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return occupations.isEmpty() ? null : occupations;
    }

    private String buildRecurrenceRuleString(MonthlyRecurrenceRule rule) {
        if (rule == null) return null;
        return rule.getWeekOrdinal() + " " + rule.getDayOfWeek().name();
    }

    private MonthlyRecurrenceRule parseRecurrenceRule(String ruleStr) {
        if (ruleStr == null || ruleStr.isBlank()) return null;
        String[] parts = ruleStr.split(" ");
        if (parts.length != 2) return null;
        return MonthlyRecurrenceRule.builder()
                .weekOrdinal(Integer.parseInt(parts[0]))
                .dayOfWeek(DayOfWeekEnum.valueOf(parts[1]))
                .build();
    }
}
