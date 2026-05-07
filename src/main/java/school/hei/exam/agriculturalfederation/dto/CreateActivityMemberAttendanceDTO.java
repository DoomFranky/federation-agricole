package school.hei.exam.agriculturalfederation.dto;

public class CreateActivityMemberAttendanceDTO {
    private String memberIdentifier;
    private String attendanceStatus;

    public CreateActivityMemberAttendanceDTO() {}

    public CreateActivityMemberAttendanceDTO(String memberIdentifier, String attendanceStatus) {
        this.memberIdentifier = memberIdentifier;
        this.attendanceStatus = attendanceStatus;
    }

    public String getMemberIdentifier() { return memberIdentifier; }
    public void setMemberIdentifier(String memberIdentifier) { this.memberIdentifier = memberIdentifier; }
    public String getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
}
