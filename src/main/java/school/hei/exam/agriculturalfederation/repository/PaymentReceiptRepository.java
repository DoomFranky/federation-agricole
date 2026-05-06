package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.GenderEnum;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;
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
            VALUES (?, ?, ?, ?, ?::payment_method, ?, ?, ?)
            RETURNING id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String id = receipt.getId();
            if (id == null || id.isBlank()) {
                id = UUID.randomUUID().toString();
                receipt.setId(id);
            }
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
            WHERE collectivity_membership_id = ?
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
            SELECT pr.id, pr.collectivity_membership_id, pr.dues_rule_id, pr.amount_mga, pr.payment_method,
                   pr.collected_at, pr.collected_by_treasurer, pr.notes,
                   m.id as member_id, m.first_name, m.last_name, m.birth_date, m.gender, m.address,
                   m.profession, m.phone_number, m.email, cm.occupation
            FROM payment_receipt pr
            JOIN collectivity_membership cm ON cm.id = pr.collectivity_membership_id
            JOIN member m ON m.id = cm.member_id
            WHERE cm.collectivity_id = ?
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
            WHERE collectivity_membership_id = ?
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
        Member member = Member.builder()
            .id(rs.getString("member_id"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .birthDate(rs.getDate("birth_date").toLocalDate())
            .gender(GenderEnum.valueOf(rs.getString("gender")))
            .address(rs.getString("address"))
            .profession(rs.getString("profession"))
            .phoneNumber(rs.getString("phone_number"))
            .email(rs.getString("email"))
            .occupation(OccupationEnum.valueOf(rs.getString("occupation")))
            .build();
        return PaymentReceipt.builder()
            .id(rs.getString("id"))
            .membershipId(rs.getString("collectivity_membership_id"))
            .duesRuleId(rs.getString("dues_rule_id"))
            .amountMga(rs.getBigDecimal("amount_mga"))
            .paymentMethod(PaymentReceipt.PaymentMethod.valueOf(rs.getString("payment_method")))
            .collectedAt(rs.getDate("collected_at").toLocalDate())
            .collectedByTreasurer(rs.getString("collected_by_treasurer"))
            .notes(rs.getString("notes"))
            .member(member)
            .build();
    }
}