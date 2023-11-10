﻿using GameClient.DTO;
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
    public ApproveUserResponse AcceptMember(Account account)
    {
        return _socket.SendAndReceive<ApproveUserResponse>(new ApproveUserRequest(account));
    }

    public RejectUserResponse RejectMember(Account account)
    {
        return _socket.SendAndReceive<RejectUserResponse>(new RejectUserRequest(account));
    }

    public List<Account> GetUsersWaitingForApproval()
    {
        return _socket.SendAndReceive<GetUsersWaitingForApprovalResponse>(new GetUsersWaitingForApprovalRequest()).Members;
    }
}