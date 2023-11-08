namespace AdminClient.DTO;

public class Member
{
    public int Id { get; set; }
    public String Username { get; set; }
    public String FirstName { get; set; }
    public String LastName { get; set; }
    public String Email { get; set; }
    public DateTime RegisterDateTime{ get; set; }
}