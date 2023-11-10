using Shared.Data;

namespace AdminClient.Services;

public interface IAdminService
{
    public Messages.ApproveUserResponse AcceptMember(Messages.ApproveUserRequest request);
    public Messages.RejectUserResponse RejectMember(Messages.RejectUserRequest request);

    public Messages.GetUsersWaitingForApprovalResponse GetUsersWaitingForApproval(Messages.GetUsersWaitingForApprovalRequest request);
}