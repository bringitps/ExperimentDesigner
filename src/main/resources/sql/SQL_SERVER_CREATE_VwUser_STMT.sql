CREATE VIEW [dbo].[vwUser]
AS
SELECT dbo.SysUser.UserId, dbo.SysUser.UserName, dbo.SysUser.IsActiveDirectoryUser, dbo.SysRole.RoleName AS [Default Role]
FROM     dbo.SysUser INNER JOIN
                  dbo.UserRole ON dbo.SysUser.UserId = dbo.UserRole.UserId INNER JOIN
                  dbo.SysRole ON dbo.UserRole.RoleId = dbo.SysRole.RoleId
WHERE  (dbo.UserRole.IsDefaultRole = 1)