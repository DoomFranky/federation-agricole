package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;

import school.hei.exam.agriculturalfederation.entity.AccountType;
import school.hei.exam.agriculturalfederation.entity.BankAccountDetail;
import school.hei.exam.agriculturalfederation.entity.BankName;
import school.hei.exam.agriculturalfederation.entity.MobileMoneyAccountDetail;
import school.hei.exam.agriculturalfederation.entity.MobileMoneyProvider;
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
        String sql = """
            SELECT ta.id, ta.collectivity_id, ta.account_type, ta.balance_mga, ta.as_of_date,
                   ba.account_holder_name, ba.bank_name, ba.bank_code, ba.branch_code, ba.account_number, ba.rib_key,
                   mm.account_holder_name as mm_holder_name, mm.provider, mm.phone_number
            FROM treasury_account ta
            LEFT JOIN bank_account ba ON ba.treasury_account_id = ta.id
            LEFT JOIN mobile_money_account mm ON mm.treasury_account_id = ta.id
            WHERE ta.collectivity_id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToTreasuryAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public List<TreasuryAccount> findByCollectivityAtDate(String collectivityId, LocalDate atDate) {
        List<TreasuryAccount> accounts = new ArrayList<>();
        String sql = """
            SELECT id, collectivity_id, account_type, balance_mga, as_of_date,
                   account_holder_name, bank_name, bank_code, branch_code, account_number, rib_key,
                   mm_holder_name, provider, phone_number
            FROM (
                SELECT ta.id, ta.collectivity_id, ta.account_type, ta.balance_mga, ta.as_of_date,
                       ba.account_holder_name, ba.bank_name, ba.bank_code, ba.branch_code, ba.account_number, ba.rib_key,
                       mm.account_holder_name as mm_holder_name, mm.provider, mm.phone_number,
                       ROW_NUMBER() OVER (
                           PARTITION BY CASE
                               WHEN ta.account_type = 'CASH' THEN 'CASH-' || ta.collectivity_id
                               WHEN ba.treasury_account_id IS NOT NULL THEN 'BANK-' || ba.treasury_account_id
                               WHEN mm.treasury_account_id IS NOT NULL THEN 'MM-' || mm.treasury_account_id
                               ELSE ta.id
                           END
                           ORDER BY ta.as_of_date DESC
                       ) as rn
                FROM treasury_account ta
                LEFT JOIN bank_account ba ON ba.treasury_account_id = ta.id
                LEFT JOIN mobile_money_account mm ON mm.treasury_account_id = ta.id
                WHERE ta.collectivity_id = ? AND ta.as_of_date <= ?
            ) ranked
            WHERE rn = 1
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(atDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToTreasuryAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public TreasuryAccount create(TreasuryAccount account) {
        String sql = "INSERT INTO treasury_account (id, collectivity_id, account_type, balance_mga, as_of_date, currency) VALUES (?, ?, ?::account_type, ?, ?, 'MGA') RETURNING id";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String id = account.getId();
            if (id == null || id.isBlank()) {
                id = UUID.randomUUID().toString();
                account.setId(id);
            }
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
        String sql = "UPDATE treasury_account SET balance_mga = ?, as_of_date = ? WHERE id = ?";
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
        String sql = """
            SELECT ta.id, ta.collectivity_id, ta.account_type, ta.balance_mga, ta.as_of_date,
                   ba.account_holder_name, ba.bank_name, ba.bank_code, ba.branch_code, ba.account_number, ba.rib_key,
                   mm.account_holder_name as mm_holder_name, mm.provider, mm.phone_number
            FROM treasury_account ta
            LEFT JOIN bank_account ba ON ba.treasury_account_id = ta.id
            LEFT JOIN mobile_money_account mm ON mm.treasury_account_id = ta.id
            WHERE ta.id = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTreasuryAccount(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public TreasuryAccount findByCollectivityAndType(String collectivityId, AccountType accountType) {
        String sql = """
            SELECT ta.id, ta.collectivity_id, ta.account_type, ta.balance_mga, ta.as_of_date,
                   ba.account_holder_name, ba.bank_name, ba.bank_code, ba.branch_code, ba.account_number, ba.rib_key,
                   mm.account_holder_name as mm_holder_name, mm.provider, mm.phone_number
            FROM treasury_account ta
            LEFT JOIN bank_account ba ON ba.treasury_account_id = ta.id
            LEFT JOIN mobile_money_account mm ON mm.treasury_account_id = ta.id
            WHERE ta.collectivity_id = ? AND ta.account_type = ?::account_type
            ORDER BY ta.as_of_date DESC
            LIMIT 1
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setString(2, accountType.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTreasuryAccount(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private TreasuryAccount mapResultSetToTreasuryAccount(ResultSet rs) throws SQLException {
        TreasuryAccount.TreasuryAccountBuilder builder = TreasuryAccount.builder()
            .id(rs.getString("id"))
            .collectivityId(rs.getString("collectivity_id"))
            .accountType(AccountType.valueOf(rs.getString("account_type")))
            .balanceMga(rs.getBigDecimal("balance_mga"))
            .asOfDate(rs.getDate("as_of_date").toLocalDate())
            .currency("MGA");

        AccountType type = AccountType.valueOf(rs.getString("account_type"));

        if (type == AccountType.BANK) {
            String bankHolderName = rs.getString("account_holder_name");
            String bankNameStr = rs.getString("bank_name");
            if (bankHolderName != null || bankNameStr != null) {
                BankName bankName = bankNameStr != null ? BankName.valueOf(bankNameStr) : null;
                builder.bankAccountDetail(BankAccountDetail.builder()
                    .accountHolderName(bankHolderName)
                    .bankName(bankName)
                    .bankCode(rs.getString("bank_code"))
                    .branchCode(rs.getString("branch_code"))
                    .accountNumber(rs.getString("account_number"))
                    .ribKey(rs.getString("rib_key"))
                    .build());
            }
        } else if (type == AccountType.MOBILE_MONEY) {
            String mmHolderName = rs.getString("mm_holder_name");
            String providerStr = rs.getString("provider");
            if (mmHolderName != null || providerStr != null) {
                MobileMoneyProvider provider = providerStr != null ? MobileMoneyProvider.valueOf(providerStr) : null;
                builder.mobileMoneyAccountDetail(MobileMoneyAccountDetail.builder()
                    .accountHolderName(mmHolderName)
                    .provider(provider)
                    .phoneNumber(rs.getString("phone_number"))
                    .build());
            }
        }

        return builder.build();
    }
}
