package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;
import school.hei.exam.agriculturalfederation.entity.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CollectivityRepository {
    private final Connection connection;
    private final MemberRepository memberRepository;
    private final CollectivityMembershipRepository membershipRepository;

    public CollectivityRepository(Connection connection, MemberRepository memberRepository, CollectivityMembershipRepository membershipRepository) {
        this.connection = connection;
        this.memberRepository = memberRepository;
        this.membershipRepository = membershipRepository;
    }

    public Collectivity findById(String id) {
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at " +
                   "FROM collectivity WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Collectivity collectivity = mapResultSetToCollectivity(rs);
                    collectivity.setMembers(membershipRepository.findActiveMembersByCollectivity(id));
                    collectivity.setStructure(getStructure(id));
                    return collectivity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Collectivity findByNumber(Integer number) {
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at " +
                   "FROM collectivity WHERE number = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCollectivity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Collectivity findByName(String name) {
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at " +
                   "FROM collectivity WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCollectivity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Collectivity findByLocationAndSpecialty(String location, String specialty) {
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at " +
                   "FROM collectivity WHERE location = ? AND agricultural_specialty = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, location);
            ps.setString(2, specialty);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCollectivity(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Collectivity> findAll() {
        List<Collectivity> collectivities = new ArrayList<>();
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at FROM collectivity";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    collectivities.add(mapResultSetToCollectivity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivities;
    }

    public List<Collectivity> findByIds(List<String> ids) {
        List<Collectivity> collectivities = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return collectivities;
        }
        String placeholders = String.join(",", java.util.Collections.nCopies(ids.size(), "?::uuid"));
        String sql = "SELECT id, number, name, location, agricultural_specialty, created_at " +
                   "FROM collectivity WHERE id IN (" + placeholders + ")";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setString(i + 1, ids.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Collectivity collectivity = mapResultSetToCollectivity(rs);
                    collectivity.setMembers(membershipRepository.findActiveMembersByCollectivity(collectivity.getId()));
                    collectivity.setStructure(getStructure(collectivity.getId()));
                    collectivities.add(collectivity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collectivities;
    }

    public List<Collectivity> create(List<Collectivity> collectivities) {
        List<Collectivity> created = new ArrayList<>();
        String sql = "INSERT INTO collectivity (id, location, agricultural_specialty, federation_id) " +
                "VALUES (?::uuid, ?, ?, (SELECT id FROM federation LIMIT 1))";
        for (Collectivity collectivity : collectivities) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                String id = UUID.randomUUID().toString();
                collectivity.setId(id);
                ps.setString(1, id);
                ps.setString(2, collectivity.getLocation());
                ps.setString(3, collectivity.getAgriculturalSpecialty());
                ps.executeUpdate();
                created.add(collectivity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return created;
    }

    public void updateIdentity(String id, Integer number, String name) {
        String sql = "UPDATE collectivity SET number = ?, name = ? WHERE id = ?::uuid";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, number);
            ps.setString(2, name);
            ps.setString(3, id);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Collectivity not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countActiveMembers(String collectivityId) {
        String sql = "SELECT COUNT(*) FROM collectivity_membership WHERE collectivity_id = ?::uuid AND left_at IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int countMembersWithMinTenure(String collectivityId, int daysMinTenure) {
        String sql = "SELECT COUNT(*) FROM collectivity_membership " +
                  "WHERE collectivity_id = ?::uuid " +
                  "AND joined_at <= CURRENT_DATE - INTERVAL '1 day' * ? " +
                  "AND left_at IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setInt(2, daysMinTenure);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void saveStructure(String collectivityId, CollectivityStructure structure) {
        if (structure.getPresident() != null) {
            saveMandate(collectivityId, structure.getPresident().getId(), "PRESIDENT");
        }
        if (structure.getVicePresident() != null) {
            saveMandate(collectivityId, structure.getVicePresident().getId(), "VICE_PRESIDENT");
        }
        if (structure.getTreasurer() != null) {
            saveMandate(collectivityId, structure.getTreasurer().getId(), "TREASURER");
        }
        if (structure.getSecretary() != null) {
            saveMandate(collectivityId, structure.getSecretary().getId(), "SECRETARY");
        }
    }

    private void saveMandate(String collectivityId, String memberId, String occupation) {
        String sql = "INSERT INTO collectivity_mandate (id, collectivity_id, member_id, occupation, date) " +
                    "VALUES (?::uuid, ?::uuid, ?::uuid, ?::member_occupation, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, collectivityId);
            ps.setString(3, memberId);
            ps.setString(4, occupation);
            ps.setDate(5, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CollectivityStructure getStructure(String collectivityId) {
        CollectivityStructure structure = new CollectivityStructure();
        String sql = "SELECT cm.member_id, cm.occupation " +
                  "FROM collectivity_mandate cm " +
                  "WHERE cm.collectivity_id = ?::uuid " +
                  "ORDER BY cm.date DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String memberId = rs.getString("member_id");
                    String occupation = rs.getString("occupation");
                    Member member = memberRepository.findById(memberId);
                    if (member != null) {
                        switch (occupation) {
                            case "PRESIDENT" -> structure.setPresident(member);
                            case "VICE_PRESIDENT" -> structure.setVicePresident(member);
                            case "SECRETARY" -> structure.setSecretary(member);
                            case "TREASURER" -> structure.setTreasurer(member);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return structure;
    }

    private Collectivity mapResultSetToCollectivity(ResultSet rs) throws SQLException {
        Collectivity collectivity = new Collectivity();
        collectivity.setId(rs.getString("id"));
        collectivity.setNumber(rs.getObject("number") != null ? rs.getInt("number") : null);
        collectivity.setName(rs.getString("name"));
        collectivity.setLocation(rs.getString("location"));
        collectivity.setAgriculturalSpecialty(rs.getString("agricultural_specialty"));
        collectivity.setCreatedAt(rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null);
        return collectivity;
    }
}