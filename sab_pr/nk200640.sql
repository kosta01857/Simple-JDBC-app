
CREATE TABLE [Grad]
( 
	[id]                 integer  NOT NULL identity(1,1),
	[postBr]             integer  NULL ,
	[naziv]              varchar(100)  NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[username]           varchar(100)  NOT NULL ,
	[ime]                varchar(100)  NOT NULL ,
	[prezime]            varchar(100)  NOT NULL ,
	[brPaketa]           integer  NOT NULL ,
	[tip]                char  NOT NULL 
	CONSTRAINT [user_default_type_185071153]
		 DEFAULT  'U'
	CONSTRAINT [user_type_valid_1870928896]
		CHECK  ( [tip]='U' OR [tip]='A' OR [tip]='K' ),
	[status]             integer  NULL 
	CONSTRAINT [kurir_status_valid_1686324589]
		CHECK  ( [status]=1 OR [status]=0 ),
	[profit]             decimal(10,3)  NULL ,
	[idV]                integer  NULL ,
	[password]           varchar(100)  NOT NULL 
)
go

CREATE TABLE [Opstina]
( 
	[idGrad]             integer  NOT NULL ,
	[naziv]              varchar(100)  NOT NULL ,
	[x]                  decimal(10,3)  NOT NULL ,
	[y]                  decimal(10,3)  NOT NULL ,
	[id]                 integer  NOT NULL identity(1,1)
)
go

CREATE TABLE [Ponuda]
( 
	[id]                 integer  NOT NULL identity(1,1),
	[idK]                varchar(100)  NOT NULL ,
	[idP]                integer  NOT NULL ,
	[procenat]           decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[id]                 integer  NOT NULL identity(1,1),
	[tip]                integer  NULL 
	CONSTRAINT [vozilo_type_valid_1619076587]
		CHECK  ( [tip]=0 OR [tip]=1 OR [tip]=2 ),
	[potrosnja]          decimal(10,3)  NULL ,
	[regBr]              varchar(100)  NULL 
)
go

CREATE TABLE [zahtevKurir]
( 
	[username]           varchar(100)  NOT NULL ,
	[idV]                integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[id]                 integer  NOT NULL identity(1,1),
	[tip]                integer  NOT NULL 
	CONSTRAINT [paket_tip_valid_571550610]
		CHECK  ( [tip]=0 OR [tip]=1 OR [tip]=2 ),
	[tezina]             decimal(10,3)  NOT NULL ,
	[status]             integer  NULL 
	CONSTRAINT [paket_status_valid_1961668017]
		CHECK  ( [status]=0 OR [status]=1 OR [status]=2 OR [status]=3 ),
	[cena]               decimal(10,3)  NULL ,
	[vremePrihv]         datetime  NULL ,
	[idKurir]            varchar(100)  NULL ,
	[idKor]              varchar(100)  NOT NULL ,
	[idSrc]              integer  NOT NULL ,
	[idDst]              integer  NOT NULL 
)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [uq_postBr] UNIQUE ([postBr]  ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([username] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XAK1regBr] UNIQUE ([regBr]  ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKzahtevPaket] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([idV]) REFERENCES [Vozilo]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([idGrad]) REFERENCES [Grad]([id])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([idK]) REFERENCES [Korisnik]([username])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([idP]) REFERENCES [Paket]([id])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go
ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKponuda] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [zahtevKurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([username]) REFERENCES [Korisnik]([username])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [zahtevKurir]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([idV]) REFERENCES [Vozilo]([id])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([idKurir]) REFERENCES [Korisnik]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([idKor]) REFERENCES [Korisnik]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([idSrc]) REFERENCES [Opstina]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([idDst]) REFERENCES [Opstina]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
