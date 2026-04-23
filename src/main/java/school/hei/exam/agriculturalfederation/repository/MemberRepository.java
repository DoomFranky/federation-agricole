package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;

import school.hei.exam.agriculturalfederation.entity.GenderEnum;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.MembershipReferee;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;

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
public class MemberRepository {
    private final Connection connection;

    public MemberRepository(Connection connection) {
        this.connection = connection;
    }

    public Member findById(String id) {
        String sql = "SELECT id, first_name, last_name, birth_date, gender, address, profession, phone_number, email " +
                   "FROM member WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Member findByEmail(String email) {
        String sql = "SELECT id, first_name, last_name, birth_date, gender, address, profession, phone_number, email " +
                   "FROM member WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Member> findByIdIn(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, birth_date, gender, address, profession, phone_number, email " +
                   "FROM member WHERE id = ANY(?::uuid[])";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setArray(1, connection.createArrayOf("uuid", ids.toArray()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    public void create(Member member,UUID uuid) {
        String sql = "INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email) " +
                     "VALUES (?::uuid, ?, ?, ?, ?::gender, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            String memberId = uuid.toString();
            ps.setString(1, memberId);
            ps.setString(2, member.getFirstName());
            ps.setString(3, member.getLastName());
            ps.setDate(4, Date.valueOf(member.getBirthDate()));
            ps.setString(5, member.getGender().name());
            ps.setString(6, member.getAddress());
            ps.setString(7, member.getProfession());
            ps.setString(8, member.getPhoneNumber());
            ps.setString(9, member.getEmail());
            ps.executeUpdate();
            
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getString("id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setBirthDate(rs.getDate("birth_date").toLocalDate());
        member.setGender(GenderEnum.valueOf(rs.getString("gender")));
        member.setAddress(rs.getString("address"));
        member.setProfession(rs.getString("profession"));
        member.setPhoneNumber(rs.getString("phone_number"));
        member.setEmail(rs.getString("email"));
        member.setOccupation(OccupationEnum.JUNIOR);
        return member;
    }
}