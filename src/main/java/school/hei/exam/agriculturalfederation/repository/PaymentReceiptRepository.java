package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.PaymentReceipt;

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
public class PaymentReceiptRepository {
    private final Connection connection;

    public PaymentReceiptRepository(Connection connection) {
        this.connection = connection;
    }

    public PaymentReceipt create(PaymentReceipt receipt) {
        String sql = """
            INSERT INTO payment_receipt (id, collectivity_membership_id, dues_rule_id, amount_mga, payment_method, collected_at, collected_by_treasurer, notes)
            VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?::payment_method, ?, ?::uuid, ?)
            RETURNING id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            ps.setString(1, id);
            ps.setString(2, receipt.getMembershipId());
            ps.setString(3, receipt.getDuesRuleId());
            ps.setBigDecimal(4, receipt.getAmountMga());
            ps.setString(5, receipt.getPaymentMethod().name());
            ps.setDate(6, Date.valueOf(receipt.getCollectedAt()));
            ps.setString(7, receipt.getCollectedByTreasurer());
            ps.setString(8, receipt.getNotes());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receipt.setId(rs.getString("id"));
                }
            }
            return receipt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PaymentReceipt> findByMembership(String membershipId) {
        List<PaymentReceipt> receipts = new ArrayList<>();
        String sql = """
            SELECT id, collectivity_membership_id, dues_rule_id, amount_mga, payment_method, collected_at, collected_by_treasurer, notes
            FROM payment_receipt
            WHERE collectivity_membership_id = ?::uuid
            ORDER BY collected_at DESC
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, membershipId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    receipts.add(mapResultSetToReceipt(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receipts;
    }

    public List<PaymentReceipt> findByCollectivityInPeriod(String collectivityId, LocalDate from, LocalDate to) {
        List<PaymentReceipt> receipts = new ArrayList<>();
        String sql = """
            SELECT pr.id, pr.collectivity_membership_id, pr.dues_rule_id, pr.amount_mga, pr.payment_method, pr.collected_at, pr.collected_by_treasurer, pr.notes
            FROM payment_receipt pr
            JOIN collectivity_membership cm ON cm.id = pr.collectivity_membership_id
            WHERE cm.collectivity_id = ?::uuid
            AND pr.collected_at BETWEEN ? AND ?
            ORDER BY pr.collected_at DESC
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    receipts.add(mapResultSetToReceipt(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return receipts;
    }

    public BigDecimal sumPaidByMembershipInPeriod(String membershipId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT COALESCE(SUM(amount_mga), 0) as total
            FROM payment_receipt
            WHERE collectivity_membership_id = ?::uuid
            AND collected_at BETWEEN ? AND ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, membershipId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return BigDecimal.ZERO;
    }

    private PaymentReceipt mapResultSetToReceipt(ResultSet rs) throws SQLException {
        return PaymentReceipt.builder()
            .id(rs.getString("id"))
            .membershipId(rs.getString("collectivity_membership_id"))
            .duesRuleId(rs.getString("dues_rule_id"))
            .amountMga(rs.getBigDecimal("amount_mga"))
            .paymentMethod(PaymentReceipt.PaymentMethod.valueOf(rs.getString("payment_method")))
            .collectedAt(rs.getDate("collected_at").toLocalDate())
            .collectedByTreasurer(rs.getString("collected_by_treasurer"))
            .notes(rs.getString("notes"))
            .build();
    }
}