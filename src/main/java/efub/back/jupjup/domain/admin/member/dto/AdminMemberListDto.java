package efub.back.jupjup.domain.admin.member.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class AdminMemberListDto {
	private int memberCount;
	private List<AdminMemberDto> members;

	public AdminMemberListDto(int memberCount, List<AdminMemberDto> members) {
		this.memberCount = memberCount;
		this.members = members;
	}
}
