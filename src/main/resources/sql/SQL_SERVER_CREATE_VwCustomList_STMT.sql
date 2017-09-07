CREATE VIEW vwCustomList
AS
SELECT CustomList.CustomListId AS Id, CustomList.CustomListName AS Name,
       CustomList.CustomListDescription As Description, 
	   COUNT(CustomListValue.CustomListValueId) AS 'Total Values'
	   FROM CustomList
	   LEFT JOIN CustomListValue ON CustomList.CustomListId = CustomListValue.CustomListId
	   GROUP BY CustomList.CustomListId, CustomList.CustomListName, CustomList.CustomListDescription;