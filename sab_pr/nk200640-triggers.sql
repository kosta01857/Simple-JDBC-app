CREATE TRIGGER TR_TransportOffer_
   ON Ponuda 
   AFTER DELETE
AS 
BEGIN
	 declare @idP int
	 select @idP = d.idP from deleted d
	 delete from Ponuda where Ponuda.idP=@idP

END
GO