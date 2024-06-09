create PROCEDURE [dbo].[CenaJedneIsporuke]
	@idP int,@output float output
AS
BEGIN
	declare @basePrice float,@cenaKG float,@tezinskiFaktor float,@tip float,@tezina float,@idSrc int,@idDst int
	SELECT @tip = tip, @tezina = tezina,@idSrc = idSrc,@idDst = idDst from Paket where Paket.id = @idP
	if(@tip = 0)
	begin
		set @basePrice = 10
		set @tezinskiFaktor = 0
		set @cenaKG = 1
	end
	if(@tip = 1)
	begin
		set @basePrice = 25
		set @tezinskiFaktor = 1
		set @cenaKG = 100
	end
	if(@tip = 2)
	begin
		set @basePrice = 75
		set @tezinskiFaktor = 2
		set @cenaKG = 300
	end
	declare @x1 float,@y1 float,@y2 float,@x2 float,@distance float
	select @x1 = x,@y1 = y from Opstina where Opstina.id = @idDst
	select @x2 = x,@y2 = y from Opstina where Opstina.id = @idSrc
	
	set @distance = SQRT((POWER(@x1-@x2,2)+POWER(@y2-@y1,2)))

	set @output = (@basePrice + (@tezinskiFaktor*@tezina)*@cenaKG)*@distance
END
