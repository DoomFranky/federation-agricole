package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.TreasuryAccount;

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
public class TreasuryAccountRepository {
    private final Connection connection;

    public TreasuryAccountRepository(Connection connection) {
        this.connection = connection;
    }

    public List<TreasuryAccount> findByCollectivity(String collectivityId) {
        List<TreasuryAccount> accounts = new ArrayList<>();
        String sql = "SELECT id, collectivity_id, account_type, balance_mga, as_of_date FROM treasury_account WHERE collectivity_id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TreasuryAccount acc = TreasuryAccount.builder()
                        .id(rs.getString("id"))
                        .collectivityId(rs.getString("collectivity_id"))
                        .accountType(TreasuryAccount.AccountType.valueOf(rs.getString("account_type")))
                        .balanceMga(rs.getBigDecimal("balance_mga"))
                        .asOfDate(rs.getDate("as_of_date").toLocalDate())
                        .currency("MGA")
                        .build();
                    accounts.add(acc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public List<TreasuryAccount> findByCollectivityAtDate(String collectivityId, LocalDate atDate) {
        List<TreasuryAccount> accounts = new ArrayList<>();
        String sql = "SELECT id, collectivity_id, account_type, balance_mga, as_of_date FROM treasury_account WHERE collectivity_id = ?::uuid AND as_of_date <= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(atDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TreasuryAccount acc = TreasuryAccount.builder()
                        .id(rs.getString("id"))
                        .collectivityId(rs.getString("collectivity_id"))
                        .accountType(TreasuryAccount.AccountType.valueOf(rs.getString("account_type")))
                        .balanceMga(rs.getBigDecimal("balance_mga"))
                        .asOfDate(rs.getDate("as_of_date").toLocalDate())
                        .currency("MGA")
                        .build();
                    accounts.add(acc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public TreasuryAccount create(TreasuryAccount account) {
        String sql = "INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga, as_of_date, currency) VALUES (?::uuid, ?::uuid, ?::account_type, ?, ?, 'MGA') RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            ps.setString(1, id);
            ps.setString(2, account.getCollectivityId());
            ps.setString(3, account.getAccountType().name());
            ps.setBigDecimal(4, account.getBalanceMga());
            ps.setDate(5, Date.valueOf(account.getAsOfDate()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account.setId(rs.getString("id"));
                }
            }
            return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(String accountId, BigDecimal newBalance) {
        String sql = "UPDATE treasury_account SET balance_mga = ?, as_of_date = ? WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setString(3, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TreasuryAccount findById(String id) {
        String sql = "SELECT id, collectivity_id, account_type, balance_mga, as_of_date FROM treasury_account WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return TreasuryAccount.builder()
                        .id(rs.getString("id"))
                        .collectivityId(rs.getString("collectivity_id"))
                        .accountType(TreasuryAccount.AccountType.valueOf(rs.getString("account_type")))
                        .balanceMga(rs.getBigDecimal("balance_mga"))
                        .asOfDate(rs.getDate("as_of_date").toLocalDate())
                        .currency("MGA")
                        .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}