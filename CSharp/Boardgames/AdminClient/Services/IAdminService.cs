namespace AdminClient.Services;

public interface IAdminService
{
    public ApproveUserResponse AcceptMember(ApproveUserRequest request);
    public RejectUserResponse RejectMember(RejectUserRequest request);

    public GetUsersWaitingForApprovalResponse GetUsersWaitingForApproval(GetUsersWaitingForApprovalRequest request);
}