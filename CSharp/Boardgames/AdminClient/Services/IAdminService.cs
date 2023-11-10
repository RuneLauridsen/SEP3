using GameClient.DTO;
using Shared.Data;
using static Shared.Data.Messages;

namespace AdminClient.Services;

public interface IAdminService
{
    public ApproveUserResponse AcceptMember(Account account);
    public RejectUserResponse RejectMember(Account account);

    public List<Account> GetUsersWaitingForApproval();
}