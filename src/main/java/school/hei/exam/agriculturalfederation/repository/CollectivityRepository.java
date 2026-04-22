package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;
import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;

import java.lang.reflect.Field;
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

    public CollectivityRepository(Connection connection) {
        this.connection = connection;
    }

    //=========================================================================================================

    public List<Collectivity> findCollectivities() {
        List<Collectivity> result = new ArrayList<>();
        String sql =
                """
                        SELECT (id, number, name, location, agricultural_speciality) FROM collectivity
                        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Collectivity collectivity = Collectivity.builder()
                            .id(rs.getString("id"))
                            .number(rs.getInt("number"))
                            .name(rs.getString("name"))
                            .specialization(rs.getString("agricultural_speciality"))
                            .build();
                    result.add(collectivity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //=========================================================================================================

    public List<Collectivity> saveCollectivity(List<Collectivity> collectivityList) {
        List<Collectivity> result = new ArrayList<>();
        String sqlInsertCollectivity =
                """
                        INSERT INTO collectivity (location, federation_id, id) 
                        VALUES (?, (SELECT (id) FROM federation LIMIT 1), ?);
                        """;

        try (PreparedStatement ps = connection.prepareStatement(sqlInsertCollectivity)) {
            connection.setAutoCommit(false);
                String collectivityId;
            for (Collectivity collectivity : collectivityList) {
                collectivityId = UUID.randomUUID().toString();
                collectivity.setId(collectivityId);
                ps.setString(1, collectivity.getLocation());
                ps.setString(2, collectivityId);
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
            for (Collectivity collectivity : collectivityList){
                linkMemberToCollectivity(collectivity, collectivity.getMemberList());
                linkStructureToCollectivity(collectivity, collectivity.getStructure());
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return collectivityList;
    }

    public Collectivity findById(String id) {
        String sql = "SELECT id, number, name, location, agricultural_speciality FROM collectivity WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Collectivity.builder()
                            .id(rs.getString("id"))
                            .number(rs.getInt("number"))
                            .name(rs.getString("name"))
                            .specialization(rs.getString("agricultural_speciality"))
                            .location(rs.getString("location"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateIdentity(String id, Integer number, String name) {
        String sql = "UPDATE collectivity SET number = ?, name = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, number);
            ps.setString(2, name);
            ps.setString(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void linkMemberToCollectivity(Collectivity collectivity, List<Member> members) {
        String sqlLinkMemberToCollectivity =
                """
                        INSERT INTO collectivity_membership (member_id, collectivity_id, occupation)
                        VALUES (?, ?, ?::member_occupation)
                        """;
        try (PreparedStatement ps = connection.prepareStatement(sqlLinkMemberToCollectivity)) {
            connection.setAutoCommit(false);
            if (members != null) {
                for (Member member : members) {
                    if (member == null) continue;
                    ps.setString(1, member.getId());
                    ps.setString(2, collectivity.getId());
                    ps.setObject(3, member.getOccupation());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private void linkStructureToCollectivity(Collectivity collectivity, CollectivityStructure structure) {
        String sqlLinkStructToCol =
                """
                        INSERT INTO collectivity_mandate (member_id, collectivity_id, occupation)
                        VALUES (?, ?, ?::member_occupation)
                        """;
        try (PreparedStatement ps = connection.prepareStatement(sqlLinkStructToCol)) {
            connection.setAutoCommit(false);
            for (Field field : structure.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String post = field.getName();
                Member member = (Member) field.get(structure);
                if (member == null) continue;
                if (post.equals("president")) {
                    ps.setString(1, member.getId());
                    ps.setString(2, collectivity.getId());
                    ps.setString(3, OccupationEnum.PRESIDENT.name());
                } else if (post.equals("vicePresident")) {
                    ps.setString(1, member.getId());
                    ps.setString(2, collectivity.getId());
                    ps.setString(3, OccupationEnum.VICE_PRESIDENT.name());
                } else if (post.equals("secretary")) {
                    ps.setString(1, member.getId());
                    ps.setString(2, collectivity.getId());
                    ps.setString(3, OccupationEnum.SECRETARY.name());
                } else if (post.equals("treasurer")) {
                    ps.setString(1, member.getId());
                    ps.setString(2, collectivity.getId());
                    ps.setString(3, OccupationEnum.TREASURER.name());
                }
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
