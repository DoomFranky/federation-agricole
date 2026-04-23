package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;

import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;
import school.hei.exam.agriculturalfederation.entity.RefereeInfo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CollectivityMembershipRepository {
    private final Connection connection;
    private final MemberRepository memberRepository;

    public CollectivityMembershipRepository(Connection connection, MemberRepository memberRepository) {
        this.connection = connection;
        this.memberRepository = memberRepository;
    }

    public String createMembership(String memberId, String collectivityId, OccupationEnum occupation,UUID membershipuuId) {
        String sql = "INSERT INTO collectivity_membership (id, member_id, collectivity_id, occupation, joined_at) " +
                     "VALUES (?::uuid, ?::uuid, ?::uuid, ?::member_occupation, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String membershipId = membershipuuId.toString();
            ps.setString(1, membershipId);
            ps.setString(2, memberId);
            ps.setString(3, collectivityId);
            ps.setString(4, occupation.name());
            ps.setDate(5, Date.valueOf(java.time.LocalDate.now()));
            ps.executeUpdate();
            return membershipId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addReferees(String membershipId, List<RefereeInfo> referees) {
        String sql = "INSERT INTO membership_referee (id, membership_id, referee_member_id, referee_collectivity_id, relationship_nature) " +
                   "VALUES (?::uuid, ?::uuid, ?::uuid, ?::uuid)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (RefereeInfo referee : referees) {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, membershipId);
                ps.setString(3, referee.memberId());
                ps.setString(4, referee.collectivityId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public record RefereeInfo(String memberId, String collectivityId, String relationshipNature) {}

    public List<String> findRefereeIdsForMembership(String membershipId) {
        List<String> refereeIds = new ArrayList<>();
        String sql = "SELECT referee_member_id FROM membership_referee WHERE membership_id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, membershipId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    refereeIds.add(rs.getString("referee_member_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return refereeIds;
    }

    public List<Member> findActiveMembersByCollectivity(String collectivityId) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.id, m.first_name, m.last_name, m.birth_date, m.gender, m.address, m.profession, m.phone_number, m.email " +
                  "FROM member m " +
                  "JOIN collectivity_membership cm ON cm.member_id = m.id " +
                  "WHERE cm.collectivity_id = ?::uuid AND cm.left_at IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Member member = new Member();
                    member.setId(rs.getString("id"));
                    member.setFirstName(rs.getString("first_name"));
                    member.setLastName(rs.getString("last_name"));
                    member.setBirthDate(rs.getDate("birth_date").toLocalDate());
                    member.setAddress(rs.getString("address"));
                    member.setProfession(rs.getString("profession"));
                    member.setPhoneNumber(rs.getString("phone_number"));
                    member.setEmail(rs.getString("email"));
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    public String findActiveMembershipId(String memberId, String collectivityId) {
        String sql;
        if (collectivityId != null) {
            sql = "SELECT id FROM collectivity_membership WHERE member_id = ?::uuid AND collectivity_id = ?::uuid AND left_at IS NULL";
        } else {
            sql = "SELECT id FROM collectivity_membership WHERE member_id = ?::uuid AND left_at IS NULL LIMIT 1";
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, memberId);
            if (collectivityId != null) {
                ps.setString(2, collectivityId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> findConfirmedMemberIdsWithMinTenure(String collectivityId, int daysMinTenure) {
        List<String> memberIds = new ArrayList<>();
        String sql;
        if (collectivityId != null)
        {
            sql = "SELECT cm.member_id " +
                    "FROM collectivity_membership cm " +
                    "WHERE cm.collectivity_id = ?::uuid " +
                    "AND cm.joined_at <= CURRENT_DATE - INTERVAL '1 day' * ? " +
                    "AND cm.left_at IS NULL";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, collectivityId);
                ps.setInt(2, daysMinTenure);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        memberIds.add(rs.getString("member_id"));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return memberIds;
        }
        else{
            sql = "SELECT cm.member_id " +
                    "FROM collectivity_membership cm " +
                    "WHERE cm.joined_at <= CURRENT_DATE - INTERVAL '1 day' * ? " +
                    "AND cm.left_at IS NULL";
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, daysMinTenure);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    memberIds.add(rs.getString("member_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberIds;
    }
}