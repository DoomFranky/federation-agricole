package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.dto.CollectivityInformationDTO;
import school.hei.exam.agriculturalfederation.dto.CollectivityLocalStatisticsDTO;
import school.hei.exam.agriculturalfederation.dto.CollectivityOverallStatisticsDTO;
import school.hei.exam.agriculturalfederation.dto.MemberDescriptionDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {
    private final Connection connection;

    public StatisticsRepository(Connection connection) {
        this.connection = connection;
    }

    public List<CollectivityLocalStatisticsDTO> findLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {
        List<CollectivityLocalStatisticsDTO> results = new ArrayList<>();
        String sql = """
            WITH date_range AS (SELECT ?::date as from_date, ?::date as to_date),
            dues AS (
                SELECT amount_mga, frequency, effective_from FROM dues_rule
                WHERE collectivity_id = ? AND effective_to IS NULL LIMIT 1
            ),
            member_payments AS (
                SELECT cm.member_id, m.first_name, m.last_name, m.email, cm.occupation, cm.joined_at,
                       COALESCE(SUM(pr.amount_mga), 0) as earned
                FROM collectivity_membership cm
                JOIN member m ON m.id = cm.member_id
                LEFT JOIN payment_receipt pr ON pr.collectivity_membership_id = cm.id
                CROSS JOIN date_range
                WHERE cm.collectivity_id = ?
                AND (pr.collected_at IS NULL OR pr.collected_at BETWEEN date_range.from_date AND date_range.to_date)
                GROUP BY cm.member_id, m.first_name, m.last_name, m.email, cm.occupation,cm.joined_at
            )
            SELECT mp.member_id, mp.first_name, mp.last_name, mp.email, mp.occupation,
                   mp.earned as earned_amount,
                   GREATEST(0,
                        CASE 
                            WHEN d.frequency = 'MONTHLY' THEN
                                CASE WHEN (SELECT from_date FROM date_range) < mp.joined_at THEN
                                    CASE WHEN mp.joined_at < d.effective_from THEN
                                        (SELECT EXTRACT(YEAR FROM to_date)*12 + EXTRACT(MONTH FROM to_date)
                                            - EXTRACT(YEAR FROM d.effective_from)*12 - EXTRACT(MONTH FROM d.effective_from)
                                        FROM date_range) * d.amount_mga - mp.earned
                                    ELSE
                                        (SELECT EXTRACT(YEAR FROM to_date)*12 + EXTRACT(MONTH FROM to_date)
                                            - EXTRACT(YEAR FROM mp.joined_at)*12 - EXTRACT(MONTH FROM mp.joined_at)
                                        FROM date_range) * d.amount_mga - mp.earned
                                    END
                                ELSE
                                    (SELECT EXTRACT(YEAR FROM to_date)*12 + EXTRACT(MONTH FROM to_date)
                                        - EXTRACT(YEAR FROM from_date)*12 - EXTRACT(MONTH FROM from_date)
                                    FROM date_range) * d.amount_mga - mp.earned
                                END
                            WHEN d.frequency = 'ANNUALLY' THEN 
                                CASE WHEN (SELECT from_date FROM date_range) < mp.joined_at THEN
                                    CASE WHEN mp.joined_at < d.effective_from THEN
                                        (SELECT EXTRACT(YEAR FROM to_date)
                                                - EXTRACT(YEAR FROM d.effective_from)
                                        FROM date_range) * d.amount_mga - mp.earned
                                    ELSE
                                        (SELECT EXTRACT(YEAR FROM to_date) 
                                                - EXTRACT(YEAR FROM mp.joined_at)
                                        FROM date_range) * d.amount_mga - mp.earned
                                    END
                                ELSE
                                    (SELECT EXTRACT(YEAR FROM to_date)
                                                - EXTRACT(YEAR FROM from_date)
                                        FROM date_range) * d.amount_mga - mp.earned
                                END
                            WHEN d.frequency = 'WEEKLY' THEN
                                CEIL(((SELECT to_date - from_date FROM date_range)/7.0) -1)* d.amount_mga - mp.earned
                        ELSE 0 END) as unpaid_amount
            FROM member_payments mp LEFT JOIN dues d ON 1=1
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setString(4, collectivityId);
            ps.setString(5, collectivityId);
            ps.setDate(6, Date.valueOf(from));
            ps.setDate(7, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapToLocalStatistics(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<CollectivityOverallStatisticsDTO> findOverallStatistics(LocalDate from, LocalDate to) {
        List<CollectivityOverallStatisticsDTO> results = new ArrayList<>();
        String sql = """
            WITH date_range AS (SELECT ?::date as from_date, ?::date as to_date)
            SELECT c.id, c.name, c.number,
                   (SELECT COUNT(*) FROM collectivity_membership cm
                    WHERE cm.collectivity_id = c.id
                    AND cm.joined_at BETWEEN date_range.from_date AND date_range.to_date) as new_members,
                   COALESCE((
                       SELECT (COUNT(DISTINCT cm.member_id) FILTER (WHERE COALESCE(paid.total, 0) >= d.amount_mga))::float * 100.0 /
                              NULLIF(COUNT(DISTINCT cm.member_id), 0)::float
                       FROM collectivity_membership cm
                       LEFT JOIN (
                           SELECT pr.collectivity_membership_id, SUM(pr.amount_mga) as total
                           FROM payment_receipt pr
                           WHERE pr.collected_at BETWEEN date_range.from_date AND date_range.to_date
                           GROUP BY pr.collectivity_membership_id
                       ) paid ON paid.collectivity_membership_id = cm.id
                       LEFT JOIN dues_rule d ON d.collectivity_id = cm.collectivity_id AND d.effective_to IS NULL
                       WHERE cm.collectivity_id = c.id
                       AND cm.joined_at <= date_range.to_date
                       AND (cm.left_at IS NULL OR cm.left_at > date_range.to_date)
                   ), 0) as dues_percentage,
                   COALESCE((
                       SELECT (COUNT(DISTINCT att.id) FILTER (WHERE att.attendance_status = 'ATTENDED'))::float * 100.0 /
                              NULLIF(COUNT(DISTINCT att.id), 0)::float
                       FROM activity a
                       JOIN attendance att ON att.activity_id = a.id
                       JOIN collectivity_membership cm ON cm.member_id = att.member_id
                       WHERE a.collectivity_id = c.id
                       AND a.scheduled_at BETWEEN date_range.from_date AND date_range.to_date
                       AND cm.collectivity_id = c.id
                       AND cm.joined_at <= date_range.to_date
                       AND (cm.left_at IS NULL OR cm.left_at > date_range.to_date)
                   ), 0) as assiduity_percentage
            FROM collectivity c
            CROSS JOIN date_range
            WHERE EXISTS (SELECT 1 FROM collectivity_membership cm WHERE cm.collectivity_id = c.id)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapToOverallStatistics(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private CollectivityLocalStatisticsDTO mapToLocalStatistics(ResultSet rs) throws SQLException {
        MemberDescriptionDTO memberDesc = new MemberDescriptionDTO()
            .id(rs.getString("member_id"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .email(rs.getString("email"))
            .occupation(rs.getString("occupation"));
        return new CollectivityLocalStatisticsDTO()
            .memberDescription(memberDesc)
            .earnedAmount(rs.getDouble("earned_amount"))
            .unpaidAmount(rs.getDouble("unpaid_amount"))
            .assiduityPercentage(rs.getDouble("assiduity_percentage"));
    }

    private CollectivityOverallStatisticsDTO mapToOverallStatistics(ResultSet rs) throws SQLException {
        CollectivityInformationDTO info = new CollectivityInformationDTO()
            .name(rs.getString("name"))
            .number(rs.getObject("number") != null ? rs.getInt("number") : null);
        return new CollectivityOverallStatisticsDTO()
            .collectivityInformation(info)
            .newMembersNumber(rs.getInt("new_members"))
            .overallMemberCurrentDuePercentage(rs.getDouble("dues_percentage"))
            .overallMemberAssiduityPercentage(rs.getDouble("assiduity_percentage"));
    }
}
