using Shared.Data;

namespace AdminClient.Services;

public class AdminService : IAdminService
{
    public Messages.ApproveUserResponse AcceptMember(Messages.ApproveUserRequest request)
    {
        throw new NotImplementedException();
    }

    public Messages.RejectUserResponse RejectMember(Messages.RejectUserRequest request)
    {
        throw new NotImplementedException();
    }

    public Messages.GetUsersWaitingForApprovalResponse GetUsersWaitingForApproval(Messages.GetUsersWaitingForApprovalRequest request)
    {
        throw new NotImplementedException();
    }
}