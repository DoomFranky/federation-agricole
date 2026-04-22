package school.hei.exam.agriculturalfederation.repository;

import java.sql.Connection;
import java.sql.Date;
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
                "SELECT id, last_name, first_name, birth_date, gender, address, "+
                "job, phone, email, join_date, poste FROM membre"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getString("id"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setAddress(resultSet.getString("address"));
                member.setBirthday(LocalDate.parse(resultSet.getString("birth_date")));
                member.setEmail(resultSet.getString("email"));
                member.setOccupation(OccupationEnum.JUNIOR);
                member.setPhoneNumber(resultSet.getString("phone"));
            }
            return members;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Member> createMembers (List<MemberInscription> membersInscription){
        try {
            List<String> listOfIds = new ArrayList<>();
            List<Name> name = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO member (first_name, last_name, birth_date, gender, address, "+
                "phone, email) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
            );
            for (MemberInscription memberInscription : membersInscription) {
                name.add(new Name(memberInscription.getFirstName(),memberInscription.getLastName()));
                preparedStatement.setString(1, memberInscription.getFirstName());
                preparedStatement.setString(2, memberInscription.getLastName());
                preparedStatement.setDate(3, Date.valueOf(memberInscription.getBirthday()));
                preparedStatement.setObject(4, memberInscription.getGender());
                preparedStatement.setString(5, memberInscription.getPhoneNumber());
                preparedStatement.setString(6, memberInscription.getEmail());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            listOfIds = getIdByName(name);
            return findMembersById(listOfIds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class Name {
        private String firstName;
        private String lastName;
        
        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
        
        
    }
    public List<Integer> getIdByName (List<Name> name){
        try {
            List<Integer> listOfids = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT id FROM member WHERE first_name = ? AND last_name = ? "
            );
            for (Name nameToGet : name) {
                preparedStatement.setString(1, nameToGet.getFirstName());
                preparedStatement.setString(2,nameToGet.getLastName());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listOfids.add(resultSet.getInt("id"));
            }

            return listOfids;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    
}
