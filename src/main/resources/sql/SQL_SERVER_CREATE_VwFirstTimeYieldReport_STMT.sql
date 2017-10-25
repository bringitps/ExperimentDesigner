CREATE VIEW vwFirstTimeYieldReport
AS
SELECT     FirstTimeYieldRpt.FtyReportId AS 'Id'
FROM   FirstTimeYieldReport FirstTimeYieldRpt
WHERE FirstTimeYieldRpt.FtyReportIsActive = 1;