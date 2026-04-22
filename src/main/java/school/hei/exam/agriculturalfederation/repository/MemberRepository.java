package school.hei.exam.agriculturalfederation.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import school.hei.exam.agriculturalfederation.entity.Member;
import school.hei.exam.agriculturalfederation.entity.MemberInscription;
import school.hei.exam.agriculturalfederation.entity.OccupationEnum;

@Repository
public class MemberRepository {
    Connection connection;

    public MemberRepository(Connection connection){
        this.connection = connection;
    }

    public List<Member> findMembersById (List<String> ids){
        try {
            List<Member> members = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                ""
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getString("member_id"));
                member.setFirstName(resultSet.getString("member_first_name"));
                member.setLastName(resultSet.getString("member_last_name"));
                member.setAddress(resultSet.getString("member_address"));
                member.setBirthday(LocalDate.parse(resultSet.getString("member_birthday")));
                member.setEmail(resultSet.getString(""));
                member.setOccupation(OccupationEnum.valueOf(resultSet.getString("")));
                member.setPhoneNumber(resultSet.getString(""));
            }
            return members;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Member> createMembers (List<MemberInscription> membersInscription){
        try {
            List<String> listOfIds = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                ""
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listOfIds.add(resultSet.getString("id"));
            }
            return findMembersById(listOfIds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
