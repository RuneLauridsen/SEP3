global using static AdminClient.Data.Messages;
using AdminClient.DTO;

using boolean = System.Boolean;

namespace AdminClient.Data;

public class Messages
{
    public record ApproveUserRequest(Member member);

    public record ApproveUserResponse(boolean b);
    public record RejectUserRequest(Member member);

    public record RejectUserResponse(boolean b);

    public record GetUsersWaitingForApprovalRequest();
    public record GetUsersWaitingForApprovalResponse(List<Member> Members);
}