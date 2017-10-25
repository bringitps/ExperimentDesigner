CREATE VIEW vwFirstPassYieldReport
AS
SELECT     FirstPassYieldRpt.FpyReportId AS 'Id'
FROM   FirstPassYieldReport FirstPassYieldRpt
WHERE FirstPassYieldRpt.FpyReportIsActive = 1;