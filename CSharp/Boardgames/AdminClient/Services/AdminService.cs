using Shared.Data;
using static Shared.Data.Messages;
namespace AdminClient.Services;

public class AdminService : IAdminService
{
    private readonly ServiceSocket _socket;

    public AdminService() {
        _socket = new ServiceSocket("localhost", 1234);
        _socket.Connect();
    }
    public ApproveUserResponse AcceptMember(ApproveUserRequest request)
    {
        return _socket.SendAndReceive<ApproveUserResponse>(request);
    }

    public RejectUserResponse RejectMember(RejectUserRequest request)
    {
        return _socket.SendAndReceive<RejectUserResponse>(request);
    }

    public GetUsersWaitingForApprovalResponse GetUsersWaitingForApproval(GetUsersWaitingForApprovalRequest request)
    {
        return _socket.SendAndReceive<GetUsersWaitingForApprovalResponse>(request);
    }
}