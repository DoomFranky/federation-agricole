package school.hei.exam.agriculturalfederation.repository;

import org.springframework.stereotype.Repository;
import school.hei.exam.agriculturalfederation.dto.InputCollectivityDTO;
import school.hei.exam.agriculturalfederation.entity.Collectivity;
import school.hei.exam.agriculturalfederation.entity.CollectivityStructure;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityRepository {
    private final Connection connection;

    public CollectivityRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Collectivity> findCollectivities(){
        List<Collectivity>  result = new ArrayList<>();
        String sql =
                """
                SELECT (id, number, name, location, agricultural_speciality) FROM collectivity
                """;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
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

    public List<Collectivity> addCollectivity(List<InputCollectivityDTO> newCollectivityDTOList){
        List<Collectivity> result = new ArrayList<>();
        String sqlInsertCollectivity =
                """
                INSERT INTO collectivity (location, federation_approval, federation_id) 
                VALUES (?, ? , (SELECT (id) FROM federation LIMIT 1));
                """;

        try (PreparedStatement ps = connection.prepareStatement(sqlInsertCollectivity)){
            connection.setAutoCommit(false);
            for (InputCollectivityDTO collectivityDTO : newCollectivityDTOList){
                ps.setString(1, collectivityDTO.location());
                ps.setBoolean(2, collectivityDTO.federationApproval());
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
        }
        return result;
    }

    private void LinkMemberToCollectivity(Collectivity collectivity, List<Member> members){
        String sqlLinkMemberToCollectivity =
                """
                INSERT INTO collectivity_membership (member_id, collectivity_id, occupation)
                VALUES (?, ?, ?::member_occupation)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sqlLinkMemberToCollectivity)) {
            connection.setAutoCommit(false);
            for (Member member : members){
                ps.setString(1, member.getId());
                ps.setString(2, collectivity.getId());
                ps.setString(3, member.getOccupation());
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
        }
    }

    private void LinkStructureToCollectivity (Collectivity collectivity, CollectivityStructure structure){
        String sqlLinkStructToCol =
                """
                INSERT INTO collectivity_mandate (member_id, collectivity_id, occupation)
                VALUES (?, ?, ?::member_occupation)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sqlLinkStructToCol)) {
            connection.setAutoCommit(false);
             for (Field field : structure.getClass().getFields())
             {
                 Member = member field.get(structure);
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
}
