package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;

import school.hei.exam.agriculturalfederation.entity.DuesRule;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class DuesRuleRepository {
    private final Connection connection;

    public DuesRuleRepository(Connection connection) {
        this.connection = connection;
    }

    public DuesRule findById(String id) {
        String sql = "SELECT id, collectivity_id, frequency, amount_mga, label, effective_from, effective_to FROM dues_rule WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDuesRule(rs, true);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DuesRule> findActiveByCollectivity(String collectivityId) {
        List<DuesRule> rules = new ArrayList<>();
        String sql = """
            SELECT id, collectivity_id, frequency, amount_mga, label, effective_from, effective_to
            FROM dues_rule
            WHERE collectivity_id = ?::uuid
            AND effective_to IS NULL
            AND effective_from <= CURRENT_DATE
            ORDER BY effective_from DESC
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rules.add(mapResultSetToDuesRule(rs, true));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rules;
    }

    public DuesRule create(DuesRule rule) {
        String sql = """
            INSERT INTO dues_rule (id, collectivity_id, frequency, amount_mga, label, effective_from, effective_to)
            VALUES (?::uuid, ?::uuid, ?::dues_frequency, ?, ?, ?, ?)
            RETURNING id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            ps.setString(1, id);
            ps.setString(2, rule.getCollectivityId());
            ps.setString(3, rule.getFrequency().name());
            ps.setBigDecimal(4, rule.getAmountMga());
            ps.setString(5, rule.getLabel());
            ps.setDate(6, Date.valueOf(rule.getEffectiveFrom()));
            ps.setDate(7, rule.getEffectiveTo() != null ? Date.valueOf(rule.getEffectiveTo()) : null);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rule.setId(rs.getString("id"));
                }
            }
            return rule;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivate(String ruleId) {
        String sql = "UPDATE dues_rule SET effective_to = ? WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setString(2, ruleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DuesRule mapResultSetToDuesRule(ResultSet rs, boolean active) throws SQLException {
        return DuesRule.builder()
            .id(rs.getString("id"))
            .collectivityId(rs.getString("collectivity_id"))
            .frequency(DuesRule.DuesFrequency.valueOf(rs.getString("frequency")))
            .amountMga(rs.getBigDecimal("amount_mga"))
            .label(rs.getString("label"))
            .effectiveFrom(rs.getDate("effective_from").toLocalDate())
            .effectiveTo(rs.getDate("effective_to") != null ? rs.getDate("effective_to").toLocalDate() : null)
            .active(active)
            .build();
    }
}