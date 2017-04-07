CREATE VIEW vwUser
AS
SELECT SysUser.UserId, SysUser.UserName, SysUser.IsActiveDirectoryUser, SysRole.RoleName AS [Default Role]
FROM     SysUser INNER JOIN
                  UserRole ON SysUser.UserId = UserRole.UserId INNER JOIN
                  SysRole ON UserRole.RoleId = SysRole.RoleId
WHERE  (UserRole.IsDefaultRole = 1)